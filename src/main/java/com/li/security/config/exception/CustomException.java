package com.li.security.config.exception;

public class CustomException extends RuntimeException {

    private Integer code;

    private String message;

    private CustomException() {
    }

    public CustomException(CustomExceptionType customExceptionType, String message) {
        this.code = customExceptionType.getCode();
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
