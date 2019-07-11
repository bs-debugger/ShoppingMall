package com.xq.live.web.controllerForClientApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.ShopZone;
import com.xq.live.model.ShopZoneItem;
import com.xq.live.service.ShopZoneService;
import com.xq.live.vo.in.ShopZoneInVo;
import com.xq.live.vo.in.ShopZoneItemInVo;
import com.xq.live.vo.out.ShopZoneItemOut;
import com.xq.live.vo.out.ShopZoneOut;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 专区相关接口
 * Created by lipeng on 2019/1/4.
 */
@Api(tags = "专区相关服务-ShopZoneForController")
@RestController
@RequestMapping(value = "/clientApp/shopZone")
public class ShopZoneForClientAppController {
    @Autowired
    private ShopZoneService shopZoneService;

    /**
     * 分页查询专区列表
     *
     * 入参:city,page,rows
     * @param inVo
     * @return
     */
    @ApiOperation(value = "分页查询大专区列表",notes = "根据城市和分页参数查询大专区列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "city", value = "城市名字", required = true, dataType = "string"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "rows", value = "行数", required = true, dataType = "int")
    })
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public BaseResp<Pager<ShopZone>> list(ShopZoneInVo inVo){
        Pager<ShopZone> list = shopZoneService.list(inVo);
        return new BaseResp<Pager<ShopZone>>(ResultStatus.SUCCESS,list);
    }

    /**
     * 分页查询大专区里面的详细列表
     *
     * 入参:shopZoneId,page,rows,regionName
     * @param inVo
     * @return
     */
    @ApiOperation(value = "分页查询专区列表",notes = "根据大专区和分页参数查询大专区列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopZoneId", value = "大专区id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "rows", value = "行数", required = true, dataType = "int")
    })
    @RequestMapping(value = "/listItem",method = RequestMethod.POST)
    public BaseResp<Pager<ShopZoneItemOut>> listItem(ShopZoneItemInVo inVo){
        Pager<ShopZoneItemOut> list = shopZoneService.listItem(inVo);
        return new BaseResp<Pager<ShopZoneItemOut>>(ResultStatus.SUCCESS,list);
    }

    /**
     * 查询所有大小分区列表
     *
     * 入参:city
     * @param inVo
     * @return
     */
    @ApiOperation(value = "查询所有大小分区列表",notes = "根据城市查询所有大小分区列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "city", value = "城市名字", required = true, dataType = "string")
    })
    @RequestMapping(value = "/listAllShopZone",method = RequestMethod.GET)
    public BaseResp<List<ShopZoneOut>> listAllShopZone(ShopZoneInVo inVo){
        List<ShopZoneOut>  list = shopZoneService.listAllShopZone(inVo);
        return new BaseResp<List<ShopZoneOut>>(ResultStatus.SUCCESS,list);
    }

}
