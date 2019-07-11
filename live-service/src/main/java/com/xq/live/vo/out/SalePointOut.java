package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.javatuples.Pair;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2018/9/26.
 */
public class SalePointOut {

    private Long id;

    private String salepointCode;

    private String salepointName;

    private String city;

    private String address;

    private String mobile;

    private String phone;

    private String indexUrl;

    private String logoUrl;

    private String salepointInfo;

    private BigDecimal locationX;

    private BigDecimal locationY;

    private Byte isDeleted;

    private Byte applyStatus;

    private Byte salepointStatus;

    private String remark;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private String businessCate;

    private Object locationPoint;

    private String salepointHours;

    private String otherService;

    private Integer distance;    //距离

    private Integer type;//销售点类型(1超市 2酒店 3景点)

    private Integer chainType;//联锁店

    private List<Pair<String, String>> salepointTopPics;//销售点顶部图片组

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSalepointCode() {
        return salepointCode;
    }

    public void setSalepointCode(String salepointCode) {
        this.salepointCode = salepointCode == null ? null : salepointCode.trim();
    }

    public String getSalepointName() {
        return salepointName;
    }

    public void setSalepointName(String salepointName) {
        this.salepointName = salepointName == null ? null : salepointName.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl == null ? null : indexUrl.trim();
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl == null ? null : logoUrl.trim();
    }

    public String getSalepointInfo() {
        return salepointInfo;
    }

    public void setSalepointInfo(String salepointInfo) {
        this.salepointInfo = salepointInfo == null ? null : salepointInfo.trim();
    }

    public BigDecimal getLocationX() {
        return locationX;
    }

    public void setLocationX(BigDecimal locationX) {
        this.locationX = locationX;
    }

    public BigDecimal getLocationY() {
        return locationY;
    }

    public void setLocationY(BigDecimal locationY) {
        this.locationY = locationY;
    }

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Byte getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Byte applyStatus) {
        this.applyStatus = applyStatus;
    }

    public Byte getSalepointStatus() {
        return salepointStatus;
    }

    public void setSalepointStatus(Byte salepointStatus) {
        this.salepointStatus = salepointStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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

    public String getBusinessCate() {
        return businessCate;
    }

    public void setBusinessCate(String businessCate) {
        this.businessCate = businessCate == null ? null : businessCate.trim();
    }

    public Object getLocationPoint() {
        return locationPoint;
    }

    public void setLocationPoint(Object locationPoint) {
        this.locationPoint = locationPoint;
    }

    public String getSalepointHours() {
        return salepointHours;
    }

    public void setSalepointHours(String salepointHours) {
        this.salepointHours = salepointHours == null ? null : salepointHours.trim();
    }

    public String getOtherService() {
        return otherService;
    }

    public void setOtherService(String otherService) {
        this.otherService = otherService == null ? null : otherService.trim();
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<Pair<String, String>> getSalepointTopPics() {
        return salepointTopPics;
    }

    public void setSalepointTopPics(List<Pair<String, String>> salepointTopPics) {
        this.salepointTopPics = salepointTopPics;
    }

    public Integer getChainType() {
        return chainType;
    }

    public void setChainType(Integer chainType) {
        this.chainType = chainType;
    }
}
