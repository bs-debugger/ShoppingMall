package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xq.live.model.User;

import java.util.Date;
import java.util.List;

/**
 * Created by ss on 2018/8/30.
 */
public class SkuForNewUserOut {

    private Long parentId;//发起人id

    private Long skuId;//菜的id

    private Long shopId;//菜对应的shopid

    private Integer peoPleNum;//邀请人数

    private String skuName;//菜名

    private String skuPic;//菜品图片

    private List<User> newUser;//邀请的新用户

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//创建时间

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;//开始时间

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;//结束时间


    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuPic() {
        return skuPic;
    }

    public void setSkuPic(String skuPic) {
        this.skuPic = skuPic;
    }

    public Integer getPeoPleNum() {
        return peoPleNum;
    }

    public void setPeoPleNum(Integer peoPleNum) {
        this.peoPleNum = peoPleNum;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public List<User> getNewUser() {
        return newUser;
    }

    public void setNewUser(List<User> newUser) {
        this.newUser = newUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
