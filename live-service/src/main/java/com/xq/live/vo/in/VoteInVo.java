package com.xq.live.vo.in;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-21 21:14
 * @copyright:hbxq
 **/
public class VoteInVo extends  BaseInVo {
    private Long id;

    @NotNull(message = "actId必填")
    private Long actId;

    //@NotNull(message = "shopId必填")
    private Long shopId;

    //@NotNull(message = "userId必填")
    private Long userId;

    private Long playerUserId;//参与选手的用户id

    private Long skuId;//推荐菜的id

    private Date createTime;

    private Integer type;//投票操作  1投票 2取消投票

    private Date beginTime;//开始时间

    private Date endTime;//结束时间

    private Integer voteNum;//投票次数

    private Long actGoodsSkuId;//活动商品关联id

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

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getPlayerUserId() {
        return playerUserId;
    }

    public void setPlayerUserId(Long playerUserId) {
        this.playerUserId = playerUserId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(Integer voteNum) {
        this.voteNum = voteNum;
    }

    public Long getActGoodsSkuId() {
        return actGoodsSkuId;
    }

    public void setActGoodsSkuId(Long actGoodsSkuId) {
        this.actGoodsSkuId = actGoodsSkuId;
    }
}
