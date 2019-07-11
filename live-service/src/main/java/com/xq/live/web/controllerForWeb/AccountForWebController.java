package com.xq.live.web.controllerForWeb;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.User;
import com.xq.live.model.UserAccount;
import com.xq.live.service.AccountService;
import com.xq.live.service.CouponService;
import com.xq.live.vo.in.AccountLogInVo;
import com.xq.live.vo.in.UserAccountInVo;
import com.xq.live.vo.out.AccountLogOut;
import com.xq.live.web.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * com.xq.live.web.controller
 *
 * @author zhangpeng32
 * Created on 2018/5/5 下午3:35
 * @Description:
 */
@Api(tags = "账户服务-AccountForWebController")
@RestController
@RequestMapping("/website/account")
public class AccountForWebController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private CouponService couponService;

    /**
     * 根据用户id查询账户信息,存在需要查看别的userId的情况(核销员看管理员账户的钱)
     * @param userId
     * @return
     */
    @ApiOperation(value = "获取账户信息",notes = "根据用户id获取用户的账户信息")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true)
    @RequestMapping(value = "/approve/getByUserId", method = RequestMethod.GET)
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
    @RequestMapping(value = "/approve/listTrading", method = RequestMethod.GET)
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
    @RequestMapping(value = "/approve/findGold", method = RequestMethod.GET)
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
     * 账户余额查询
     * @param userId
     * @return
     */
    @RequestMapping(value = "/approve/balance", method = RequestMethod.GET)
    public BaseResp<BigDecimal> balance(Long userId){
        if(userId ==  null){
            return new BaseResp<BigDecimal>(ResultStatus.error_input_user_id);
        }

        BigDecimal result = accountService.balance(userId);
        return new BaseResp<BigDecimal>(ResultStatus.SUCCESS, result);
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



}
