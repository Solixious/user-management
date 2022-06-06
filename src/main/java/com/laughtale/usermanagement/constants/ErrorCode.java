package com.laughtale.usermanagement.constants;

import lombok.Getter;

@Getter
public enum ErrorCode {
    USERNAME_EXISTS("Username Already In Use"),
    EMAIL_EXISTS("Email Already In Use"),
    PASSWORD_LENGTH("Password Too Short"),
    USERNAME_TOO_SHORT_LENGTH("Username Too Short"),
    USERNAME_TOO_LONG_LENGTH("Username Too Long"),
    INVALID_USERNAME("The Username Contains Invalid Characters"),
    INVALID_CREDENTIALS("The Username And Password Combination Does Not Exist"),
    USER_DISABLED("The User Has Been Disabled By The Admin"),
    GENERAL_ERROR("Internal Application Error Occurred");

    String errorMessage;
    ErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
