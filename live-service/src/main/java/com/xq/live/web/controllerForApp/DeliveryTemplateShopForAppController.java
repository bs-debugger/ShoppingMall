package com.xq.live.web.controllerForApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.service.DeliveryTemplateShopService;
import com.xq.live.vo.in.DeliveryTemplateShopInVo;
import com.xq.live.vo.out.DeliveryTemplateShopOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商家运费模板相关接口
 * Created by lipeng on 2018/12/26.
 */
@RestController
@RequestMapping(value = "/app/deliveryTemplateShop")
public class DeliveryTemplateShopForAppController {
    @Autowired
    private DeliveryTemplateShopService deliveryTemplateShopService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public BaseResp<Pager<DeliveryTemplateShopOut>> list(DeliveryTemplateShopInVo inVo){
        Pager<DeliveryTemplateShopOut> list = deliveryTemplateShopService.list(inVo);
        return new BaseResp<Pager<DeliveryTemplateShopOut>>(ResultStatus.SUCCESS,list);
    }
}
