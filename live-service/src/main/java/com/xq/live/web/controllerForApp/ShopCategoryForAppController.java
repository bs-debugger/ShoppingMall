package com.xq.live.web.controllerForApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.ShopCategory;
import com.xq.live.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * 商城系统类目接口
 * Created by lipeng on 2018/11/1.
 */
@RestController
@RequestMapping("/app/shopCategory")
public class ShopCategoryForAppController {
    @Autowired
    private ShopCategoryService shopCategoryService;

    /**
     * 查询商家入驻的所有经营品类
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public BaseResp<List<ShopCategory>>  list(){
        List<ShopCategory> list = shopCategoryService.list();
        return new BaseResp<List<ShopCategory>>(ResultStatus.SUCCESS,list);
    }

}
