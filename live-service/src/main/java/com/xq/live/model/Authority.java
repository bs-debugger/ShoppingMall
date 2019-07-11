package com.xq.live.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * @package: com.xq.live.model
 * @description: TODO
 * @author: zhangpeng32
 * @date: 2018/4/20 15:49
 * @version: 1.0
 */
public class Authority implements GrantedAuthority {

    private String authority;

    public Authority(String authority) {
        this.authority = authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }
}
