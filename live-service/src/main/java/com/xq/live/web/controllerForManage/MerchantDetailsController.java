package com.xq.live.web.controllerForManage;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.dao.MerchantDetailsMapper;
import com.xq.live.service.MerchantDetailsService;
import com.xq.live.vo.in.MerchantDetailsInVo;
import com.xq.live.vo.in.UserAccountInVo;
import com.xq.live.vo.out.MerchantDetailsOut;
import com.xq.live.vo.out.ShopZoneOut;
import com.xq.live.web.utils.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Api(tags = "商户明细-RefundApplicationController")
@RestController
@RequestMapping("manage/detailed")
public class MerchantDetailsController {

    @Autowired
    private MerchantDetailsService merchantDetailsService;

    @Autowired
    private MerchantDetailsMapper merchantDetailsMapper;
    /**
     * 获取商户明细列表
     * @param merchantDetailsInVo
     * @return
     */
    @ApiOperation(value = "获取商户明细列表")
    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    public BaseResp<Pager<MerchantDetailsOut>> getList(@RequestBody MerchantDetailsInVo merchantDetailsInVo){
        return new BaseResp<>(ResultStatus.SUCCESS, merchantDetailsService.getList(merchantDetailsInVo));
    }

    /**
     * 获取商户明细头部信息
     * @param
     * @return
     */
     @ApiOperation(value = "获取商户明细头部信息")
     @RequestMapping(value = "/getTableList", method = RequestMethod.POST)
     public BaseResp getTableList(@RequestBody MerchantDetailsInVo merchantDetailsInVo){
         return merchantDetailsService.getTableList(merchantDetailsInVo);
     }

     /**
     * 获取商户明细详情信息
     * */
    @ApiOperation(value = "获取商户明细详情")
    @RequestMapping(value = "/getDetails", method = RequestMethod.POST)
    public BaseResp<Pager<MerchantDetailsOut>> getDetails(@RequestBody MerchantDetailsInVo merchantDetailsInVo){
        return new BaseResp<>(ResultStatus.SUCCESS, merchantDetailsService.getDetails(merchantDetailsInVo));
    }

    /**
     * 获取商户明细非订单详情信息
     * */
    @ApiOperation(value = "获取商户明细非订单详情")
    @RequestMapping(value = "/getNoDetails", method = RequestMethod.POST)
    public BaseResp<Pager<MerchantDetailsOut>> getNoDetails(@RequestBody MerchantDetailsInVo merchantDetailsInVo){
        return new BaseResp<>(ResultStatus.SUCCESS, merchantDetailsService.getNoDetails(merchantDetailsInVo));
    }

    /**
     * 商户明细导出
     * */
    @ApiOperation(value = "商户明细导出")
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void export(MerchantDetailsInVo merchantDetailsInVo, HttpServletResponse response){
        //文件名格式化用
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String format = df.format(new Date());
        merchantDetailsInVo.setRows(Integer.MAX_VALUE);
        //导出操作
        FileUtil.exportExcel(merchantDetailsMapper.getList(merchantDetailsInVo),"商户明细列表","商户明细列表", MerchantDetailsOut.class,"商户明细列表"+format+".xls",response);
    }

    /**
     * 获取查询条件专区信息
     *
     */
    @ApiOperation(value = "获取专区信息")
    @RequestMapping(value = "/listAllShopZone",method = RequestMethod.GET)
    public BaseResp<List<ShopZoneOut>> listAllShopZone(){
        List<ShopZoneOut>  list = merchantDetailsService.listAllShopZone();
        return new BaseResp<>(ResultStatus.SUCCESS, list);
    }

    /**
     * 商户余额补贴
     * */
    @ApiOperation(value = "商户余额补贴")
    @RequestMapping(value = "/updateBusinesses", method = RequestMethod.POST)
    public BaseResp getTableList(@RequestBody UserAccountInVo userAccountInVo){
        return merchantDetailsService.updateBusinesses(userAccountInVo);
    }
}
