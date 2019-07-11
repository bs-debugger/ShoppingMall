package com.xq.live.web.controllerForWeb;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.ShopZone;
import com.xq.live.service.ShopZoneService;
import com.xq.live.vo.in.ShopZoneInVo;
import com.xq.live.vo.in.ShopZoneItemInVo;
import com.xq.live.vo.out.ShopZoneItemOut;
import com.xq.live.vo.out.ShopZoneOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 专区相关接口
 * Created by ss on 2019/1/7.
 */
@RestController
@RequestMapping(value = "/webApprove/shopZone")
public class ShopZoneForWebController {
    @Autowired
    private ShopZoneService shopZoneService;

    /**
     * 分页查询大专区里面的详细列表
     *
     * 入参:page,rows,city,regionName
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/listItem",method = RequestMethod.POST)
    public BaseResp<Pager<ShopZoneItemOut>> listItem(@RequestBody ShopZoneItemInVo inVo){
        Pager<ShopZoneItemOut> list = shopZoneService.listItem(inVo);
        return new BaseResp<Pager<ShopZoneItemOut>>(ResultStatus.SUCCESS,list);
    }

    /**
     * 分页查询专区的大分区列表
     *
     * 入参:city,page,rows
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/listShopZone",method = RequestMethod.GET)
    public BaseResp<Pager<ShopZone>> listShopZone(ShopZoneInVo inVo){
        Pager<ShopZone> list = shopZoneService.list(inVo);
        return new BaseResp<Pager<ShopZone>>(ResultStatus.SUCCESS,list);
    }
    /**
     * 查询所有大小分区列表
     *
     * 入参:city,page,rows
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/listAllShopZone",method = RequestMethod.GET)
    public BaseResp<List<ShopZoneOut>> listAllShopZone(ShopZoneInVo inVo){
        List<ShopZoneOut>  list = shopZoneService.listAllShopZone(inVo);
        return new BaseResp<List<ShopZoneOut>>(ResultStatus.SUCCESS,list);
    }

}
