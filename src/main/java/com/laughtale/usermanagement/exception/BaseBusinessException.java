package com.laughtale.usermanagement.exception;

import com.laughtale.usermanagement.constants.ErrorCode;
import lombok.Getter;

@Getter
public class BaseBusinessException extends Exception {

    private String errorCode;
    private String errorMessage;

    public BaseBusinessException(final ErrorCode errorCode) {
        this.errorCode = errorCode.name();
        this.errorMessage = errorCode.getErrorMessage();
    }
}
