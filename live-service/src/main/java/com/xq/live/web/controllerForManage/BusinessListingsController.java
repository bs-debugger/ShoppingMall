package com.xq.live.web.controllerForManage;

import com.github.pagehelper.PageInfo;
import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.service.BusinessListingsService;
import com.xq.live.vo.in.BusinessListingsInVo;
import com.xq.live.vo.out.BusinessListingsOut;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "商家列表-BusinessListingsController")
@RestController
@RequestMapping("manage/BusinessListings")
public class BusinessListingsController {

    @Autowired
    private BusinessListingsService businessListingsService;

    /**
     * 商家列表信息
     * */
    @ApiOperation(value = "获取商家列表")
    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    public BaseResp<PageInfo<BusinessListingsOut>> getList(@RequestBody BusinessListingsInVo businessListingsInVo){
        return new BaseResp<>(ResultStatus.SUCCESS, businessListingsService.getList(businessListingsInVo));
    }

   /**
    * 店铺所有商品
    * */
   @ApiOperation(value = "店铺所有商品")
   @RequestMapping(value = "/getShopList", method = RequestMethod.POST)
   public BaseResp<Pager<BusinessListingsOut>> getShopList(@RequestBody BusinessListingsInVo businessListingsInVo){
       return new BaseResp<>(ResultStatus.SUCCESS, businessListingsService.getShopList(businessListingsInVo));
   }

   /**
     * 编辑商家信息
     * */
   @ApiOperation(value = "编辑商家信息")
   @RequestMapping(value = "/updateBusinesses", method = RequestMethod.POST)
   public BaseResp updateBusinesses(@RequestBody BusinessListingsInVo businessListingsInVo){
       return businessListingsService.updateBusinesses(businessListingsInVo);
   }

   /**
    * 增删核销员
    * */
   @ApiOperation(value = "编辑商家信息")
   @RequestMapping(value = "/updateClerk", method = RequestMethod.POST)
   public BaseResp updateClerk(@RequestBody BusinessListingsInVo businessListingsInVo){
       return businessListingsService.updateClerk(businessListingsInVo);
   }

    /**
     * 增删店铺主图
     * */
    @ApiOperation(value = "编辑商家信息")
    @RequestMapping(value = "/updateShopOwnerMap", method = RequestMethod.POST)
    public BaseResp updateShopOwnerMap(@RequestBody BusinessListingsInVo businessListingsInVo){
        return businessListingsService.updateShopOwnerMap(businessListingsInVo);
    }

   /**
    * 编辑商品信息
    * */
   @ApiOperation(value = "编辑商品信息")
   @RequestMapping(value = "/updateGoods", method = RequestMethod.POST)
   public BaseResp update(@RequestBody BusinessListingsInVo businessListingsInVo){
       return businessListingsService.updateGoods(businessListingsInVo);
   }

    /**
     * 删除商家信息
     * */
    @ApiOperation(value = "删除商家信息")
    @RequestMapping(value = "/deleteBusinesses", method = RequestMethod.POST)
    public BaseResp deleteBusinesses(@RequestBody BusinessListingsInVo businessListingsInVo){
        return businessListingsService.deleteBusinesses(businessListingsInVo);
    }

}
