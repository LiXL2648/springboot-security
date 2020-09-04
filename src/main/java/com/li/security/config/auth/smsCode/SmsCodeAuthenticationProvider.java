package com.li.security.config.auth.smsCode;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Objects;

public class SmsCodeAuthenticationProvider implements AuthenticationProvider {
    private UserDetailsService userDetailsService;

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    protected UserDetailsService getUserDetailsService() {
        return this.userDetailsService;
    }

    /**
     * 进行身份认证的逻辑
     * @param authentication    就是我们传入的Token
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //利用UserDetailsService获取用户信息，拿到用户信息后重新组装一个已认证的Authentication
        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;
        //根据手机号码拿到用户信息
        UserDetails userDetails = userDetailsService.loadUserByUsername((String) authenticationToken.getPrincipal());
        if (Objects.isNull(userDetails)) {
            throw new InternalAuthenticationServiceException("无法根据手机号获取用户信息");
        }
        SmsCodeAuthenticationToken result = new SmsCodeAuthenticationToken(userDetails, userDetails.getAuthorities());
        result.setDetails(authenticationToken.getDetails());
        return result;
    }

    /**
     * AuthenticationManager挑选一个AuthenticationProvider
     * 来处理传入进来的Token就是根据supports方法来判断的
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(aClass);
    }
}