package com.xq.live.vo.in;

/**
 *直播入参对象
 *@author zhangpeng32
 *@create 2018-02-07 18:10
 **/
public class ZhiboInVo extends BaseInVo{
    private Long id;
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
