package com.xq.live.vo.out;

import com.xq.live.model.Attachment;

import java.util.Date;

/**
 * Created by lipeng on 2018/10/29.
 */
public class SalepointTopPicOut {
    private Long id;

    private Long salepointId;

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

    public Long getSalepointId() {
        return salepointId;
    }

    public void setSalepointId(Long salepointId) {
        this.salepointId = salepointId;
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
