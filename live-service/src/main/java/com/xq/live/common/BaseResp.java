package com.xq.live.common;

/**
 * 数据返回统一格式
 * @author zhangpeng32
 * @param <T>
 * @create 2018-01-17 19:42
 **/

import io.swagger.annotations.ApiModelProperty;

public class BaseResp<T> {
    /**
     * 返回码
     */
    @ApiModelProperty(value = "返回码 1成功 2失败" )
    private int code;

    /**
     * 返回信息描述
     */
    @ApiModelProperty(value = "返回信息描述")
    private String message;

    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据对象")
    private T data;

    @ApiModelProperty(value = "当前时间")
    private long currentTime;

    @ApiModelProperty(value = "总条数")
    private int total;

    public BaseResp(){}

    /**
     *
     * @param code 错误码
     * @param message 信息
     * @param data 数据
     */
    public BaseResp(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.currentTime = System.currentTimeMillis();
    }

    /**
     * 不带数据的返回结果
     * @param resultStatus
     */
    public BaseResp(ResultStatus resultStatus) {
        this.code = resultStatus.getErrorCode();
        this.message = resultStatus.getErrorMsg();
        this.currentTime = System.currentTimeMillis();
    }

    /**
     * 带数据的返回结果
     * @param resultStatus
     * @param data
     */
    public BaseResp(ResultStatus resultStatus, T data) {
        this.code = resultStatus.getErrorCode();
        this.message = resultStatus.getErrorMsg();
        this.data = data;
        this.currentTime = System.currentTimeMillis();
    }

    /**
     * 不带数据返回成功的数据结果
     * @author feitao <yyimba@qq.com> 2019-5-7 10:07:41
     * @return
     */
    public static BaseResp success() {
        return new BaseResp(ResultStatus.SUCCESS);
    }

    /**
     * 不带数据返回失败的数据结果
     * @author feitao <yyimba@qq.com> 2019-5-7 10:07:41
     * @return
     */
    public static BaseResp fail() {
        return new BaseResp(ResultStatus.FAIL);
    }

    /**
     * 不带数据返回异常的数据结果
     * @author feitao <yyimba@qq.com> 2019-5-7 10:07:41
     * @return
     */
    public static BaseResp error(ResultStatus resultStatus) {
        return new BaseResp(resultStatus);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
