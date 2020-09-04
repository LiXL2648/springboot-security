package com.li.security.config.auth.captchCode;

import com.li.security.config.auth.MyAuthenticationFailureHandler;
import com.li.security.config.auth.MyConstants;
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

@Component
public class CaptchaCodeFilter extends OncePerRequestFilter {

    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 1. 必须是登录的post请求才能进行验证，其他的直接放行
        if ("/login".equals(request.getRequestURI()) && "POST".equalsIgnoreCase(request.getMethod())) {
            try {
                // 2.验证谜底与用户输入是否匹配
                validate(request);
            } catch (AuthenticationException e) {
                // 3.捕获步骤1中校验出现异常，交给失败处理类进行进行处理
                myAuthenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void validate(HttpServletRequest request) {
        String captchaCode = request.getParameter("captchaCode");
        if (captchaCode == null || captchaCode.isEmpty()) {
            throw new SessionAuthenticationException("请输入验证码");
        }

        HttpSession session = request.getSession();
        CaptchaCodeVo captchaCodeVo = (CaptchaCodeVo) session.getAttribute(MyConstants.CAPTCHA_SESSION_KEY);
        if (captchaCodeVo == null) {
            throw new SessionAuthenticationException("验证码不存在，请重新加载");
        }

        if (captchaCodeVo.isExpire()) {
            session.removeAttribute(MyConstants.CAPTCHA_SESSION_KEY);
            throw new SessionAuthenticationException("验证码已过期");
        }

        if (!captchaCodeVo.getCode().equalsIgnoreCase(captchaCode)) {
            throw new SessionAuthenticationException("验证码不匹配");
        }

        session.removeAttribute(MyConstants.CAPTCHA_SESSION_KEY);
    }
}
