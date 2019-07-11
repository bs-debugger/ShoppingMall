package com.xq.live.vo.out;

/**
 * 退款原因出参
 * Created by admin on 2018/10/19.
 */
public class PayRefundReasonOut {

    private Long id;

    private String reason;

    private Integer sort;

    private Integer type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
