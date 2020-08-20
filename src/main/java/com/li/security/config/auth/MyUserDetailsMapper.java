package com.li.security.config.auth;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MyUserDetailsMapper {

    MyUserDetails findByUserName(String userName);

    List<String> findRoleByUserName(String userName);

    List<String> findAuthorityByRoleCodes(List<String> roleCodes);
}
