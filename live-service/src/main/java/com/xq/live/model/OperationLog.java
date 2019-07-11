package com.xq.live.model;

import io.swagger.annotations.ApiModelProperty;

public class OperationLog {

    
    @ApiModelProperty(value = "操作类型")
    private Integer type;

    @ApiModelProperty(value = "详细操作")
    private String remake;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getRemake() {
        return remake;
    }

    public void setRemake(String remake) {
        this.remake = remake;
    }
}
