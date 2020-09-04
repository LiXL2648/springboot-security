package com.li.security.config.auth.smsCode;

import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SmsCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String SPRING_SECURITY_FORM_PHONENUM_KEY = "phoneNum";

    private String phoneNumParameter = SPRING_SECURITY_FORM_PHONENUM_KEY;
    private boolean postOnly = true;

    public SmsCodeAuthenticationFilter() {
        super(new AntPathRequestMatcher("/smsLogin", "POST"));
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            String phoneNum = this.obtainPhoneNum(request);

            if (phoneNum == null) {
                phoneNum = "";
            }


            phoneNum = phoneNum.trim();
            SmsCodeAuthenticationToken authRequest = new SmsCodeAuthenticationToken(phoneNum);
            this.setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }

    @Nullable
    protected String obtainPhoneNum(HttpServletRequest request) {
        return request.getParameter(this.phoneNumParameter);
    }

    protected void setDetails(HttpServletRequest request, SmsCodeAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    public void setPhoneNumParameter(String phoneNumParameter) {
        Assert.hasText(phoneNumParameter, "Username parameter must not be empty or null");
        this.phoneNumParameter = phoneNumParameter;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getPhoneNumParameter() {
        return this.phoneNumParameter;
    }

}