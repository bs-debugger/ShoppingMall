package com.xq.live.vo.out;

import com.xq.live.model.SalePointUser;
import com.xq.live.model.User;

import java.util.Date;

/**
 * Created by ss on 2019/1/26.
 */
public class SalePointUserOut {

    private SalePointUser salePointUser;

    private User user;

    public SalePointUser getSalePointUser() {
        return salePointUser;
    }

    public void setSalePointUser(SalePointUser salePointUser) {
        this.salePointUser = salePointUser;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
