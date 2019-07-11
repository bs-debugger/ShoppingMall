package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.RandomNumberUtil;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.*;
import com.xq.live.service.AccountService;
import com.xq.live.service.CouponService;
import com.xq.live.service.ShopCashierService;
import com.xq.live.vo.in.AccountLogInVo;
import com.xq.live.vo.in.CouponInVo;
import com.xq.live.vo.in.UserAccountInVo;
import com.xq.live.vo.out.AccountLogOut;
import com.xq.live.web.utils.MathRandom;
import com.xq.live.web.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * com.xq.live.web.controller
 *
 * @author zhangpeng32
 * Created on 2018/5/5 下午3:35
 * @Description:
 */
@Api(tags = "账户服务-AccoutController")
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private CouponService couponService;

    @Autowired
    private ShopCashierService shopCashierService;

    /**
     * 根据用户id查询账户信息,存在需要查看别的userId的情况(核销员看管理员账户的钱)
     * @param userId
     * @return
     */
    @ApiOperation(value = "获取账户信息",notes = "根据用户id获取用户的账户信息")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true)
    @RequestMapping(value = "/getByUserId", method = RequestMethod.GET)
    //@ApiResponses({@ApiResponse(code = 500, message = "服务器内部错误", response = ApiError.class)})
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
    @ApiOperation(value = "查询用户交易流水",notes = "注意:operateType  1支出 2收入\n" +
            "   type: 1，商户余额 ，2 金币，3,用户余额,4，用户已获得的奖励金，5，用户审核中的奖励金，6，用户获取失败的奖励金")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true)
    @RequestMapping(value = "/listTrading", method = RequestMethod.GET)
    public BaseResp<?> listTrading(AccountLogInVo inVo){
        if(inVo ==  null || inVo.getUserId() == null){
            return new BaseResp<Pager<AccountLogOut>>(ResultStatus.error_para_user_empty);
        }

        Pager<AccountLogOut> res = accountService.findAccountLogs(inVo);
        return new BaseResp<Pager<AccountLogOut>>(ResultStatus.SUCCESS, res);
    }


    /**
     * 账户金币查询
     * @param userId
     * @return
     */
    @ApiOperation(value = "账户金币查询",notes = "账户金币查询")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true)
    @RequestMapping(value = "/findGold", method = RequestMethod.GET)
    public BaseResp<Integer> findGoldByUserId(Long userId){
        if(userId ==  null){
            return new BaseResp<Integer>(ResultStatus.error_input_user_id);
        }
        Integer result = accountService.findGoldByUserId(userId);
        if (result==null){
            return new BaseResp<Integer>(ResultStatus.error_para_user_empty);
        }
        return new BaseResp<Integer>(ResultStatus.SUCCESS, result);
    }

    /**
     * 修改账户用户提现绑定银行卡信息
     * @param msg
     * @param inVo
     * @return
     */
    @ApiOperation(value = "修改账户用户提现绑定银行卡信息",notes = "修改账户用户提现绑定银行卡信息")
    @ApiImplicitParams({
    @ApiImplicitParam(name = "id", value = "账户id", required = true),
    @ApiImplicitParam(name = "accountBankCard", value = "用户提现账户", required = false),
    @ApiImplicitParam(name = "bankCardName", value = "银行卡银行(所属银行 用户用)", required = false),
    @ApiImplicitParam(name = "bankCardPhone", value = "银行卡绑定手机号(用户用）", required = false),
    @ApiImplicitParam(name = "accountIdentityCard", value = "身份证号", required = false),
    @ApiImplicitParam(name = "accountCardholderName", value = "持卡人姓名 (用户用)", required = false),
    @ApiImplicitParam(name = "msg", value = "验证码", required = true)
    })
    @RequestMapping(value = "/updatePayBinding", method = RequestMethod.POST)
    public BaseResp<Integer> updatePayBinding(String msg,UserAccountInVo inVo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        if(msg ==  null){
            return new BaseResp<Integer>(ResultStatus.error_input_user_id);
        }
        Integer result = accountService.updatePayBinding(msg,inVo);
        if (result==null){
            return new BaseResp<Integer>(ResultStatus.error_smsend_pay_binding);
        }
        return new BaseResp<Integer>(ResultStatus.SUCCESS, result);
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
     * 核销券领取随机金币
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/ingold", method = RequestMethod.POST)
    public BaseResp<Integer> ingold(CouponInVo inVo){
        if(inVo == null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        if(inVo.getUserId() ==  null){
            return new BaseResp<Integer>(ResultStatus.error_parm_user_id_empty);
        }
        if(inVo.getId() == null){
            return new BaseResp<Integer>(ResultStatus.error_parm_coupon_id_empty);
        }
        Coupon coupon = couponService.get(inVo.getId());
        if(coupon == null){ //券不存在
            return new BaseResp<Integer>(ResultStatus.error_coupon_null);
        }

        if(inVo.getUserId().compareTo(coupon.getUserId()) != 0){    //券不是该用户的，无法领取红包
            return new BaseResp<Integer>(ResultStatus.error_coupon_user_id);
        }
        //券未核销，不能领取金币
        if(coupon.getIsUsed() != Coupon.COUPON_IS_USED_YES){
            return new BaseResp<Integer>(ResultStatus.error_receive_red_gold_fail);
        }

        UserAccountInVo accountInVo = new UserAccountInVo();
        accountInVo.setUserId(inVo.getUserId());
        accountInVo.setOccurGold(MathRandom.PercentageRandom());

        Integer re = accountService.incomegold(accountInVo,"使用优惠券");
        if(re > 0){
            return new BaseResp<Integer>(ResultStatus.SUCCESS, accountInVo.getOccurGold());
        }
        return new BaseResp<Integer>(ResultStatus.error_receive_red_gold_fail);
    }

    /**
     * 获取当前登录钱包信息
     * @param
     * @return
     */
    @RequestMapping(value = "/getUserAccount", method = RequestMethod.POST)
    public BaseResp<UserAccount> getUserAccount(){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<UserAccount>(ResultStatus.error_param_empty);
        }
        UserAccount account= accountService.findAccountByUserId(user.getId());
        return new BaseResp<UserAccount>(ResultStatus.SUCCESS,account);
    }

    /**
     * 查询当前登陆用户用户交易流水
     * 注意:operateType  1支出 2收入
     * type-1 1，商户余额 ，2 金币，3,用户余额,4，用户已获得的奖励金，5，用户审核中的奖励金，6，用户获取失败的奖励金
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/listUserTrading", method = RequestMethod.GET)
    public BaseResp<?> listUserTrading(AccountLogInVo inVo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<UserAccount>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        Pager<AccountLogOut> res = accountService.findAccountLogs(inVo);
        return new BaseResp<Pager<AccountLogOut>>(ResultStatus.SUCCESS, res);
    }

    /**
     * 更新用户银行信息
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/updateBank", method = RequestMethod.POST)
    public BaseResp<Integer> updateBank(UserAccountInVo inVo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        if(inVo.getCardBankname()==null||inVo.getAccountCardholder()==null||inVo.getAccountName()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        Integer re = accountService.update(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, re);
    }


    /**
     * 给商家增加余额
     * @param userAccountInVoList 商家id和金额的列表[{"shopId":35,"occurAmount":13.5}]
     * @param remark 备注(展示在账户日志中的备注字段)
     * @param type 类型(0 增加余额，1 扣除余额)
     * @return
     */
    @RequestMapping(value = "/addShopAccountForFree", method = RequestMethod.POST)
    public BaseResp<List<Map<String,String>>> addShopAccountForFree(@RequestBody List<UserAccountInVo> userAccountInVoList, String remark, Integer type){
        if(userAccountInVoList==null||userAccountInVoList.size()<1||remark==null||type==null){
            return new BaseResp<List<Map<String,String>>>(ResultStatus.error_param_empty);
        }
        List<Map<String,String>> resultList=new ArrayList<>();
        for(UserAccountInVo userAccountInVo:userAccountInVoList){
            Map<String,String> result=new HashMap<>();
            ShopCashier shopCashier=shopCashierService.adminByShopId(userAccountInVo.getShopId());
            if(shopCashier!=null&&shopCashier.getCashierId()!=null){
                //1. 账户日志
                UserAccountInVo accountInVo = new UserAccountInVo();
                accountInVo.setUserId(shopCashier.getCashierId());
                accountInVo.setOccurAmount(userAccountInVo.getOccurAmount());
                accountInVo.setType(AccountLog.TYPE_SHOP);
                AccountLog accountLogInVo = new AccountLog();
                accountLogInVo.setRemark(remark);
                Integer re=0;
                if(type==0){
                     re= accountService.updateIncomeV2(accountInVo, accountLogInVo);
                }else if(type==1){
                     re= accountService.updatePayoutV2(accountInVo, accountLogInVo);
                }
                if(re>0){
                    result.put("增加成功",userAccountInVo.getShopId().toString()+"增加了"+userAccountInVo.getOccurAmount());
                }else{
                    result.put("增加失败",userAccountInVo.getShopId().toString()+"管理员在系统中未找到对应用户");
                }
            }else{
                result.put("增加失败",userAccountInVo.getShopId().toString()+"未找到管理员");
            }
            resultList.add(result);
        }
        return new BaseResp<List<Map<String,String>>>(ResultStatus.SUCCESS,resultList);
    }


}
