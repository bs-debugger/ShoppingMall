package com.xq.live.service.impl;

import com.xq.live.dao.DeliveryTemplateMapper;
import com.xq.live.dao.OrderAddressMapper;
import com.xq.live.dao.OrderCouponPostageMapper;
import com.xq.live.model.*;
import com.xq.live.service.*;
import com.xq.live.vo.in.OrderAddressInVo;
import com.xq.live.vo.in.OrderCouponInVo;
import com.xq.live.vo.in.OrderCouponPostageInVo;
import com.xq.live.vo.out.OrderAddressOut;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by ss on 2019/1/25.
 * 票券运费订单业务
 */
@Service
public class OrderCouponPostageServiceImpl implements OrderCouponPostageService{

    @Autowired
    private DeliveryTemplateMapper deliveryTemplateMapper;

    @Autowired
    private OrderCouponPostageMapper orderCouponPostageMapper;

    @Autowired
    private OrderAddressMapper orderAddressMapper;

    @Autowired
    private DeliveryCostService deliveryCostService;

    @Autowired
    private OrderCouponService orderCouponService;

    @Autowired
    private UserService userService;

    @Autowired
    private ShopService shopService;

    /**
     * 创建运费订单
     * @param inVo
     */
    @Override
    public Long postageInfo(OrderCouponPostageInVo inVo) {
        //是否包邮
        this.poolPostageFree(inVo);
        if (inVo.getPostFree()==OrderCouponPostage.PASTAGE_FREE_NOT_FREE){
            //计算运费
            this.sendAmout(inVo);
            //创建订单
            Long id=this.areatePostageFreeInfo(inVo);
            return id;
        }else {
            //免运费
            inVo.setSendAmount(BigDecimal.ZERO);
            //创建订单
            Long id=this.areatePostageFreeInfo(inVo);
            //核销票券
            this.hxCouponLog(inVo);
            inVo.setId(id);
            //修改运费订单状态
            this.updateOrderStatus(inVo);
            return id;
        }
    }

    /**
     * 查询订单
     * @param inVo
     */
    @Override
    public OrderCouponPostage selectInfo(OrderCouponPostageInVo inVo) {
        return orderCouponPostageMapper.selectByPrimaryKey(inVo.getId());
    }

    /*是否包邮*/
    void poolPostageFree(OrderCouponPostageInVo inVo){
        DeliveryTemplate deliveryTemplate= deliveryTemplateMapper.selectByPrimaryKey(inVo.getTemplateId());
        if (deliveryTemplate==null||deliveryTemplate.getId()==null){
            throw new RuntimeException("未能查到相应的配送模板!");
        }
        inVo.setPostFree(deliveryTemplate.getIsFree());
    }

    /*创建订单*/
    Long areatePostageFreeInfo(OrderCouponPostageInVo inVo){
        Long LongTime = System.currentTimeMillis() / 1000;
        OrderCouponPostage orderCouponPostage = new OrderCouponPostage();
        orderCouponPostage.setUserId(inVo.getUserId());
        orderCouponPostage.setShopId(inVo.getShopId());
        orderCouponPostage.setBulk(inVo.getBulk());
        orderCouponPostage.setRealWeight(inVo.getRealWeight());
        orderCouponPostage.setPiece(inVo.getPiece());
        orderCouponPostage.setRemark(inVo.getRemark());
        orderCouponPostage.setCouponAddressId(inVo.getCouponAddressId());
        orderCouponPostage.setOrderCouponId(inVo.getOrderCouponId());
        orderCouponPostage.setOrderCouponCode(inVo.getOrderCouponCode());
        orderCouponPostage.setPostFree(inVo.getPostFree());
        orderCouponPostage.setSendTime(inVo.getSendTime());
        orderCouponPostage.setOutTradeNo(LongTime.toString()+inVo.getOrderCouponCode());
        orderCouponPostage.setStatus(OrderCouponPostage.POSTAGE_STATUS_NOT_PAY);
        orderCouponPostage.setTemplateId(inVo.getTemplateId());
        orderCouponPostage.setSendAmount(inVo.getSendAmount());
        Integer i= orderCouponPostageMapper.insert(orderCouponPostage);
        if (i<1){
            throw new RuntimeException("添加运费订单错误!");
        }
        return orderCouponPostage.getId();
    }

    /*计算运费*/
    void sendAmout(OrderCouponPostageInVo inVo){
        //查询收货地址信息
        OrderAddressOut address = orderAddressMapper.getAddress(inVo.getCouponAddressId());
        if (address==null||address.getId()==null){
            throw new RuntimeException("未查到收获地址!");
        }
        OrderAddressInVo orderAddressInVo = new OrderAddressInVo();
        orderAddressInVo.setDictProvinceId(address.getDictProvinceId());
        orderAddressInVo.setDictCityId(address.getDictCityId());
        BigDecimal realWeight = new BigDecimal(inVo.getRealWeight().doubleValue());
        //写入运费
        inVo.setSendAmount(deliveryCostService.calculateCost(orderAddressInVo, realWeight, inVo.getPiece(), inVo.getBulk(), inVo.getTemplateId(), inVo.getFormulaMode()));
    }

    //核销票券
    void hxCouponLog(OrderCouponPostageInVo inVo){
        this.setShopName(inVo);//设置商家名字
        //修改票券状态
        User user = userService.getUserById(inVo.getUserId());
        OrderCouponInVo couponInVo = new OrderCouponInVo();
        couponInVo.setChangerId(user.getId());
        couponInVo.setChangerName(user.getUserName());
        couponInVo.setId(inVo.getOrderCouponId());
        couponInVo.setSendTime(inVo.getSendTime().toString());
        couponInVo.setRemark(inVo.getRemark());
        couponInVo.setSendAmount(inVo.getSendAmount());
        couponInVo.setCouponAddressId(inVo.getCouponAddressId());
        couponInVo.setShopId(inVo.getShopId());
        couponInVo.setShopName(inVo.getShopName());
        //修改票券
        orderCouponService.useCoupon(couponInVo);
    }

    //设置商家名字
    public void setShopName(OrderCouponPostageInVo inVo){
        if(new Long(0).toString().equals(inVo.getShopId().toString())){
            inVo.setShopName("享七自营");
        }else{
            Shop shopById = shopService.getShopById(inVo.getShopId());
            inVo.setShopName(shopById.getShopName());
        }
    }

    /*
    * 修改运费订单状态*/
    @Override
     public void updateOrderStatus(OrderCouponPostageInVo inVo){
        OrderCouponPostage orderCouponPostage = new OrderCouponPostage();
        orderCouponPostage.setId(inVo.getId());
        orderCouponPostage.setStatus(OrderCouponPostage.POSTAGE_STATUS_HAD_PAY);
        orderCouponPostageMapper.updateByPrimaryKeySelective(orderCouponPostage);
    }
}
