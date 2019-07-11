package com.xq.live.web.controllerForApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.DictArea;
import com.xq.live.model.DictCity;
import com.xq.live.model.DictCounty;
import com.xq.live.model.DictProvince;
import com.xq.live.service.DictService;
import com.xq.live.vo.in.DictInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 省份，地市，县区，街道
 * Created by lipeng on 2018/3/23.
 */
@RestController
@RequestMapping(value = "/app/dict")
public class DictForAppController {
    @Autowired
    private DictService dictService;

    @RequestMapping(value = "/findDictProvience",method = RequestMethod.GET)
    public BaseResp<List<DictProvince>> findDictProvience(){
        List<DictProvince> dictProvience = dictService.findDictProvience();
        return new BaseResp<List<DictProvince>>(ResultStatus.SUCCESS,dictProvience);
    }

    @RequestMapping(value = "/findDictCity",method = RequestMethod.GET)
    public BaseResp<List<DictCity>> findDictCity(DictInVo inVo){
        List<DictCity> dictCity = dictService.findDictCity(inVo);
        return new BaseResp<List<DictCity>>(ResultStatus.SUCCESS,dictCity);
    }

    @RequestMapping(value = "/findDictCounty",method = RequestMethod.GET)
    public BaseResp<List<DictCounty>> findDictCounty(DictInVo inVo){
        List<DictCounty> dictCounty = dictService.findDictCounty(inVo);
        return new BaseResp<List<DictCounty>>(ResultStatus.SUCCESS,dictCounty);
    }

    @RequestMapping(value = "/findDictArea",method = RequestMethod.GET)
    public BaseResp<List<DictArea>> findDictArea(DictInVo inVo){
        List<DictArea> dictArea = dictService.findDictArea(inVo);
        return new BaseResp<List<DictArea>>(ResultStatus.SUCCESS,dictArea);
    }
}
