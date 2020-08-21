package com.li.security.config.auth.imagecode;

import java.time.LocalDateTime;

public class CaptchaImageVO {

    private String code;

    private LocalDateTime expireTime;

    public CaptchaImageVO(String code,  int expireAfterSeconds) {
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
