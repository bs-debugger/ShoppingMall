package com.xq.live.web.controllerForApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.service.ShopAllocationService;
import com.xq.live.vo.in.ShopAllocationInVo;
import com.xq.live.vo.out.ShopAllocationOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by ss on 2018/5/26.
 */
@RestController
@RequestMapping(value = "/app/shopAllocation")
public class ShopAllocationForAppController {

    @Autowired
    private ShopAllocationService allocationService;

    /**
     *  通过id,shopid或者operatorid查看多条数据
     * @param allocationInVo
     * @return
     */
    @RequestMapping(value = "/selectForList",method = RequestMethod.GET)
    public BaseResp<List<ShopAllocationOut>> ListForID(ShopAllocationInVo allocationInVo){
        if(allocationInVo==null){
            return new BaseResp<List<ShopAllocationOut>>(ResultStatus.error_param_empty);
        }
        List<ShopAllocationOut> list= allocationService.listAll(allocationInVo);
        if (list==null){
            return new BaseResp<List<ShopAllocationOut>>(ResultStatus.getError_allocation_selectList);
        }
        return new BaseResp<List<ShopAllocationOut>>(ResultStatus.SUCCESS,list);
    }

    /**
     * 通过id,shopid查看单条数据
     * @param allocationInVo
     * @return
     */
    @RequestMapping(value = "/selectForOne",method = RequestMethod.GET)
    public BaseResp<ShopAllocationOut> selectByInfo(ShopAllocationInVo allocationInVo){
        if(allocationInVo==null){
            return new BaseResp<ShopAllocationOut>(ResultStatus.error_param_empty);
        }
        ShopAllocationOut allocationOut=allocationService.admin(allocationInVo);
        if (allocationOut==null){
            return new BaseResp<ShopAllocationOut>(ResultStatus.getError_allocation_selectList);
        }
        return new BaseResp<ShopAllocationOut>(ResultStatus.SUCCESS,allocationOut);
    }

    /**
     * 根据id或者shopid修改信息
     * @param allocationInVo
     * @return
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public BaseResp<Integer> adminByID(ShopAllocationInVo allocationInVo){
        if(allocationInVo==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        Integer i= allocationService.updateForPayment(allocationInVo);
        if (i>0){
            return new BaseResp<Integer>(ResultStatus.SUCCESS,i);
        }else {
            return new BaseResp<Integer>(ResultStatus.error_allocation_update);
        }
    }

    /**
     * 添加信息
     * @param allocationInVo
     * @return
     */
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    public BaseResp<Integer> insertForAdmin(ShopAllocationInVo allocationInVo){
        if(allocationInVo==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        Integer i= allocationService.insertInfo(allocationInVo);
        if (i>0){
            return new BaseResp<Integer>(ResultStatus.SUCCESS,i);
        }else {
            return new BaseResp<Integer>(ResultStatus.error_allocation_insert);
        }
    }

    /**
     * 删除信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public BaseResp<Integer> deleteForId(Long id){
        if(id==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        Integer i= allocationService.deleteForShopConfig(id);
        if (i>0){
            return new BaseResp<Integer>(ResultStatus.SUCCESS,i);
        }else {
            return new BaseResp<Integer>(ResultStatus.error_allocation_insert);
        }
    }
}
