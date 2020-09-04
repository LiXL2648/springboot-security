package com.li.security.config.auth.smsCode;

import com.li.security.config.auth.MyAuthenticationFailureHandler;
import com.li.security.config.auth.MyConstants;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

@Component
public class SmsCodeValidateFilter extends OncePerRequestFilter {

    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 拦截短信登录请求 /ssmLogin，并且请求方式为POST
        if (StringUtils.equals("/smsLogin", request.getRequestURI())
                && StringUtils.equalsIgnoreCase("POST", request.getMethod())) {

            try {
                validate(request);
            } catch (AuthenticationException e) {
                myAuthenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    // 验证规则
    private void validate(HttpServletRequest request) {

        HttpSession session = request.getSession();
        SmsCodeVo smsCodeVo = (SmsCodeVo) session.getAttribute(MyConstants.SMS_SESSION_KEY);
        String phoneNum = request.getParameter("phoneNum");
        String smsCode = request.getParameter("smsCode");

        if (StringUtils.isEmpty(phoneNum)) {
            throw new SessionAuthenticationException("手机号码不能为空");
        }

        if (StringUtils.isEmpty(smsCode)) {
            throw new SessionAuthenticationException("短信验证码不能为空");
        }

        if (Objects.isNull(smsCodeVo)) {
            throw new SessionAuthenticationException("短信验证码不存在，请重新获取");
        }

        if (smsCodeVo.isExpire()) {
            session.removeAttribute(MyConstants.SMS_SESSION_KEY);
            throw new SessionAuthenticationException("短信验证码已过期，请重新获取");
        }

        if (!StringUtils.equals(smsCodeVo.getCode(), smsCode)) {
            throw new SessionAuthenticationException("短信验证码输入不正确");
        }

        if (!StringUtils.equals(smsCodeVo.getPhoneNum(), phoneNum)) {
            throw new SessionAuthenticationException("短信发送目标与手机号码不一致");
        }

        session.removeAttribute(MyConstants.SMS_SESSION_KEY);
    }
}
