package com.xq.live.common;

public enum ActivityType {

    BANNER(0, "Banner推荐"),

    BENEFITS(1, "钜惠推荐"),

    YY_SECKILL(2, "1元秒杀"),

    CHOICENESS_SHOP(3, "精选商家"),

    OTHER(99, "其它活动");

    private Integer code;

    private String title;

    ActivityType(Integer code, String title) {
        this.code = code;
        this.title = title;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 根据活动Code获取标题
     * @param code
     * @return
     */
    public static String getTitleByCode(Integer code) {
        for (ActivityType activityType : ActivityType.values()) {
            if (activityType.getCode().equals(code)) {
                return activityType.getTitle();
            }
        }
        return null;
    }

}
