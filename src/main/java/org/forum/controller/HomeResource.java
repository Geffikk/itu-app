package org.forum.controller;

import org.forum.entities.user.User;
import org.forum.entities.user.UserProfile;
import org.forum.entities.user.exception.UserNotFoundException;
import org.forum.newform.NewSectionForm;
import org.forum.newform.ProfilForm;
import org.forum.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeResource {



    @Autowired
    private YearService yearService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UserService userService;
    @RequestMapping(value = { "/help" })
    public String tryIt(Model model) {
        return "section/topic/text";
    }

    @RequestMapping(value = { "/", "/home" })
    public String home(Model model) {
        return "home/home";
    }


    @RequestMapping(value = "/rebricek")
    public String showRanking(Model model) {

        model.addAttribute("uzivateliaMoney", userService.findRecentMoney());
        model.addAttribute("uzivateliaPoints", userService.findRecentPoints());
        return "home/ranking";
    }

    @RequestMapping(value = "/obchod")
    public String showShop(Model model) {
        return "home/obchod";
    }

    @RequestMapping(value = "/spinandwin")
    public String showGame(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("user", userService.findByUsername(authentication.getName()));
        return "user/spin_and_win";
    }

    /** FORUM **/
    @RequestMapping(value = "/forum")
    public String showForum(Model model, Authentication authentication) {
        // TODO: 10/12/2020
        //spravit premennu oblubeneVlakna -> podla authorization.name si vyhladat uzivatela
        // a vybrat z neho jeho oblubene vlakna

        User user = userService.findByUsername(authentication.getName());

        model.addAttribute("oblubeneVlakna", user.getFavoriteTopics());
        model.addAttribute("currentPath", "null");
        model.addAttribute("roky", yearService.findAll());
        return "forum/forum";
    }


    @RequestMapping("/403")
    public String accessdenied() {
        return "fragments/403";
    }

}
