package com.demo.jsf.webapp.dao;

import java.util.List;

public interface UserManagementDAO {

    boolean createUser(String newUserData);
    List<String> getUsers();
    void clearUsers();
}
