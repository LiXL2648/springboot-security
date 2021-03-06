package com.li.security.config;

import com.li.security.config.auth.*;
import com.li.security.config.auth.captchCode.CaptchaCodeFilter;
import com.li.security.config.auth.smsCode.SmsCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Autowired
    private CustomExpiredSessionStrategy customExpiredSessionStrategy;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private MyLogoutSuccessHandler myLogoutSuccessHandler;

    @Autowired
    private CaptchaCodeFilter captchaFilter;

    @Autowired
    private SmsCodeSecurityConfig smsCodeSecurityConfig;

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class)
                .logout() // 默认退出url为/logout
                .logoutUrl("/logout") // 指定退出的url，前端退出按钮需保持一致
                //.logoutSuccessUrl("/login.html") // 退出后返回的页面
                .logoutSuccessHandler(myLogoutSuccessHandler)
                .deleteCookies("JSESSIONID") // 删除cookie
                .and()
                .rememberMe()// 开启记住我的功能
                .rememberMeParameter("rememberMe") // 设置记住我参数名
                .rememberMeCookieName("remember-me-cookie") // 记住我的cookie名
                .tokenValiditySeconds(7 * 24 * 60 * 60) // 记住我的有效时长
                .tokenRepository(persistentTokenRepository()) //token数据库存储方式
                .and().csrf().disable() //禁用跨站csrf攻击防御，后面的章节会专门讲解
                .formLogin()
                .loginPage("/login.html") // 用户未登录时，访问任何资源都转跳到该路径，即登录页
                .loginProcessingUrl("/login") // 登录表单form中action的地址，也就是处理认证请求的路径
                .usernameParameter("username") // 登录表单form中用户名输入框input的name名，不修改的话默认是username
                .passwordParameter("password") // form中密码输入框input的name名，不修改的话默认是password
                //.defaultSuccessUrl("/index") // 登录认证成功后默认转跳的路径
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(myAuthenticationFailureHandler)
                .and().apply(smsCodeSecurityConfig)
                .and()
                .authorizeRequests()
                .antMatchers("/login.html", "/", "/login", "/kaptcha", "/smslogin", "/smsCode", "/smsLogin").permitAll() // 不需要通过登录验证就可以被访问的资源路径
                // .antMatchers("/biz1", "/biz2") // 需要对外暴露的资源路径
                // .hasAnyAuthority("ROLE_user", "ROLE_admin") // user 和 admin 角色可以访问的资源
                // .antMatchers("/syslog", "/sysuser")
                // .hasAnyRole("admin") // admin 角色可以访问的资源
                // .hasAnyAuthority("ROLE_admin") // 角色是一种特殊的权限
                // .antMatchers("/syslog").hasAuthority("/updUser")// 有该角色ID才能访问该资源
                // .antMatchers("/sysuser").hasAuthority("/delUser")
                // .anyRequest().authenticated()
                .antMatchers("/index").authenticated() // 用户登录即可访问的页面，即不需要任何权限
                .anyRequest().access("@rbacService.hasPermission(request, authentication)") //判断用户是否具备访问资源的权限
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)// Spring Security 精确的控制会话（默认）
                .invalidSessionUrl("/login.html") //会话超时之后，跳转到一个指定的URL
                .sessionFixation().migrateSession() // session的固化保护功能
                .maximumSessions(1) //表示同一个用户最大的登录数量
                .maxSessionsPreventsLogin(false) // true表示已经登录就不予许再次登录，false表示允许再次登录但是之前的登录会下线。
                .expiredSessionStrategy(customExpiredSessionStrategy); //session被下线(超时)之后的处理策略。
        /*
        http.httpBasic() // 开启httpBasic认证
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated(); // 所有请求都需要登录认证才能访问
        */
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /*
        auth.inMemoryAuthentication()
                .withUser("user")
                .password(passwordEncoder().encode("2648"))
                .roles("user")
                .and()
                .withUser("admin")
                .password(passwordEncoder().encode("2648"))
                .roles("admin")
                .authorities("sys:log","sys:user")
                .and()
                .passwordEncoder(passwordEncoder()); // 配置BCrypt加密
        */

        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {

        web.ignoring() // 将项目中的静态资源路径开放出来
                .antMatchers("/css/**", "/fonts/**", "/img/**", "/js/**", "/webjars/**", "/favicon.ico");
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }
}
