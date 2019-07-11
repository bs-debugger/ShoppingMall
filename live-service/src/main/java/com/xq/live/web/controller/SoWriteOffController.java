package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.config.WeiXinTeamplateMsg;
import com.xq.live.model.*;
import com.xq.live.service.*;
import com.xq.live.vo.in.SoWriteOffInVo;
import com.xq.live.vo.out.SoOut;
import com.xq.live.vo.out.SoWriteOffOut;

import com.xq.live.web.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-21 18:18
 * @copyright:hbxq
 **/
@RestController
@RequestMapping(value = "/hx")
public class SoWriteOffController {

    @Autowired
    private SoWriteOffService soWriteOffService;

    @Autowired
    private UserService userService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private SoService soService;

    @Autowired
    private WeiXinPushService weiXinPushService;

    @Autowired
    private MessageService messageService;

    /**
     * 根据id查询记录
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BaseResp<SoWriteOff> get(@PathVariable("id") Long id) {
        SoWriteOff soWriteOff = soWriteOffService.get(id);
        return new BaseResp<SoWriteOff>(ResultStatus.SUCCESS, soWriteOff);
    }

    /**
     * 注:cashierId和cashierName是当前用户的userId和userName，要从网关中取
     * @param soWriteOff
     * @param result
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<Long> add(@Valid SoWriteOff soWriteOff, BindingResult result) throws Exception{
        if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }

        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        soWriteOff.setCashierId(user.getId());
        soWriteOff.setCashierName(user.getUserName());
        //1、参数校验--验证券是否被核销过
        Coupon coupon = couponService.get(soWriteOff.getCouponId());
        if(coupon == null || coupon.getIsUsed() == Coupon.COUPON_IS_USED_YES){
            return new BaseResp<Long>(ResultStatus.error_coupon_is_used);
        }

        //退款申请中和已退款的票卷不能核销
        if(coupon.getStatus()!=null&&(coupon.getStatus()==Coupon.STATUS_REFUND||coupon.getStatus()==Coupon.STATUS_REFUND_APPLICATION)){
            return new BaseResp<Long>(ResultStatus.error_coupon_refund);
        }

        //验证扫码人id
        User cashier = userService.getUserById(soWriteOff.getCashierId());
        if(cashier == null){
            return new BaseResp<Long>(ResultStatus.error_para_cashier_id);
        }

        //验证账号类型：商家账号
        if(cashier.getUserType() != User.USER_TYPE_SJ){
            return new BaseResp<Long>(ResultStatus.error_para_cashier_user_type);
        }

        //验证商家信息
        if(cashier.getShopId() == null){
            return new BaseResp<Long>(ResultStatus.error_para_user_shop_id);
        }
        Shop shop = shopService.getShopByUserId(cashier.getId());
        if(shop ==  null){
            return new BaseResp<Long>(ResultStatus.error_shop_info_empty);
        }

        SoOut soOut = soService.get(soWriteOff.getSoId());
        //判断核销的券所属于的订单是否为食典券订单，砍价券订单，抢购券订单。
        //如果未上述的订单，则要判断订单所属的商家是否为该核销商家，如果不是则不让核销
        //但是现在有个新的判断方法，判断该票券所在shopId是否是核销的商家，如果不是则不让核销
        if(soOut!=null) {
            if (soOut.getSoType() == So.SO_TYPE_PT && soOut.getShopId() != null) {
                if (!soWriteOff.getShopId().equals(soOut.getShopId())) {
                    return new BaseResp<Long>(ResultStatus.error_act_shop_not_right);
                }
            }
        }else{
            return new BaseResp<Long>(ResultStatus.error_para_coupon_code_empty);
        }

        soWriteOff.setShopId(shop.getId());
        soWriteOff.setShopName(shop.getShopName());
        soWriteOff.setPaidAmount(soWriteOff.getShopAmount().subtract(soWriteOff.getCouponAmount()));
        //2、核销抵用券
        Long id = soWriteOffService.add(soWriteOff);

        //核销完成之后推送核销完成信息到微信
        String shopName=shop.getShopName();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date=new java.util.Date();
        String str=sdf.format(date);
        User users = UserContext.getUserSession();
        StringBuilder sb = new StringBuilder(user.getMobile());
        sb.replace(3, 7, "****");
        String keyWords=coupon.getSkuName()+","+str+","+shopName+","+sb+",您的订单已核销";//卡卷名称+核销时间+门店+店员昵称+备注
        Integer res =weiXinPushService.pushByUserId(WeiXinTeamplateMsg.templateId_TYPE_HX, "",keyWords,coupon.getUserId() );

        //核销完成之后发送消息到小程序的消息列表
        messageService.addMessage("核销成功", "您的 "+coupon.getSkuName()+" 已核销。", MessageText.MESSAGE_TEXT_TYPE_PRIVATE, coupon.getUserId(), user.getId());

        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 查询每个商家核销的票卷的信息
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public BaseResp<Pager<SoWriteOffOut>> list(SoWriteOffInVo inVo){
        if(inVo==null||inVo.getShopId()==null||inVo.getBegainTime()==null||inVo.getEndTime()==null||inVo.getIsBill()==null){
            return new BaseResp<Pager<SoWriteOffOut>>(ResultStatus.error_param_empty);
        }
        Pager<SoWriteOffOut> list = soWriteOffService.list(inVo);
        return new BaseResp<Pager<SoWriteOffOut>>(ResultStatus.SUCCESS,list);
    }



}
