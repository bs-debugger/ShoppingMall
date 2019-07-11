package com.xq.live.web.controllerForApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.ServiceAmount;
import com.xq.live.service.ServiceAmountService;
import com.xq.live.vo.in.ServiceAmountInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 缴纳服务费接口
 * Created by lipeng on 2018/7/27.
 */
@RestController
@RequestMapping(value = "/app/serviceAmount")
public class ServiceAmountForAppController {
    @Autowired
    private ServiceAmountService serviceAmountService;

    /**
     * 通过shopId查询商家的缴费记录
     * @param shopId
     * @return
     */
    @RequestMapping(value = "/selectByShopId",method = RequestMethod.GET)
    public BaseResp<ServiceAmount> selectByShopId(Long shopId){
        List<ServiceAmount> list = serviceAmountService.selectByShopId(shopId);
        if(list==null||list.size()==0){
            return new BaseResp<ServiceAmount>(ResultStatus.SUCCESS,null);
        }
        return new BaseResp<ServiceAmount>(ResultStatus.SUCCESS,list.get(list.size()-1));
    }

    /**
     * 通过shopId查询商家的所有缴费记录
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/allByShopId",method = RequestMethod.GET)
    public BaseResp<List<ServiceAmount>> allByShopId(ServiceAmountInVo inVo){
        List<ServiceAmount> list = serviceAmountService.allByShopId(inVo);
        if(list==null||list.size()==0){
            return new BaseResp<List<ServiceAmount>>(ResultStatus.SUCCESS,null);
        }
        return new BaseResp<List<ServiceAmount>>(ResultStatus.SUCCESS,list);
    }
}
