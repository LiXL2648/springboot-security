package com.li.security;

import com.li.security.dao.UserDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringBootSecurityTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDao userDao;

    @Test
    public void testDataSource() throws SQLException {
        System.out.println(dataSource);
        System.out.println(dataSource.getConnection());
    }

    @Test
    public void testGetUserById() {
        System.out.println(userDao.getUserById(1));
    }
}
