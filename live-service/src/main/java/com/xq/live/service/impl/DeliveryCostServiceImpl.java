package com.xq.live.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xq.live.dao.*;
import com.xq.live.model.DeliveryFree;
import com.xq.live.model.DeliveryMethod;
import com.xq.live.model.DeliveryTemplate;
import com.xq.live.model.GoodsSku;
import com.xq.live.service.DeliveryCostService;
import com.xq.live.vo.in.OrderAddressInVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * com.xq.live.service.impl
 *
 * @author zhangpeng32
 * Created on 2018/9/15 下午12:29
 * @Description:
 */
@Service
public class DeliveryCostServiceImpl implements DeliveryCostService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private DeliveryMethodMapper deliveryMethodMapper;
    @Autowired
    private DeliveryTemplateMapper deliveryTemplateMapper;
    @Autowired
    private GoodsSkuMapper goodsSkuMapper;
    @Autowired
    private DeliveryFreeMapper deliveryFreeMapper;


    @Override
    public BigDecimal calculateCost(OrderAddressInVo orderAddressInVo, BigDecimal weight,Integer piece,BigDecimal bulk,Long tempateId,Integer formulaMode) {
        if (formulaMode==null){
            formulaMode=0;
        }
        BigDecimal dmFee = BigDecimal.ZERO; //运费

        DeliveryTemplate deliveryTemplate = deliveryTemplateMapper.selectByPrimaryKey(tempateId);
        if(deliveryTemplate==null){
            throw new RuntimeException("配送信息有误,请联系客服!");
        }

        //当前选中的dm
        DeliveryMethod deliveryMethod = null;
        DeliveryFree deliveryFree =null;
        String delieryArea = orderAddressInVo.getDictProvinceId() + "-" + orderAddressInVo.getDictCityId(); //拼接配送范围  省id-市id

        if(deliveryTemplate.getIsFree()==DeliveryTemplate.DELIVERY_TEMPLATE_NO_FREE){
            //根据发货模板查询配送范围
            List<DeliveryMethod> deliveryMethods = deliveryMethodMapper.findListByTemplateId(deliveryTemplate.getId());
            for(DeliveryMethod dm : deliveryMethods){
                String regoins = dm.getRegion();
                if(StringUtils.isNotEmpty(regoins)){
                    String[] regoinArr = regoins.split("\\|");
                    List<String> stringList = Arrays.asList(regoinArr);
                    if(regoinArr != null && regoinArr.length > 0 && Arrays.asList(regoinArr).contains(delieryArea)){  //匹配  省id-市id
                        deliveryMethod =  dm;  //当前选中的dm,计算得到
                        break;
                    }
                }
            }
        }else{
            //根据发货模板查询配送范围
            List<DeliveryFree> deliveryFrees = deliveryFreeMapper.findListByTemplateId(deliveryTemplate.getId());
            for(DeliveryFree dm : deliveryFrees){
                String regoins = dm.getRegion();
                if(StringUtils.isNotEmpty(regoins)){
                    String[] regoinArr = regoins.split("\\|");
                    List<String> stringList = Arrays.asList(regoinArr);
                    if(regoinArr != null && regoinArr.length > 0 && Arrays.asList(regoinArr).contains(delieryArea)){  //匹配  省id-市id
                        deliveryFree =  dm;  //当前选中的dm,计算得到
                        break;
                    }
                }
            }
        }

        if(deliveryMethod==null&&deliveryFree==null){
            throw new RuntimeException("该收货地址不在配送范围,请重新选择收货地址!");
        }

        //如果是包邮的话，运费就是为0
        if(deliveryTemplate.getIsFree()==DeliveryTemplate.DELIVERY_TEMPLATE_IS_FREE){
            return dmFee;
        }

        switch (formulaMode){
            //重量
            case 0:
                //如果重量为0,则代表是礼盒包邮
                if(weight.compareTo(BigDecimal.ZERO)==0){
                    return BigDecimal.ZERO;
                }
                //首重 为1 公斤
                BigDecimal firstWeight = deliveryMethod.getFirstWeight();
                //首费
                BigDecimal firstAmount =deliveryMethod.getFirstAmount();
                //续重 目前为 1公斤
                BigDecimal secondweight = deliveryMethod.getSecondAmount();
                //续费
                BigDecimal secondAmount = deliveryMethod.getSecondAmount();

                //如果当前重量weight < firstWeight,则按首重计算运费
                if(weight.compareTo(firstWeight) < 0){
                    dmFee = firstAmount;
                }else{
                    //总运费=首重单价+(计费重量-首重重量)*续重单价;
                    dmFee = firstAmount.add(weight.subtract(firstWeight).multiply(secondAmount));
                }
                return dmFee;
            //件数
            case 1:
                //首件 为1
                Integer firstPiece = deliveryMethod.getFirstPiece();
                //首费
                BigDecimal firstAmountPiece =deliveryMethod.getFirstAmount();
                //续件
                Integer secondPiece = deliveryMethod.getSecondPiece();
                //续费
                BigDecimal secondAmountPiece = deliveryMethod.getSecondAmount();

                //如果当前件数为一件,则按首件首费计算运费
                if(piece== 1){
                    dmFee = firstAmountPiece;
                }else{
                    //总运费=首重单价+(续件-首件)*续重单价;
                    dmFee = firstAmountPiece.add(new BigDecimal(piece - firstPiece).multiply(secondAmountPiece));
                }
                return dmFee;
            //体积
            case 2:
                //体积为 为1
                BigDecimal firstBulk = deliveryMethod.getFirstBulk();
                //体积首费
                BigDecimal firstAmountBulk =deliveryMethod.getFirstAmount();
                //续体积
                BigDecimal secondBulk = deliveryMethod.getSecondAmount();
                //体积续费
                BigDecimal secondAmountBulk = deliveryMethod.getSecondAmount();

                //如果当前重量bulk < firstWeight,则按首重计算运费
                if(bulk.compareTo(firstBulk) < 0){
                    dmFee = firstAmountBulk;
                }else{
                    //总运费=首重单价+(计费重量-首重重量)*续重单价;
                    dmFee = firstAmountBulk.add(bulk.subtract(firstBulk).multiply(secondAmountBulk));
                }
                return dmFee;
            //默认重量
            default:
                //如果重量为0,则代表是礼盒包邮
                if(weight.compareTo(BigDecimal.ZERO)==0){
                    return BigDecimal.ZERO;
                }
                //首重 为1 公斤
                BigDecimal firstWeights = deliveryMethod.getFirstWeight();
                //首费
                BigDecimal firstAmounts =deliveryMethod.getFirstAmount();
                //续重 目前为 1公斤
                BigDecimal secondweights = deliveryMethod.getSecondAmount();
                //续费
                BigDecimal secondAmounts = deliveryMethod.getSecondAmount();

                //如果当前重量weight < firstWeight,则按首重计算运费
                if(weight.compareTo(firstWeights) < 0){
                    dmFee = firstAmounts;
                }else{
                    //总运费=首重单价+(计费重量-首重重量)*续重单价;
                    dmFee = firstAmounts.add(weight.subtract(firstWeights).multiply(secondAmounts));
                }
                return dmFee;
        }
    }

    @Override
    public List<DeliveryMethod> findListByTemplateId(Long tempateId) {
        //根据发货模板查询配送范围
        List<DeliveryMethod> list = deliveryMethodMapper.findListByTemplateId(tempateId);
        return list;
    }

    @Override
    public Map<String, Object> findByGoodsSkuId(Long goodsSkuId) {
        GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(goodsSkuId);
        if(goodsSku==null){
            return null;
        }
        DeliveryTemplate deliveryTemplate = deliveryTemplateMapper.selectByPrimaryKey(goodsSku.getDeliveryTemplateId());
        if(deliveryTemplate==null){
            return null;
        }
        Map<String,Object> map = new HashMap<String,Object>();
        if(deliveryTemplate.getIsFree()==DeliveryTemplate.DELIVERY_TEMPLATE_NO_FREE){
            //根据发货模板查询配送范围
            List<DeliveryMethod> deliveryMethods = deliveryMethodMapper.findListByTemplateId(deliveryTemplate.getId());
            map.put(deliveryTemplate.getIsFree().toString(),deliveryMethods);
        }else{
            List<DeliveryFree> deliveryFrees = deliveryFreeMapper.findListByTemplateId(deliveryTemplate.getId());
            map.put(deliveryTemplate.getIsFree().toString(),deliveryFrees);
        }

        return map;
    }
}
