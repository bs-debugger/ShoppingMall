package com.xq.live.vo.in;

import javax.validation.constraints.NotNull;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-08 19:33
 * @copyright:hbxq
 **/
public class ZanInVo extends BaseInVo {
    private Long id;
    @NotNull(message = "refId必填")
    private Long refId;
    @NotNull(message = "type必填")
    private Integer type;

    private Long userId;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
