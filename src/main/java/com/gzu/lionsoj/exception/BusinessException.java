package com.gzu.lionsoj.exception;

import com.gzu.lionsoj.common.ErrorCode;

/**
 * @Classname: BusinessException
 * @Description: 自定义异常
 * @Author: lions
 * @Datetime: 12/28/2023 11:42 PM
 */
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
