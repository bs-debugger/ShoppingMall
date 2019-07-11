package com.xq.live.vo.in;

import java.util.List;

/**
 * 审核商品状态
 * @author xuzhen
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2019/4/2315:18
 */
public class AduitGoodsSkuInVo {

    private List<Long> ids;

    private Integer aduitStatus;

    private String rejectResult;

    private String rejectPics;

    private Integer smsIsSend;


    public Integer getAduitStatus() {
        return aduitStatus;
    }

    public void setAduitStatus(Integer aduitStatus) {
        this.aduitStatus = aduitStatus;
    }

    public String getRejectResult() {
        return rejectResult;
    }

    public void setRejectResult(String rejectResult) {
        this.rejectResult = rejectResult;
    }

    public String getRejectPics() {
        return rejectPics;
    }

    public void setRejectPics(String rejectPics) {
        this.rejectPics = rejectPics;
    }

    public Integer getSmsIsSend() {
        return smsIsSend;
    }

    public void setSmsIsSend(Integer smsIsSend) {
        this.smsIsSend = smsIsSend;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
