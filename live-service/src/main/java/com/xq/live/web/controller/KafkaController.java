package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lipeng on 2018/11/29.
 */
@RestController
@RequestMapping("/kafka")
public class KafkaController {
    @Autowired
    private KafkaService kafkaService;

    @RequestMapping(value = "/createTopic",method = RequestMethod.GET)
    public BaseResp<Boolean> createTopic(){
        Boolean lipengTopic = kafkaService.createTopic("batch.order.topic", 5);
        return new BaseResp<Boolean>(ResultStatus.SUCCESS,lipengTopic);
    }

    @RequestMapping(value = "/sendData",method = RequestMethod.GET)
    public BaseResp<Boolean> sendData(){
        for(int i=0;i<5;i++) {
            Boolean lipengTopic = kafkaService.sendDataToTopic("newTest", i + "newTest", i + "newTest");
        }
        return new BaseResp<Boolean>(ResultStatus.SUCCESS,true);
    }

    @RequestMapping(value = "/isExistTopic",method = RequestMethod.GET)
    public BaseResp<Boolean> isExistTopic(){
        Boolean lipengTopic = kafkaService.isExistTopic("lipengTopic");
        return new BaseResp<Boolean>(ResultStatus.SUCCESS,lipengTopic);
    }
}
