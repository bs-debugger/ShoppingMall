package com.xq.live.vo.in;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-09 13:34
 * @copyright:hbxq
 **/
public class CouponSkuInVo extends BaseInVo {
    private Long id;
    private Long skuId;
    private Long skuCode;
    private Integer type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(Long skuCode) {
        this.skuCode = skuCode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
