package com.demo.jsf.webapp.config;

import com.demo.jsf.webapp.dao.UserManagementDAO;
import com.demo.jsf.webapp.dao.UserManagementDAOImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringCoreConfig {

    @Bean
    public UserManagementDAO userManagementDAO() {
        return new UserManagementDAOImpl();
    }
}
