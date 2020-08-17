package com.li.security.config.exception;

public enum CustomExceptionType {

    USER_INPUT_ERROR(400, "用户输入异常"),
    SYSTEM_ERROR(500, "系统内部异常"),
    OTHER_ERROR(999, "未知异常");

    private Integer code;

    private String typeDesc;

    CustomExceptionType(Integer code, String typeDesc) {
        this.code = code;
        this.typeDesc = typeDesc;
    }

    public Integer getCode() {
        return code;
    }

    public String getTypeDesc() {
        return typeDesc;
    }
}
