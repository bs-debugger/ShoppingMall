package com.xq.live.vo.in;

import java.util.Date;

/**
 * 活动推荐菜入参
 * Created by lipeng on 2018/6/13.
 */
public class ActSkuInVo extends BaseInVo{
    private Long id;

    //状态标记，没有查询全部菜品，查询没有落选的菜品
    private Long flag;

    private Long actId;

    private Long skuId;

    private String skuCode;

    private Long shopId;

    private Long prId;//推荐菜对应的使用规则id

    private Byte applyStatus;

    private Byte isLuoxuan;

    private Integer voteNum;

    private Date createTime;

    private Date updateTime;

    private Long voteUserId;//投票人的userId

    private Date beginTime;//开始时间

    private Date endTime;//结束时间

    private Integer sortType;//排序类型 1最新  null最热

    private String city;//分类城市

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getFlag() {
        return flag;
    }

    public void setFlag(Long flag) {
        this.flag = flag;
    }

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

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode == null ? null : skuCode.trim();
    }

    public Long getPrId() {
        return prId;
    }

    public void setPrId(Long prId) {
        this.prId = prId;
    }

    public Byte getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Byte applyStatus) {
        this.applyStatus = applyStatus;
    }

    public Byte getIsLuoxuan() {
        return isLuoxuan;
    }

    public void setIsLuoxuan(Byte isLuoxuan) {
        this.isLuoxuan = isLuoxuan;
    }

    public Integer getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(Integer voteNum) {
        this.voteNum = voteNum;
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

    public Long getVoteUserId() {
        return voteUserId;
    }

    public void setVoteUserId(Long voteUserId) {
        this.voteUserId = voteUserId;
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

    public Integer getSortType() {
        return sortType;
    }

    public void setSortType(Integer sortType) {
        this.sortType = sortType;
    }
}
