package org.forum.service;

import org.forum.entities.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCreationServiceImpl implements UserCreationService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public void create(User user) {
        userService.save(prepareUzivatel(user));
    }

    private User prepareUzivatel(User user) {
        user.setPassword(getEncodedPassword(user.getPassword()));
        return user;
    }

    private String getEncodedPassword(String heslo) {
        return passwordEncoder.encode(heslo);
    }

}
