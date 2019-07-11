package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.OrderCouponMapper;
import com.xq.live.dao.OrderWriteOffMapper;
import com.xq.live.dao.UserMapper;
import com.xq.live.model.OrderWriteOff;
import com.xq.live.model.User;
import com.xq.live.service.OrderWriteOffService;
import com.xq.live.vo.in.OrderWriteOffInVo;
import com.xq.live.vo.out.OrderCouponOut;
import com.xq.live.vo.out.OrderWriteOffOut;
import com.xq.live.vo.out.OrderWriteOffResultOut;
import com.xq.live.web.utils.CutOutTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商城礼品券使用service
 * Created by lipeng on 2018/9/17.
 */
@Service
public class OrderWriteOffServiceImpl implements OrderWriteOffService {
    @Autowired
    private OrderWriteOffMapper orderWriteOffMapper;

    @Autowired
    private OrderCouponMapper orderCouponMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Long add(OrderWriteOff orderWriteOff) {
        int r = orderWriteOffMapper.insert(orderWriteOff);
        if(r == 0){
            return null;
        }
        return orderWriteOff.getId();
    }

    @Override
    public Pager<OrderWriteOffOut> list(OrderWriteOffInVo inVo) {
        Pager<OrderWriteOffOut> result = new Pager<OrderWriteOffOut>();
        int listTotal = orderWriteOffMapper.listTotal(inVo);
        if(listTotal > 0){
            List<OrderWriteOffOut> list = orderWriteOffMapper.list(inVo);
            for (OrderWriteOffOut orderWriteOffOut : list) {
                this.setShowCode(orderWriteOffOut);//设置showCode
            }
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        result.setTotal(listTotal);
        return result;
    }

    @Override
    public OrderWriteOffOut getDetail(Long id) {
        OrderWriteOffOut orderWriteOffOut = orderWriteOffMapper.selectDetailByPrimaryKey(id);
        this.setOrderCouponOut(orderWriteOffOut);//设置票券详情
        this.setShowCode(orderWriteOffOut);
        return orderWriteOffOut;
    }

    @Override
    public OrderWriteOffOut getDetailByOrderCouponCode(String orderCouponCode) {
        OrderWriteOffOut orderWriteOffOut = orderWriteOffMapper.selectDetailByOrderCouponCode(orderCouponCode);
        this.setOrderCouponOut(orderWriteOffOut);//设置票券详情
        this.setShowCode(orderWriteOffOut);
        return orderWriteOffOut;
    }

    public void setOrderCouponOut(OrderWriteOffOut orderWriteOffOut){
        OrderCouponOut orderCouponOut = orderCouponMapper.selectDetailByPrimaryKey(orderWriteOffOut.getOrderCouponId());
        this.setOwnNameAndChangerName(orderCouponOut);//设置拥有人名字和兑换人名字
        orderWriteOffOut.setOrderCouponOut(orderCouponOut);
    }

    private void setOwnNameAndChangerName(OrderCouponOut orderCouponOut) {
        User own = userMapper.selectByPrimaryKey(orderCouponOut.getOwnId());
        User changer = userMapper.selectByPrimaryKey(orderCouponOut.getChangerId());
        if(own!=null&&own.getId()!=null){
            orderCouponOut.setOwnName(own.getUserName());
        }
        if (changer!=null&&changer.getId()!=null){
            orderCouponOut.setChangerName(changer.getUserName());
        }

    }

    @Override
    public List<OrderWriteOffOut> listAmount(OrderWriteOffInVo inVo) {
        List<OrderWriteOffOut> list=orderWriteOffMapper.selectTotalAmount(inVo);
        if (list==null||list.size()<=0){
            return null;
        }
        return list;
    }

    @Override
    public Pager<OrderWriteOffOut> listForShop(OrderWriteOffInVo inVo) {
        Pager<OrderWriteOffOut> ret = new Pager<OrderWriteOffOut>();
        int total = orderWriteOffMapper.listTotal(inVo);
        List<OrderWriteOffOut> totalOut = orderWriteOffMapper.selectTotalAmount(inVo);
        if(total > 0){
            List<OrderWriteOffOut> list = orderWriteOffMapper.list(inVo);
            for (OrderWriteOffOut orderWriteOffOut : list) {
                this.setShowCode(orderWriteOffOut);//设置showCode
            }
            list.addAll(0,totalOut);//把总销售额和总服务费放到list的第一个数据里面
            ret.setList(list);
        }
        ret.setTotal(total);
        ret.setPage(inVo.getPage());
        return ret;
    }

    private void setShowCode(OrderWriteOffOut orderWriteOffOut) {
        if(StringUtils.isBlank(orderWriteOffOut.getShowCode())){
            SimpleDateFormat df = new SimpleDateFormat("MMdd");
            String date = df.format(orderWriteOffOut.getCreateTime());
            String s = CutOutTimeUtils.addZeroForNum(orderWriteOffOut.getOrderCouponId().toString(), 6);
            String showCode = date+s;
            orderWriteOffOut.setShowCode(showCode);
        }
    }

    @Override
    @Transactional
    public int updateByShopId(OrderWriteOffInVo inVo) {
        int i = orderWriteOffMapper.updateByShopId(inVo);
        return i;
    }

    @Override
    public Map<String, Object> selectWanDaWriteOff(OrderWriteOffResultOut inVo) {
        List<OrderWriteOffResultOut> orderWriteOffResultOuts= orderWriteOffMapper.selectWanDaWriteOff(inVo);
        Map<String,Object> map = new HashMap<String, Object>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i=0;i< orderWriteOffResultOuts.size();i++){
            OrderWriteOffResultOut orderWriteOffResultOut=orderWriteOffResultOuts.get(i);
            orderWriteOffResultOut.setState("正常");

            orderWriteOffResultOut.setUsedTime(formatter.format(orderWriteOffResultOut.getCreateTime()));
            if(i>0){//三小时内2单
                OrderWriteOffResultOut orderWriteOffResultOut1=orderWriteOffResultOuts.get(i-1);
                Long a=orderWriteOffResultOut.getCreateTime().getTime()/(1000*60*60);
                Long b=orderWriteOffResultOut1.getCreateTime().getTime()/(1000*60*60);
                int c=b.intValue()-a.intValue();
                if(orderWriteOffResultOut1.getShopZoneName().equals(orderWriteOffResultOut.getShopZoneName())&&orderWriteOffResultOut1.getShopName().equals(orderWriteOffResultOut.getShopName())&&
                  orderWriteOffResultOut1.getUserName().equals(orderWriteOffResultOut.getUserName())
                        &&a.intValue()-b.intValue()<3){
                    orderWriteOffResultOuts.get(i).setState("疑似刷单");
                    orderWriteOffResultOuts.get(i-1).setState("疑似刷单");
                }
            }

            if(i>1){//一天内2单以上
                OrderWriteOffResultOut orderWriteOffResultOut1=orderWriteOffResultOuts.get(i-2);
                if(orderWriteOffResultOut1.getShopZoneName().equals(orderWriteOffResultOut.getShopZoneName())&&orderWriteOffResultOut1.getShopName().equals(orderWriteOffResultOut.getShopName())&&
                        orderWriteOffResultOut1.getUserName().equals(orderWriteOffResultOut.getUserName())&&DateUtils.isSameDay(orderWriteOffResultOut.getCreateTime(),orderWriteOffResultOut1.getCreateTime())){
                    orderWriteOffResultOuts.get(i).setState("疑似刷单");
                    orderWriteOffResultOuts.get(i-1).setState("疑似刷单");
                    orderWriteOffResultOuts.get(i-2).setState("疑似刷单");
                }
            }

            if(i>3){//七天内五单以上
                OrderWriteOffResultOut orderWriteOffResultOut1=orderWriteOffResultOuts.get(i-4);
                if(orderWriteOffResultOut1.getShopZoneName().equals(orderWriteOffResultOut.getShopZoneName())&&orderWriteOffResultOut1.getShopName().equals(orderWriteOffResultOut.getShopName())&&
                        orderWriteOffResultOut1.getUserName().equals(orderWriteOffResultOut.getUserName())
                        &&orderWriteOffResultOut.getCreateTime().getDay()-orderWriteOffResultOut1.getCreateTime().getDay()<8){
                    orderWriteOffResultOuts.get(i).setState("疑似刷单");
                    orderWriteOffResultOuts.get(i-1).setState("疑似刷单");
                    orderWriteOffResultOuts.get(i-2).setState("疑似刷单");
                    orderWriteOffResultOuts.get(i-3).setState("疑似刷单");
                    orderWriteOffResultOuts.get(i-4).setState("疑似刷单");
                }
            }
        }
        int normal=0;
        int other=0;
        BigDecimal realTotalPrice=BigDecimal.ZERO;
        BigDecimal totalPrice=BigDecimal.ZERO;
        BigDecimal otherTotalPrice=BigDecimal.ZERO;
        for(OrderWriteOffResultOut orderWriteOffResultOut:orderWriteOffResultOuts){
            totalPrice=totalPrice.add( orderWriteOffResultOut.getRealUnitPrice());
            if("疑似刷单".equals(orderWriteOffResultOut.getState())){
                otherTotalPrice=otherTotalPrice.add( orderWriteOffResultOut.getRealUnitPrice());
                other+=1;
            }else {
                realTotalPrice=realTotalPrice.add( orderWriteOffResultOut.getRealUnitPrice());
                normal+=1;
            }
        }
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd ");
        map.put("startTime",formatter1.format(inVo.getStartTime()));
        map.put("endTime",formatter1.format(inVo.getEndTime()));
        map.put("other",other);
        map.put("normal",normal);
        map.put("total",orderWriteOffResultOuts.size());
        map.put("list",orderWriteOffResultOuts);
        map.put("realTotalPrice",realTotalPrice);
        map.put("otherTotalPrice",otherTotalPrice);
        map.put("totalPrice",totalPrice);
        map.put("shopZoneName",orderWriteOffResultOuts.get(0).getShopZoneName());
        return map;
    }
}
