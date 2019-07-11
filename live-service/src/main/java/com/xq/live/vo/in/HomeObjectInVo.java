package com.xq.live.vo.in;

/**
 * Created by ss on 2018/11/13.
 * 首页类目对象对应入参
 */
public class HomeObjectInVo {

    private String type;

    private Integer start;

    public HomeObjectInVo(){

    }

    public HomeObjectInVo(String type,Integer start){
        this.setStart(start);
        this.setType(type);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }
}
