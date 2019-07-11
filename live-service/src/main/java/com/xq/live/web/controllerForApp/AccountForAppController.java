package com.xq.live.web.controllerForApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.RandomNumberUtil;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.Coupon;
import com.xq.live.model.User;
import com.xq.live.model.UserAccount;
import com.xq.live.service.AccountService;
import com.xq.live.service.CouponService;
import com.xq.live.vo.in.AccountLogInVo;
import com.xq.live.vo.in.CouponInVo;
import com.xq.live.vo.in.SecurityCodeInVo;
import com.xq.live.vo.in.UserAccountInVo;
import com.xq.live.vo.out.AccountLogOut;
import com.xq.live.web.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * com.xq.live.web.controller
 *
 * @author lipeng
 * Created on 2018/5/5 下午3:35
 * @Description:
 */
@RestController
@RequestMapping("app/account")
public class AccountForAppController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private CouponService couponService;

    /**
     * 根据用户id查询账户信息
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getByUserId", method = RequestMethod.GET)
    public BaseResp<UserAccount> getByUserId(Long userId){
        if(userId ==  null){
            return new BaseResp<UserAccount>(ResultStatus.error_input_user_id);
        }

        UserAccount result = accountService.findAccountByUserId(userId);
        return new BaseResp<UserAccount>(ResultStatus.SUCCESS, result);
    }

    /**
     * 查询用户交易流水
     * 注意:operateType  1支出 2收入
     * type-1 1，商户余额 ，2 金币，3,用户余额,4，用户已获得的奖励金，5，用户审核中的奖励金，6，用户获取失败的奖励金
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/listTrading", method = RequestMethod.GET)
    public BaseResp<?> listTrading(AccountLogInVo inVo){
        if(inVo ==  null || inVo.getUserId() == null){
            return new BaseResp<Pager<AccountLogOut>>(ResultStatus.error_para_user_empty);
        }

        Pager<AccountLogOut> res = accountService.findAccountLogsForShop(inVo);
        return new BaseResp<Pager<AccountLogOut>>(ResultStatus.SUCCESS, res);
    }

    /**
     * 账户余额查询
     * @param userId
     * @return
     */
    @RequestMapping(value = "/balance", method = RequestMethod.GET)
    public BaseResp<BigDecimal> balance(Long userId){
        if(userId ==  null){
            return new BaseResp<BigDecimal>(ResultStatus.error_input_user_id);
        }

        BigDecimal result = accountService.balance(userId);
        return new BaseResp<BigDecimal>(ResultStatus.SUCCESS, result);
    }

    /**
     * 核销券领取随机红包
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/redPacket", method = RequestMethod.POST)
    public BaseResp<BigDecimal> randomMoney(CouponInVo inVo){
        if(inVo == null){
            return new BaseResp<BigDecimal>(ResultStatus.error_param_empty);
        }
        if(inVo.getUserId() ==  null){
            return new BaseResp<BigDecimal>(ResultStatus.error_parm_user_id_empty);
        }
        if(inVo.getId() == null){
            return new BaseResp<BigDecimal>(ResultStatus.error_parm_coupon_id_empty);
        }
        Coupon coupon = couponService.get(inVo.getId());
        if(coupon == null){ //券不存在
            return new BaseResp<BigDecimal>(ResultStatus.error_coupon_null);
        }

        if(inVo.getUserId().compareTo(coupon.getUserId()) != 0){    //券不是该用户的，无法领取红包
            return new BaseResp<BigDecimal>(ResultStatus.error_coupon_user_id);
        }
        //券未核销，不能领取红包
        if(coupon.getIsUsed() != Coupon.COUPON_IS_USED_YES){
            return new BaseResp<BigDecimal>(ResultStatus.error_coupon_is_not_used);
        }

        int max = 0;
        BigDecimal couponAmount = coupon.getCouponAmount();
        /**
         * 如果购买享七券支付的金额小于1元，则可领取红包金额按小于1元处理
         * couponAmount).multiply(BigDecimal.valueOf(0.1) = 面值 * 10% = 买券时候的付款金额
         * 如果是0元券，也可以领取1元以下的红包
         */
        if(BigDecimal.ONE.compareTo(couponAmount.multiply(BigDecimal.valueOf(0.1))) == 1){
            max = 1;
        }else{
            max = couponAmount.intValue()/10;
        }
        BigDecimal redPacketAmount = RandomNumberUtil.randomNumber(max, 10);
        UserAccountInVo accountInVo = new UserAccountInVo();
        accountInVo.setUserId(inVo.getUserId());
        accountInVo.setOccurAmount(redPacketAmount);
        int ret = accountService.income(accountInVo, "核销享七券，领取红包奖励");
        if(ret > 0){
            return new BaseResp<BigDecimal>(ResultStatus.SUCCESS, redPacketAmount);
        }
        return new BaseResp<BigDecimal>(ResultStatus.error_receive_red_packet_fail);
    }

    /**
     * 更新账户的基本信息
     * @param userAccount
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public BaseResp<Integer> update(UserAccountInVo userAccount){

        Integer re = accountService.update(userAccount);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, re);
    }

    /**
     * 根据shopid和需要改变的两个人的userid修改两个人的信息
     * 主要是余额修改和身份修改
     * @param admin
     * @param user
     * @param type
     * @return
     * type=1是管理员和核销员互换，type=2是管理员变普通用户，核销员变管理员，type=3是管理员变普通用户，普通用户变管理员
     * admin是之前管理员的userid，user是核销员或着用户的userid
     * app/account/changeUser?admin=&user=&type=
     */
    @RequestMapping(value = "/changeUser", method = RequestMethod.POST)
    public BaseResp<Integer> changeUserByshop(Long admin,Long user,Integer type){
        Integer ref = accountService.changeUserByshop(admin, user, type);
        if (ref <1){
            return new BaseResp<Integer>(ResultStatus.error_user_update_no);
        }
        return new BaseResp<Integer>(ResultStatus.SUCCESS, ref);
    }

    /**
     * 新增或者修改安全密码
     * @param vo
     * @return
     */
    @RequestMapping(value = "/addOrUpdateSecurityCode", method = RequestMethod.POST)
    public BaseResp<Integer> addOrUpdateSecurityCode(SecurityCodeInVo vo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        vo.setUserId(user.getId());
        if(vo.getType() == 0){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        Integer ref = accountService.addOrUpdateSecurityCode(vo);
        if (ref <1){
            return new BaseResp<Integer>(ResultStatus.error_user_update_no);
        }
        return new BaseResp<Integer>(ResultStatus.SUCCESS, ref);
    }

    /**
     * 忘记安全密码后重置安全密码
     * @param vo
     * @return
     */
    @RequestMapping(value = "/forgetSecurityCode", method = RequestMethod.POST)
    public BaseResp<Integer> forgetSecurityCode(SecurityCodeInVo vo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        vo.setUserId(user.getId());
        if(vo.getType() == 0){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        Integer ref = accountService.forgetSecurityCode(vo);
        if (ref ==-2){
            return new BaseResp<Integer>(ResultStatus.error_user_security_code);
        }else if(ref ==-1){
            return new BaseResp<Integer>(ResultStatus.error_user_update_no);
        }else if(ref ==-3){
            return new BaseResp<Integer>(ResultStatus.error_user_security_EXPIRE);
        }
        return new BaseResp<Integer>(ResultStatus.SUCCESS, ref);
    }

}
