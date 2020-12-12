package org.forum.controller;

import org.forum.entities.user.User;
import org.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@ComponentScan
@Controller
@RequestMapping("/spinandwin/")
public class HappyWheelController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/win")
    public String result(HttpServletRequest request) {
        String number = request.getParameter("val1");
        int youwon = Integer.parseInt(number);
        youwon = youwon - 700;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName());
        int usermoney = user.getMoney() + youwon;

        user.setMoney(usermoney);
        userService.save(user);

        return "user/spin_and_win";
    }
}
