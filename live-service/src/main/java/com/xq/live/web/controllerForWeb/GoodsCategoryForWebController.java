package com.xq.live.web.controllerForWeb;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.GoodsCategory;
import com.xq.live.service.GoodsCategoryService;
import com.xq.live.vo.out.ShopGoodsCategoryConfigOut;
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
@RequestMapping("/webApprove/goodsCategory")
public class GoodsCategoryForWebController {
    @Autowired
    private GoodsCategoryService goodsCategoryService;

    /**
     * 查询所有商品的类目(用缓存)
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public BaseResp<List<GoodsCategory>>  list(){
        List<GoodsCategory> list = goodsCategoryService.list();
        return new BaseResp<List<GoodsCategory>>(ResultStatus.SUCCESS,list);
    }

    /**
     * 查询某个一级类目下面的所有子类目
     * @return
     */
    @RequestMapping(value = "/listByParentId",method = RequestMethod.GET)
    public BaseResp<List<GoodsCategory>>  listByParentId(Long parentId){
        List<GoodsCategory> list = goodsCategoryService.list(parentId);
        return new BaseResp<List<GoodsCategory>>(ResultStatus.SUCCESS,list);
    }

    /**
     * 通过经营品类的一级id，去查询商品的类目配置
     * @param shopCategoryId
     * @return
     */
    @RequestMapping(value = "/selectGoodsCategoryByShopCategoryId" ,method = RequestMethod.GET)
    public BaseResp<List<ShopGoodsCategoryConfigOut>> selectGoodsCategoryByShopCategoryId(Long shopCategoryId){
        List<ShopGoodsCategoryConfigOut> list = goodsCategoryService.selectGoodsCategoryByShopCategoryId(shopCategoryId);
        return new BaseResp<List<ShopGoodsCategoryConfigOut>>(ResultStatus.SUCCESS,list);
    }

}
