package com.li.security.config.auth.smsCode;

import com.li.security.config.auth.MyConstants;
import com.li.security.config.auth.MyUserDetails;
import com.li.security.config.auth.MyUserDetailsMapper;
import com.li.security.config.exception.AjaxResponse;
import com.li.security.config.exception.CustomException;
import com.li.security.config.exception.CustomExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RestController
public class SmsCodeController {

    @Autowired
    private MyUserDetailsMapper myUserDetailsMapper;

    @PostMapping("/smsCode")
    public AjaxResponse getSmsCode(@RequestParam(value = "phoneNum", required = true) String phoneNum,
                                   HttpSession session) {

        MyUserDetails myUserDetails = myUserDetailsMapper.findByUserName(phoneNum);
        if (myUserDetails == null || !myUserDetails.isEnabled()) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "您输入的手机号未注册"));
        }

        // 构造smsCodeVo
        SmsCodeVo smsCodeVo = new SmsCodeVo(RandomStringUtils.randomNumeric(6), 120, phoneNum);

        // 调用短信服务提供商的接口发送短信
        log.info(smsCodeVo.getCode() + " -> " + phoneNum);

        // 将封装好的smsCodeVo保存到session中
        session.setAttribute(MyConstants.SMS_SESSION_KEY, smsCodeVo);

        return AjaxResponse.success("短信验证码已经发送");
    }
}
