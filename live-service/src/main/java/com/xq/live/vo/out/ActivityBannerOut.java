package com.xq.live.vo.out;

import io.swagger.annotations.ApiModelProperty;

public class ActivityBannerOut {

    @ApiModelProperty("推荐ID")
    private Integer recommendId;

    @ApiModelProperty("活动类型")
    private Integer type;

    @ApiModelProperty("活动地址")
    private String linkUrl;

    @ApiModelProperty("活动封面图")
    private String imgUrl;

    @ApiModelProperty("商品ID")
    private Long goodsSkuId;

    @ApiModelProperty("商家ID")
    private Long shopId;

    @ApiModelProperty("活动ID")
    private Long actId;

    @ApiModelProperty("城市名")
    private String city;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("活动内容")
    private String content;
}
