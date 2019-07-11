package com.xq.live.vo.in;

import io.swagger.annotations.ApiModelProperty;

/**
 * 入参基础类
 * @author zhangpeng32
 * @create 2018-02-07 15:02
 **/
public class BaseInVo {
    @ApiModelProperty(value = "当前页数",dataType = "int")
    private Integer page = 1;   //当前页
    @ApiModelProperty(hidden = true)
    private Integer start;
    @ApiModelProperty(value = "每页行数，默认10",dataType = "int")
    private Integer rows = 10;
    /**
     * 搜索关键字
     */
    @ApiModelProperty(value = "搜索关键字",dataType = "string")
    private String searchKey;

    public Integer getStart() {
        if (this.start!=null&& this.start>0){
            return this.start;
        }
        start = rows * (page - 1);
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

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }
}
