package com.xq.live.model;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name="user_account")
public class UserAccount {

    /**
     * 用户账户类型 0 享七 1 微信 2 支付宝 3 银行卡
     */
    public final static int ACCOUNT_TYPE_XQ = 0;

    public final static int ACCOUNT_TYPE_WX = 1;

    public final static int ACCOUNT_TYPE_ZFB = 2;

    public final static int ACCOUNT_TYPE_BANK = 3;

    /**
     * 账户状态  1 正常 2 冻结
     */
    public final static int ACCOUNT_STATUS_ACTIVE = 1;

    public final static int ACCOUNT_STATUS_FROZEN = 2;

    @ApiModelProperty(value = "用户账户id")
    private Long id;    //用户账户主键

    @ApiModelProperty(value = "用户表ID")
    private Long userId;    //用户表ID

    @ApiModelProperty(value = "用户账号")
    private String userName;    //用户账号

    @ApiModelProperty(value = "支付账号 商家用")
    private String accountName;    //支付账号 商家用

    @ApiModelProperty(value = "账户类型 0 享七  1 微信  2支付宝")
    private Integer accountType;   //账户类型 0 享七  1 微信  2支付宝

    @ApiModelProperty(value = "账户余额,商家余额")
    private BigDecimal accountAmount;   //账户余额,商家余额

    @ApiModelProperty(value = "账户状态 1 正常 2 冻结")
    private Integer accountStatus; //账户状态 1 正常 2 冻结

    @ApiModelProperty(value = "是否删除 0 否 1 是")
    private Integer isDeleted;     //是否删除 0 否 1 是

    @ApiModelProperty(value = "创建时间")
    private Date createTime;    //创建时间

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;    //更新时间

    @ApiModelProperty(value = "版本号")
    private Integer versionNo;  //版本号

    @ApiModelProperty(value = "账户的持卡人姓名")
    private String accountCardholder;//账户的持卡人姓名

    @ApiModelProperty(value = "金币余额")
    private Integer gold;//金币余额

    @ApiModelProperty(value = "用户已获得的奖励金")
    private BigDecimal passedAmount;//用户已获得的奖励金

    @ApiModelProperty(value = "用户审核中的奖励金")
    private BigDecimal reviewAmount;//用户审核中的奖励金

    @ApiModelProperty(value = "用户获取失败的奖励金")
    private BigDecimal failAmount;//用户获取失败的奖励金

    @ApiModelProperty(value = "所属银行")
    private  String cardBankname;//所属银行

    @ApiModelProperty(value = "用户余额（小程序上的用户余额，和account_mount商家余额分开）")
    private  BigDecimal userAmount;//用户余额（小程序上的用户余额，和account_mount商家余额分开）

    @ApiModelProperty(value = "用户提现账户")
    private String accountBankCard;//用户提现账户
    @ApiModelProperty(value = "银行卡银行(所属银行 用户用)")
    private String bankCardName;//银行卡银行(所属银行 用户用)
    @ApiModelProperty(value = "银行卡绑定手机号(用户用）")
    private String bankCardPhone;//银行卡绑定手机号(用户用）
    @ApiModelProperty(value = "身份证号")
    private String accountIdentityCard;//身份证号
    @ApiModelProperty(value = "持卡人姓名 (用户用)")
    private String accountCardholderName;//持卡人姓名 (用户用)
    @ApiModelProperty(value = "店铺ID")
    private Long shopId;
    @ApiModelProperty(value = "用户账号类型")
    private Integer userAccountType;
    @ApiModelProperty(value = "安全密钥")
    private String securityCode;

    public UserAccount() {
        this.userAccountType=2;
    }

    public Integer getGold() {
        return gold;
    }

    public void setGold(Integer gold) {
        this.gold = gold;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName == null ? null : accountName.trim();
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(BigDecimal accountAmount) {
        this.accountAmount = accountAmount;
    }

    public Integer getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Integer accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }

    public String getAccountCardholder() {
        return accountCardholder;
    }

    public void setAccountCardholder(String accountCardholder) {
        this.accountCardholder = accountCardholder;
    }

    public BigDecimal getPassedAmount() {
        return passedAmount;
    }

    public void setPassedAmount(BigDecimal passedAmount) {
        this.passedAmount = passedAmount;
    }

    public BigDecimal getReviewAmount() {
        return reviewAmount;
    }

    public void setReviewAmount(BigDecimal reviewAmount) {
        this.reviewAmount = reviewAmount;
    }

    public BigDecimal getFailAmount() {
        return failAmount;
    }

    public void setFailAmount(BigDecimal failAmount) {
        this.failAmount = failAmount;
    }

    public String getCardBankname() {
        return cardBankname;
    }

    public void setCardBankname(String cardBankname) {
        this.cardBankname = cardBankname;
    }

    public BigDecimal getUserAmount() {
        return userAmount;
    }

    public void setUserAmount(BigDecimal userAmount) {
        this.userAmount = userAmount;
    }

    public String getAccountBankCard() {
        return accountBankCard;
    }

    public void setAccountBankCard(String accountBankCard) {
        this.accountBankCard = accountBankCard;
    }

    public String getBankCardName() {
        return bankCardName;
    }

    public void setBankCardName(String bankCardName) {
        this.bankCardName = bankCardName;
    }

    public String getBankCardPhone() {
        return bankCardPhone;
    }

    public void setBankCardPhone(String bankCardPhone) {
        this.bankCardPhone = bankCardPhone;
    }

    public String getAccountIdentityCard() {
        return accountIdentityCard;
    }

    public void setAccountIdentityCard(String accountIdentityCard) {
        this.accountIdentityCard = accountIdentityCard;
    }

    public String getAccountCardholderName() {
        return accountCardholderName;
    }

    public void setAccountCardholderName(String accountCardholderName) {
        this.accountCardholderName = accountCardholderName;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Integer getUserAccountType() {
        return userAccountType;
    }

    public void setUserAccountType(Integer userAccountType) {
        this.userAccountType = userAccountType;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }
}
