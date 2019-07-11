package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.service.OrderDeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通过物流单号查询物流
 * Created by admin on 2018/9/21.
 */

@RestController
@RequestMapping(value = "/orderDelivery")
public class OrderDeliveryController {

    @Autowired
    private OrderDeliveryService orderDeliveryService;

    @RequestMapping(value = "/getExpress",method = RequestMethod.GET)
    public BaseResp<List<Map<String, Object>>> getExpress(String expressCode) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, Object>> re=  orderDeliveryService.getDetail(expressCode);
        return new BaseResp<List<Map<String, Object>>>(ResultStatus.SUCCESS,re);
    }
}
