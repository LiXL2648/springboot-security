package com.li.security.config.auth;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MyUserDetailsMapper {

    // 根据用户名查询用户信息
    MyUserDetails findByUserName(String userName);

    // 根据用户名查询用户角色
    List<String> findRoleByUserName(String userName);

    // 根据用户角色查询用户权限
    List<String> findAuthorityByRoleCodes(@Param("roleCodes") List<String> roleCodes);

}
