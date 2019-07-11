package com.xq.live.vo.in;

import java.util.Date;

/**
 * Created by admin on 2018/10/31.
 */
public class FormIdInVo {
    private  String formId;

    private Date cerateTime;

    private  Long userId;

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public Date getCerateTime() {
        return cerateTime;
    }

    public void setCerateTime(Date cerateTime) {
        this.cerateTime = cerateTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
