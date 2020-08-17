package com.li.security.config.exception;

import lombok.Data;

@Data
public class AjaxResponse {

    private Boolean isok;

    private Integer code;

    private String Message;

    private Object data;

    private AjaxResponse() {}

    public static AjaxResponse success() {
        AjaxResponse result = new AjaxResponse();
        result.setIsok(true);
        result.setCode(200);
        result.setMessage("success");
        return result;
    }

    public static AjaxResponse success(Object data) {
        AjaxResponse result = new AjaxResponse();
        result.setIsok(true);
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    public static AjaxResponse error(CustomException e) {
        AjaxResponse result = new AjaxResponse();
        result.setIsok(false);
        result.setCode(e.getCode());
        if (e.getCode() == CustomExceptionType.USER_INPUT_ERROR.getCode()) {
            result.setMessage(e.getMessage());
        } else if (e.getCode() == CustomExceptionType.SYSTEM_ERROR.getCode()) {
            result.setMessage(e.getMessage() + "，系统出现异常");
        } else {
            result.setMessage(e.getMessage() + "，系统出现未知异常");
        }
        return  result;
    }
}
