package com.li.security.config.auth.smsCode;

import java.time.LocalDateTime;

public class SmsCodeVo {

    private String code; // 短信验证码

    private LocalDateTime expireTime; // 过期时间

    private String phoneNum;

    public SmsCodeVo(String code, int expireAfterSeconds, String phoneNum) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireAfterSeconds);
        this.phoneNum = phoneNum;
    }

    public boolean isExpire() {
        return LocalDateTime.now().isAfter(this.expireTime);
    }

    public String getCode() {
        return code;
    }

    public String getPhoneNum() {
        return phoneNum;
    }
}
