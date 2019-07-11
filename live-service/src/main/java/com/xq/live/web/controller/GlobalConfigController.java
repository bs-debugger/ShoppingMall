package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.GlobalConfig;
import com.xq.live.service.GlobalConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 全局配置接口
 */
@RestController
@RequestMapping(value = "/globalConfig")
public class GlobalConfigController {
    @Autowired
    private GlobalConfigService globalConfigService;

    /**
     * 查询全局配置
     * @param globalConfig
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public BaseResp<Map<String,GlobalConfig>> list(GlobalConfig globalConfig){
        Map<String,GlobalConfig> list = globalConfigService.list(globalConfig);
        return new BaseResp<Map<String,GlobalConfig>>(ResultStatus.SUCCESS,list);
    }


    /**
     * 删除一条配置
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public BaseResp<Integer> delete(Long id){
        Integer re = globalConfigService.delete(id);
        if(re > 0) {
            return new BaseResp<Integer>(ResultStatus.SUCCESS);
        }
        return new BaseResp<Integer>(ResultStatus.FAIL);
    }

    /**
     * 增加一条配置
     * @param globalConfig
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public BaseResp<Long> add(GlobalConfig globalConfig){
        Long re = globalConfigService.add(globalConfig);
        if(re != null) {
            return new BaseResp<Long>(ResultStatus.SUCCESS, re);
        }
        return new BaseResp<Long>(ResultStatus.FAIL);
    }
}
