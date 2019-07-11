package com.xq.live.exception;

import com.xq.live.common.ResultStatus;

import java.text.MessageFormat;

/**
 * 自定义异常处理类
 * @author feitao <yyimba@qq.com> 2019/5/6 9:13
 */
public class AppException extends RuntimeException {

    /**
     * 接口返回结果统一样式
     */
    private ResultStatus resultStatus;

    public AppException(ResultStatus resultStatus) {
        super(resultStatus.getErrorMsg());
        this.resultStatus = resultStatus;
    }

    public AppException(ResultStatus resultStatus, Object ... msgFormat) {
        super(MessageFormat.format(resultStatus.getErrorMsg(), msgFormat));
        this.resultStatus = resultStatus;
    }

    public ResultStatus getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }

}
