package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xq.live.model.Shop;
import com.xq.live.model.User;

import java.util.Date;
import java.util.List;

/**
 * Created by ss on 2018/12/9.
 * 秒杀商品出参
 */
public class SecondsKillGoodsSkuOut {
    private GoodsSkuOut goodsSkuOut;
    private Long parentId;//发起人id
    private Shop shop;
    private Integer peoPleNum;//邀请人数
    private List<User> newUser;//邀请的新用户
    private String valueKey;//值主键
    private String totleKey;//外主键

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//创建时间

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;//开始时间

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;//结束时间

    public String getValueKey() {
        return valueKey;
    }

    public void setValueKey(String valueKey) {
        this.valueKey = valueKey;
    }

    public String getTotleKey() {
        return totleKey;
    }

    public void setTotleKey(String totleKey) {
        this.totleKey = totleKey;
    }

    public GoodsSkuOut getGoodsSkuOut() {
        return goodsSkuOut;
    }

    public void setGoodsSkuOut(GoodsSkuOut goodsSkuOut) {
        this.goodsSkuOut = goodsSkuOut;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Integer getPeoPleNum() {
        return peoPleNum;
    }

    public void setPeoPleNum(Integer peoPleNum) {
        this.peoPleNum = peoPleNum;
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
