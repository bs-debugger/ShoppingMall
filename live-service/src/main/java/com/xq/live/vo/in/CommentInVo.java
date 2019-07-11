package com.xq.live.vo.in;

import javax.validation.constraints.NotNull;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-08 20:04
 * @copyright:hbxq
 **/
public class CommentInVo extends BaseInVo {
    private Long id;

    @NotNull(message = "refId必填")
    private Long refId; //关联的id
    @NotNull(message = "cmtType必填")
    private Integer cmtType;  //评论类型 1 活动 2 主题 3 直播 4 评论 5 商家
    //@NotNull(message = "userId必填")
    private Long userId;

    private Long zanUserId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
    }

    public Integer getCmtType() {
        return cmtType;
    }

    public void setCmtType(Integer cmtType) {
        this.cmtType = cmtType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getZanUserId() {
        return zanUserId;
    }

    public void setZanUserId(Long zanUserId) {
        this.zanUserId = zanUserId;
    }
}
