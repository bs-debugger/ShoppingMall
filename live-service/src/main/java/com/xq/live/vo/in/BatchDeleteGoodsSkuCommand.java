package com.xq.live.vo.in;

import java.util.List;

/**
 * @author xuzhen
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2019/4/2810:18
 */
public class BatchDeleteGoodsSkuCommand {

    private List<Long>  skuIds;

    public List<Long> getSkuIds() {
        return skuIds;
    }

    public void setSkuIds(List<Long> skuIds) {
        this.skuIds = skuIds;
    }
}
