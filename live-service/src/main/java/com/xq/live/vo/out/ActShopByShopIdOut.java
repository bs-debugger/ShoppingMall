package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 商家参与的活动列表
 *
 * @author lipeng
 * @date 2018-03-12 16:12
 * @copyright:hbxq
 **/
public class ActShopByShopIdOut implements Comparable<ActShopByShopIdOut>{

    private Long id;

    private Long actId;//活动id

    private Long shopId;//参加该活动的商家id

    private String shopCode;//参加该活动的商家编码

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//创建时间

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;//更新时间

    private String actName;//活动名称

    private Integer actStatus;//活动状态

    private String actDesc;//活动描述

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;//活动开始时间

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;//活动结束时间

    private String mainPic;//活动图片

    private String actUrl;//活动链接地址

    private Integer isDeleted;//活动是否删除

    private Integer isVote;//用户是否投票

    private Integer voteNum;//投票数量

    private Integer isLuoxuan;//是否落选

    private Long shopZoneItemId;//

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }

    public Integer getIsVote() {
        return isVote;
    }

    public void setIsVote(Integer isVote) {
        this.isVote = isVote;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getActUrl() {
        return actUrl;
    }

    public void setActUrl(String actUrl) {
        this.actUrl = actUrl;
    }

    public String getMainPic() {
        return mainPic;
    }

    public void setMainPic(String mainPic) {
        this.mainPic = mainPic;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getActDesc() {
        return actDesc;
    }

    public void setActDesc(String actDesc) {
        this.actDesc = actDesc;
    }

    public Integer getActStatus() {
        return actStatus;
    }

    public void setActStatus(Integer actStatus) {
        this.actStatus = actStatus;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Integer getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(Integer voteNum) {
        this.voteNum = voteNum;
    }

    public Integer getIsLuoxuan() {
        return isLuoxuan;
    }

    public void setIsLuoxuan(Integer isLuoxuan) {
        this.isLuoxuan = isLuoxuan;
    }

    public Long getShopZoneItemId() {
        return shopZoneItemId;
    }

    public void setShopZoneItemId(Long shopZoneItemId) {
        this.shopZoneItemId = shopZoneItemId;
    }

    @Override
    public int compareTo(ActShopByShopIdOut o) {
        SimpleDateFormat f = new SimpleDateFormat("hhmmss");
        int d1 = Integer.parseInt(f.format(this.getCreateTime()).toString());
        int d2 = Integer.parseInt(f.format(o.getCreateTime()).toString());
        if(d1>d2){
            return 1;
        }else{
            return -1;
        }

    }
}
