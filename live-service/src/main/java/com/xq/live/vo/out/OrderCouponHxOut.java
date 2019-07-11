package com.xq.live.vo.out;

/**
 * 商城系统订单票券核销出参
 */
public class OrderCouponHxOut {
    private Long id;//票券id

    private Long shopId;//商家id

    private String shopName;//商家名字

    private Long salePointId;//核销点id

    private Long hxUserId;//核销人的userId

    private String hxUserName;//核销人的userName

    private String errorCode;//0代表拥有核销权限  -1代表没有核销权限

    private String errorMessage;//errorCode为0此为null,errorCode为-1此为不能核销原因

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Long getSalePointId() {
        return salePointId;
    }

    public void setSalePointId(Long salePointId) {
        this.salePointId = salePointId;
    }

    public Long getHxUserId() {
        return hxUserId;
    }

    public void setHxUserId(Long hxUserId) {
        this.hxUserId = hxUserId;
    }

    public String getHxUserName() {
        return hxUserName;
    }

    public void setHxUserName(String hxUserName) {
        this.hxUserName = hxUserName;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
