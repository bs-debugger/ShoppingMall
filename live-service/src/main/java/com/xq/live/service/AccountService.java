package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.AccountLog;
import com.xq.live.model.UserAccount;
import com.xq.live.vo.in.AccountLogInVo;
import com.xq.live.vo.in.SecurityCodeInVo;
import com.xq.live.vo.in.SmsSendInVo;
import com.xq.live.vo.in.UserAccountInVo;
import com.xq.live.vo.out.AccountLogOut;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * com.xq.live.service
 *  用户账户service
 * @author zhangpeng32
 * Created on 2018/5/5 下午4:27
 * @Description:
 */
public interface AccountService {
    /**
     * 更新用户账户
     * @param userAccount
     * @return
     */
    Integer update(UserAccountInVo userAccount);

    /**
     * 账户收入
     * @param inVo
     * @param remark  备注
     * @return
     */
    Integer income(UserAccountInVo inVo, String remark);


    /**
     * 账户支出
     * @param inVo
     * @return
     */
    Integer payout(UserAccountInVo inVo, String remark);

    /**
     * 账户金币收入
     * @param inVo
     * @param remark  备注
     * @return
     */
    Integer incomegold(UserAccountInVo inVo, String remark);


    /**
     * 账户金币支出
     * @param inVo
     * @return
     */
    Integer payoutgold(UserAccountInVo inVo ,Integer gold, String remark);


    /**
     * 根据用户id查询账户信息
     * @param userId
     * @return
     */
    UserAccount findAccountByUserId(Long userId);

    /**
     * 分页查询用户交易流水列表
     * @param inVo
     * @return
     */
    public Pager<AccountLogOut> findAccountLogs(AccountLogInVo inVo);

    /**
     * 根据用户id查询账户余额
     * @param userId
     * @return
     */
    public BigDecimal balance(Long userId);


    /**
     * 根据用户id查询账户金币
     * @param userId
     * @return
     */
    public Integer findGoldByUserId(Long userId);

    /**
     * 根据shopid和需要改变的两个人的userid修改两个人的信息
     * type=1是管理员和核销员互换，type=2是管理员变普通用户，核销员变管理员，type=3是管理员变普通用户，普通用户边管理员
     * @param userId
     * @param thuserid
     * @param type
     * @return
     */
    public Integer changeUserByshop(Long userId, Long thuserid,Integer type);

    /**
     * 账户收入
     * @param inVo
     * @param remark  备注
     * @return
     */
    Integer updateIncome(UserAccountInVo inVo,String remark, Long cashApplyId,Long orderId);

    Integer updateIncomeV1(UserAccountInVo inVo, AccountLog accountLogInVo);

    /**
     * 账户支出
     * @param inVo
     * @return
     */
    Integer updatePayout(UserAccountInVo inVo,String remark,Long cashApplyId,Long orderId);


    Integer updatePayoutV1(UserAccountInVo inVo, AccountLog accountLogInVo);

    /**
     * 修改账户用户提现绑定银行卡信息
     * @param msg
     * @param inVo
     * @return
     */
    Integer updatePayBinding(String msg,UserAccountInVo inVo);

    /**
     * 新增商户账号信息后  加钱逻辑修改
     * @param inVo
     * @param accountLogInVo
     * @return
     */
    Integer updateIncomeV2(UserAccountInVo inVo, AccountLog accountLogInVo);

    /**
     * 新增商户账号信息后  扣钱逻辑修改
     * @param inVo
     * @param accountLogInVo
     * @return
     */
    public Integer updatePayoutV2(UserAccountInVo inVo,AccountLog accountLogInVo);

    /**
     * 新增安全密码
     * @param vo
     * @return
     */
    public Integer addOrUpdateSecurityCode(SecurityCodeInVo vo);

    /**
     * 编辑安全密码
     * @param vo
     * @return
     */
    public Integer forgetSecurityCode(SecurityCodeInVo vo);


    /**
     * 分页查询商户交易流水列表
     * @param inVo
     * @return
     */
    public Pager<AccountLogOut> findAccountLogsForShop(AccountLogInVo inVo);

    /**
     * 通过用户ID查询店铺账号
     * @param userId
     * @return
     */
    public UserAccount findAdminAccountByUserId(Long userId);
}
