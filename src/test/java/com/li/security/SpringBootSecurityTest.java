package com.li.security;

import com.li.security.config.auth.MyUserDetails;
import com.li.security.config.auth.MyUserDetailsMapper;
import com.li.security.config.auth.MyUserDetailsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringBootSecurityTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MyUserDetailsMapper myUserDetailsMapper;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testLoadUserByUsername() {
        UserDetails userDetails = myUserDetailsService.loadUserByUsername("admin");
        System.out.println(userDetails.getAuthorities());
    }

    @Test
    public void testFindByUserName() {
        MyUserDetails myUserDetails = myUserDetailsMapper.findByUserName("admin");
        System.out.println(myUserDetails);
    }

    @Test
    public void testFindRoleByUserName() {
        List<String> roleList = myUserDetailsMapper.findRoleByUserName("admin");
        System.out.println(roleList);
    }

    @Test
    public void testFindAuthorityByRoleCodes() {
        List<String> authorities = myUserDetailsMapper.findAuthorityByRoleCodes(Arrays.asList("admin"));
        System.out.println(authorities);
    }

    @Test
    public void testDataSource() throws SQLException {
        System.out.println(dataSource);
        System.out.println(dataSource.getConnection());
    }

    @Test
    public void contextLoads() {
        System.out.println(passwordEncoder.encode("2648"));
    }
}
