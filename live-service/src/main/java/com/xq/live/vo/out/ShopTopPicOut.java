package com.xq.live.vo.out;

import com.xq.live.model.Attachment;

import java.util.Date;

/**
 * @package: com.xq.live.vo.out
 * @description: 商家顶部图片出参
 * @author: zhangpeng32
 * @date: 2018/3/20 15:38
 * @version: 1.0
 */
public class ShopTopPicOut {
    private Long id;

    private Long shopId;

    private Long attachementId;

    private Integer isDeleted;

    private Date createTime;

    private Attachment attachment;

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

    public Long getAttachementId() {
        return attachementId;
    }

    public void setAttachementId(Long attachementId) {
        this.attachementId = attachementId;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }
}
