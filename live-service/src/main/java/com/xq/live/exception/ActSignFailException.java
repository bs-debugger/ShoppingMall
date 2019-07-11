package com.xq.live.exception;

/**
 * 新平台活动报名失败异常
 * Created by lipeng on 2018/4/28.
 */
public class ActSignFailException extends RuntimeException{
    public ActSignFailException(String message) {
        super(message);
    }
}
