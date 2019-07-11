package com.xq.live.vo.in;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author xuzhen
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2019/4/289:38
 */
public class PageGoodsSkuCommand {

    @ApiModelProperty(value = "当前页数",dataType = "int")
    private Integer page = 1;   //当前页
    @ApiModelProperty(hidden = true)
    private Integer start;
    @ApiModelProperty(value = "每页行数，默认10",dataType = "int")
    private Integer rows = 10;

    private String startTime;

    private String endTime;

    private String skuName;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }
}
