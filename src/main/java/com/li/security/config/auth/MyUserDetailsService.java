package com.li.security.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private MyUserDetailsMapper myUserDetailsMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 根据用户名查询用户信息
        MyUserDetails myUserDetails = myUserDetailsMapper.findByUserName(username);

        if (myUserDetails == null) {
            throw new UsernameNotFoundException("该用户不存在");
        }

        // 2. 根据用户明显查询用户角色
        List<String> roleCodes = myUserDetailsMapper.findRoleByUserName(username);

        // 3. 根据用户角色查询用户权限
        List<String> authorities = myUserDetailsMapper.findAuthorityByRoleCodes(roleCodes);

        // 4. 角色是一种特殊的权限，并且角色是以ROLE_为前缀（Spring Security规范）
        roleCodes = roleCodes.stream()
                .map(role -> "ROLE_" + role)
                .collect(Collectors.toList());

        // 5. 将所有角色添加到权限集合中
        authorities.addAll(roleCodes);

        // 6. 转成用逗号分隔的字符串，为用户设置权限标识
        myUserDetails.setAuthorities(
                AuthorityUtils.commaSeparatedStringToAuthorityList(
                        String.join(",", authorities)
                )
        );
        return myUserDetails;
    }
}
