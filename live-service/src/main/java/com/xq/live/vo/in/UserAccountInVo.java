package com.xq.live.vo.in;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * com.xq.live.vo.in
 *
 * @author zhangpeng32
 * Created on 2018/5/5 下午6:48
 * @Description:
 */
public class UserAccountInVo extends BaseInVo {
    @ApiModelProperty(value = "用户账户id")
    private Long id;    //用户账户主键
    @ApiModelProperty(value = "用户表ID")
    private Long userId;    //用户表ID
    @ApiModelProperty(value = "用户账号")
    private String userName;    //用户账号
    @ApiModelProperty(value = "支付账号")
    private String accountName;    //支付账号
    @ApiModelProperty(value = "发生金额")
    private BigDecimal occurAmount;    //发生金额
    @ApiModelProperty(value = "发生金币")
    private Integer occurGold;    //发生金币
    @ApiModelProperty(value = "账户类型 0 享七  1 微信  2支付宝")
    private Integer accountType;   //账户类型 0 享七  1 微信  2支付宝
    @ApiModelProperty(value = "账户余额")
    private BigDecimal accountAmount;   //账户余额
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
    @ApiModelProperty(value = "用户可提现余额")
    private BigDecimal passedAmount;//用户可提现余额
    @ApiModelProperty(value = "用户审核中的余额")
    private BigDecimal reviewAmount;//用户审核中的余额
    @ApiModelProperty(value = "用户获取失败金额")
    private BigDecimal failAmount;//用户获取失败金额
    @ApiModelProperty(value = "所属银行")
    private  String cardBankname;//所属银行
    @ApiModelProperty(value = "用户余额")
    private  BigDecimal userAmount;//用户余额（小程序上的用户余额，和account_mount商家余额分开）
    @ApiModelProperty(value = "操作金额类型：1，商户余额 3,用户余额,4，用户已获得的奖励金，5，用户审核中的奖励金，6，用户获取失败的奖励金")
    private Integer type;//操作金额类型：1，商户余额 3,用户余额,4，用户已获得的奖励金，5，用户审核中的奖励金，6，用户获取失败的奖励金

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

    @ApiModelProperty(value = "详情")
    private String remake;

    private  Long shopId;

    public Integer getGold() {
        return gold;
    }

    public void setGold(Integer gold) {
        this.gold = gold;
    }

    public Integer getOccurGold() {
        return occurGold;
    }

    public void setOccurGold(Integer occurGold) {
        this.occurGold = occurGold;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        this.userName = userName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }

    public BigDecimal getOccurAmount() {
        return occurAmount;
    }

    public void setOccurAmount(BigDecimal occurAmount) {
        this.occurAmount = occurAmount;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public String getRemake() {
        return remake;
    }

    public void setRemake(String remake) {
        this.remake = remake;
    }
}
