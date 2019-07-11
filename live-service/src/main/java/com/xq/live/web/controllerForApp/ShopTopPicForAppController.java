package com.xq.live.web.controllerForApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.ShopTopPic;
import com.xq.live.service.ShopTopPicService;
import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 商家顶部图片接口
 * Created by lipeng on 2018/7/3.
 */
@RestController
@RequestMapping(value = "/app/shopTopPic")
public class ShopTopPicForAppController {
    @Autowired
    private ShopTopPicService shopTopPicService;


    /**
     * 通过shopId查询商家的顶部图片
     * @param shopId
     * @return
     */
    @RequestMapping(value = "/selectPic",method = RequestMethod.GET)
    public BaseResp<List<Triplet<String, String,Long>>> selectByShopId(Long shopId){
        List<Triplet<String, String,Long>> pairs = shopTopPicService.selectByShopId(shopId);
        return new BaseResp<List<Triplet<String, String,Long>>>(ResultStatus.SUCCESS,pairs);
    }

    /**
     * 新增顶部图片
     * @param shopTopPic
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<Long> add(@Valid ShopTopPic shopTopPic, BindingResult result){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        Long id = shopTopPicService.add(shopTopPic);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 物理删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public BaseResp<Integer> delete(Long id){

        Integer i = shopTopPicService.delete(id);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, i);
    }
}
