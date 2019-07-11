package com.xq.live.vo.in;

import javax.validation.constraints.NotNull;

/**
 * @author xuzhen
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2019/4/1516:07
 */
public class BankInVo extends BaseInVo{

    private Long id;

    /**
     * 银行卡号
     */
    private String accountNo;

    /**
     * 持卡人姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 1 绑定到用户  2 绑定到商家
     */
    private Integer bindType;


    //支付安全码
    private String securityCode;

    /**
     * 绑定银行步骤 1 校验银行卡  2 校验验证码
     */
    private Integer step;

    /**
     * 验证码
     */
    private String code;



    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getBindType() {
        return bindType;
    }

    public void setBindType(Integer bindType) {
        this.bindType = bindType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
