package com.li.security.config.auth.captchCode;

import java.time.LocalDateTime;

public class CaptchaCodeVo {

    private String code;

    private LocalDateTime expireTime;

    public CaptchaCodeVo(String code, int expireAfterSeconds) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireAfterSeconds);
    }

    public boolean isExpire() {
        return LocalDateTime.now().isAfter(this.expireTime);
    }

    public String getCode() {
        return code;
    }
}
