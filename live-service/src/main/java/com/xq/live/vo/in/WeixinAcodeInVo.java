package com.xq.live.vo.in;

import io.swagger.annotations.ApiModelProperty;

public class WeixinAcodeInVo {

    @ApiModelProperty("携带的参数")
    private String scene;

    @ApiModelProperty("跳转地址")
    private String page;

    @ApiModelProperty("二维码的宽度，单位 px，最小 280px，最大 1280px")
    private Integer width;

    @ApiModelProperty("自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调，默认 false")
    private Boolean autoColor;

    @ApiModelProperty("auto_color 为 false 时生效，使用 rgb 设置颜色 例如 255,0,0")
    private String lineColor;

    @ApiModelProperty("是否需要透明底色，为 true 时，生成透明底色的小程序")
    private Boolean isHyaline;

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Boolean getAutoColor() {
        return autoColor;
    }

    public void setAutoColor(Boolean autoColor) {
        this.autoColor = autoColor;
    }

    public String getLineColor() {
        return lineColor;
    }

    public void setLineColor(String lineColor) {
        this.lineColor = lineColor;
    }

    public Boolean getIsHyaline() {
        return isHyaline;
    }

    public void setIsHyaline(Boolean isHyaline) {
        this.isHyaline = isHyaline;
    }
}
