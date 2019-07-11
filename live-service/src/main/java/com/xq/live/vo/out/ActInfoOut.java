package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-03-06 19:32
 * @copyright:hbxq
 **/
public class ActInfoOut {
    @ApiModelProperty(value = "活动id")
    private Long id;
    @NotNull(message = "actName必填")
    @ApiModelProperty(value = "活动名称")
    private String actName;

    @NotNull(message = "startTime必填")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "活动开始时间")
    private Date startTime;

    @NotNull(message = "endTime必填")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "活动结束时间")
    private Date endTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "活动创建时间")
    private Date createTime;
    @ApiModelProperty(value = "活动状态")
    private Integer actStatus;  //活动状态
    @ApiModelProperty(value = "活动描述")
    private String actDesc;//活动描述
    @ApiModelProperty(value = "活动主图")
    private String mainPic; //主图url
    @ApiModelProperty(value = "活动链接url")
    private String actUrl;  //活动链接url
    @ApiModelProperty(value = "是否删除0 否 1 是")
    private Integer isDeleted;  //是否删除0 否 1 是

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;    //更新时间
    @ApiModelProperty(value = "总投票数")
    private int voteNum;      //总投票数
    @ApiModelProperty(value = "参与商家数")
    private int shopNum;     //参与商家数
    @ApiModelProperty(value = "浏览人数")
    private int viewNum;    //浏览人数

    private Integer isSign;//是否报名

    private Integer isSignForUser;//选手是否报名

    @ApiModelProperty(value = "活动模板 1十堰活动模板  2小视频活动  3邀请好友模版 4砍价活动 5限量秒杀活动 6抽奖活动 7拼团活动")
    private Integer type;

    private Integer skuType;
    @ApiModelProperty(value = "商家是否参与了此活动")
    private int isAdd;//商家是否参与了此活动
    @ApiModelProperty(value = "活动报名开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date applyStartTime;
    @ApiModelProperty(value = "活动报名结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date applyEndTime;
    @ApiModelProperty(value = "活动报名规则")
    private String applyRules;
    @ApiModelProperty(value = "报名允许的最大商品数目")
    private Integer applySignNum;//报名允许的最大商品数目
    @ApiModelProperty(value = "默认参与人数")
    private Integer peopleNum;//默认参与人数

    public Date getApplyStartTime() {
        return applyStartTime;
    }

    public void setApplyStartTime(Date applyStartTime) {
        this.applyStartTime = applyStartTime;
    }

    public Date getApplyEndTime() {
        return applyEndTime;
    }

    public void setApplyEndTime(Date applyEndTime) {
        this.applyEndTime = applyEndTime;
    }

    public String getApplyRules() {
        return applyRules;
    }

    public void setApplyRules(String applyRules) {
        this.applyRules = applyRules;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getActStatus() {
        return actStatus;
    }

    public void setActStatus(Integer actStatus) {
        this.actStatus = actStatus;
    }

    public String getActDesc() {
        return actDesc;
    }

    public void setActDesc(String actDesc) {
        this.actDesc = actDesc;
    }

    public String getMainPic() {
        return mainPic;
    }

    public void setMainPic(String mainPic) {
        this.mainPic = mainPic;
    }

    public String getActUrl() {
        return actUrl;
    }

    public void setActUrl(String actUrl) {
        this.actUrl = actUrl;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }

    public int getShopNum() {
        return shopNum;
    }

    public void setShopNum(int shopNum) {
        this.shopNum = shopNum;
    }

    public int getViewNum() {
        return viewNum;
    }

    public void setViewNum(int viewNum) {
        this.viewNum = viewNum;
    }

    public Integer getIsSign() {
        return isSign;
    }

    public void setIsSign(Integer isSign) {
        this.isSign = isSign;
    }

    public Integer getIsSignForUser() {
        return isSignForUser;
    }

    public void setIsSignForUser(Integer isSignForUser) {
        this.isSignForUser = isSignForUser;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSkuType() {
        return skuType;
    }

    public void setSkuType(Integer skuType) {
        this.skuType = skuType;
    }

    public int getIsAdd() {
        return isAdd;
    }

    public void setIsAdd(int isAdd) {
        this.isAdd = isAdd;
    }

    public Integer getApplySignNum() {
        return applySignNum;
    }

    public void setApplySignNum(Integer applySignNum) {
        this.applySignNum = applySignNum;
    }

    public Integer getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(Integer peopleNum) {
        this.peopleNum = peopleNum;
    }
}
