package com.xq.live.vo.in;

/**
 * Created by lipeng on 2018/3/5.
 */
public class FavoritesInVo extends BaseInVo {
    private Long id;

    private Long userId;

    private Long shopId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }
}
