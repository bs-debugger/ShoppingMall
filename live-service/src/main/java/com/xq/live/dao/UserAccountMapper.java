package com.xq.live.dao;

import com.xq.live.model.UserAccount;
import com.xq.live.vo.in.UserAccountInVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserAccount record);

    int batchInsert(UserAccount record);

    int insertSelective(UserAccount record);

    UserAccount selectByPrimaryKey(Long id);

    /**
     * 根据用户id查询账户信息
     * @param userID
     * @return
     */
    UserAccount findAccountByUserId(Long userID);


    /**
     * 根据用户id查询账户信息(金币)
     * @param userID
     * @return
     */
    UserAccount findGoldByUserId(Long userID);


    /**
     * 根据参数修改表数据
     * @param inVo
     * @return
     */
    int updateByPrimaryKeySelective(UserAccountInVo inVo);

    /**
     * 根据参数修改表数据除去UserID
     * @param inVo
     * @return
     */
    int updateByUserID(UserAccountInVo inVo);

    int updateByPrimaryKey(UserAccount record);

    /*通过userid修改用户信息*/
    Integer updateByUserId(UserAccount record);

    /**
     * 账户收入
     * @param inVo
     * @return
     */
    int income(UserAccountInVo inVo);

    /**
     * 账户支出
     * @param inVo
     * @return
     */
    int payout(UserAccountInVo inVo);

    /**
     * 根据UserID更改当前用户的金币余额信息
     * @param record
     * @return
     */
    int goldByUserId(UserAccount record);

    /**
     * 根据UserID更改当前用户的余额信息
     * @param inVo
     * @return
     */
    int updateByoccur(UserAccountInVo inVo);

    /**
     * 根据UserID和type增加当前用户的余额信息
     * @param inVo
     * @return
     */
    int updateIncome(UserAccountInVo inVo);

    /**
     * 根据UserID和type减少当前用户的余额信息
     * @param inVo
     * @return
     */
    int updatePayout(UserAccountInVo inVo);

    int batchUpdateByPrimaryKeySelective(UserAccount userAccount);

    /**
     * 修改账户用户提现绑定银行卡信息
     * @param inVo
     * @return
     */
    int updatePayBinding(UserAccountInVo inVo);

    /**
     * 通过店铺ID查询当前店铺的店铺账户
     * @param shopId
     * @return
     */
    UserAccount selectShopAccountByShopId(@Param("shopId")Long shopId);

    /**
     * 管理员账户支出
     * @param inVo
     * @return
     */
    int payoutForAdmin(UserAccountInVo inVo);

    /**
     * 通过店铺ID修改店铺的绑定的银行卡信息
     * @param shopId
     * @param accountName
     * @param cardBankname
     * @return
     */
    int updateBankDetailByShopId(@Param("shopId")Long shopId,@Param("accountName")String accountName,
                                 @Param("cardBankname")String cardBankname,@Param("accountCardholder")String accountCardholder);

    /**
     * 管理员账户收入
     * @param inVo
     * @return
     */
    int incomeForAdmin(UserAccountInVo inVo);

    /**
     * 更新账户安全密码
     * @param userAccount
     * @return
     */
    int updateSecurityCodeById(@Param("userAccount") UserAccount userAccount);

    /**
     * 修改账号绑定的店铺ID
     * @param userId
     * @param shopId
     * @return
     */
    int updateShopIdByUserId(@Param("userId") Long userId, @Param("shopId") Long shopId);

}
