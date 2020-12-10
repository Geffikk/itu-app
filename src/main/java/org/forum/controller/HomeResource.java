package org.forum.controller;

import org.forum.entities.user.UserProfile;
import org.forum.entities.user.exception.UserNotFoundException;
import org.forum.newform.NewSectionForm;
import org.forum.newform.ProfilForm;
import org.forum.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeResource {



    @Autowired
    private YearService yearService;

    @Autowired
    private UserProfileService userProfileService;



    @RequestMapping(value = { "/", "/home" })
    public String home(Model model) {
        return "home/home";
    }


    @RequestMapping(value = "/ranking")
    public String showRanking(Model model) {
        return "home/ranking";
    }

    @RequestMapping(value = "/obchod")
    public String showShop(Model model) {
        return "home/obchod";
    }


    /** FORUM **/
    @RequestMapping(value = "/forum")
    public String showForum(Model model) {
        // TODO: 10/12/2020
        //spravit premennu oblubeneVlakna -> podla authorization.name si vyhladat uzivatela
        // a vybrat z neho jeho oblubene vlakna
        model.addAttribute("currentPath", "");
        model.addAttribute("roky", yearService.findAll());
        return "forum/forum";
    }








    @RequestMapping("/403")
    public String accessdenied() {
        return "fragments/403";
    }

}
