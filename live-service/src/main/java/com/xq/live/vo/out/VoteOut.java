package com.xq.live.vo.out;

import com.xq.live.model.User;

/**
 * Created by admin on 2019/1/9.
 */
public class VoteOut {

    private Long totalVoteNum;

    private User user;

    private Long userId;

    public Long getTotalVoteNum() {
        return totalVoteNum;
    }

    public void setTotalVoteNum(Long totalVoteNum) {
        this.totalVoteNum = totalVoteNum;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
