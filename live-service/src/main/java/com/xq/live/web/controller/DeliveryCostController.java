package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.config.CostWeightConfig;
import com.xq.live.model.DeliveryTemplate;
import com.xq.live.service.DeliveryCostService;
import com.xq.live.vo.in.OrderAddressInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 配送相关接口
 * Created by lipeng on 2018/9/18.
 */
@RestController
@RequestMapping(value = "/deliveryCost")
public class DeliveryCostController {
    @Autowired
    private DeliveryCostService deliveryCostService;

    @Autowired
    private CostWeightConfig costWeightConfig;

    /**
     * 查看商品的配送的信息
     *
     * 注意:也可以用此接口判断某个商品是否包邮
     * @param goodsSkuId
     * @return
     */
    @RequestMapping(value = "/findByGoodsSkuId",method = RequestMethod.GET)
    public BaseResp<Map<String,Object>> findByGoodsSkuId(Long goodsSkuId){
        Map<String,Object> map = deliveryCostService.findByGoodsSkuId(goodsSkuId);
        return new BaseResp<Map<String,Object>>(ResultStatus.SUCCESS,map);
    }

    /**
     *
     * 根据地址和重量还有模版id计算运费
     *
     * 注:入参:dictProvinceId,dictCityId,weight,tempateId
     * @param orderAddressInVo
     * @param weight
     * @param tempateId
     * @param formulaMode 计算方式
     * @return
     */
    @RequestMapping(value = "/calculateCost",method = RequestMethod.POST)
    public BaseResp<BigDecimal> calculateCost(OrderAddressInVo orderAddressInVo, Double weight,Integer piece,BigDecimal bulk,Long tempateId,Integer formulaMode){
        Double[] area = costWeightConfig.getArea();
        Double[] costWeight = costWeightConfig.getWeight();
        BigDecimal realWeight = BigDecimal.ZERO;
        if (weight!=null){
            realWeight = new BigDecimal(weight);//这里是kg
        }
        BigDecimal bigDecimal = deliveryCostService.calculateCost(orderAddressInVo, realWeight.setScale(2, BigDecimal.ROUND_HALF_UP),piece,bulk,tempateId, formulaMode);
        return new BaseResp<BigDecimal>(ResultStatus.SUCCESS,bigDecimal);
    }

    /**
     *
     * 给与固定运费
     *
     * 注:入参:dictProvinceId,dictCityId,weight,tempateId
     * @param orderAddressInVo
     * @param weight
     * @param tempateId
     * @return
     */
    @RequestMapping(value = "/calculateCostForCoupon",method = RequestMethod.POST)
    public BaseResp<BigDecimal> calculateCostForCoupon(OrderAddressInVo orderAddressInVo, Double weight,Long tempateId){
        Double[] area = costWeightConfig.getArea();
        Double[] costWeight = costWeightConfig.getWeight();
        BigDecimal realWeight = BigDecimal.ZERO;//这里是kg
        if(weight==0){
            realWeight = BigDecimal.ZERO;
        }else if(weight>0&&weight<=area[0]){
            realWeight = new BigDecimal(2.5);
        }else if(weight>area[0]&&weight<=area[1]){
            realWeight = BigDecimal.valueOf(weight).multiply(BigDecimal.valueOf(costWeight[0]));
        }else if(weight>area[1]&&weight<=area[2]){
            realWeight = BigDecimal.valueOf(weight).multiply(BigDecimal.valueOf(costWeight[1]));
        }else if(weight>area[2]){
            realWeight = BigDecimal.valueOf(weight).multiply(BigDecimal.valueOf(costWeight[2]));
        }
        BigDecimal bigDecimal = deliveryCostService.calculateCost(orderAddressInVo, realWeight.setScale(2, BigDecimal.ROUND_HALF_UP),1,BigDecimal.ZERO, tempateId,0);
        return new BaseResp<BigDecimal>(ResultStatus.SUCCESS,new BigDecimal(30));
    }
}
