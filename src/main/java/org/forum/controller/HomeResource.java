package org.forum.controller;

import org.forum.entities.Post;
import org.forum.entities.Topic;
import org.forum.entities.Post;
import org.forum.entities.Topic;
import org.forum.entities.user.User;
import org.forum.entities.user.UserProfile;
import org.forum.entities.user.exception.UserNotFoundException;
import org.forum.newform.NewPostFrom;
import org.forum.newform.NewSectionForm;
import org.forum.newform.ProfilForm;
import org.forum.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Controller
public class HomeResource {



    @Autowired
    private YearService yearService;

    @Autowired
    private UserService userService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserProfileService userProfileService;

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
    public String showForum(Model model) {
        NewPostFrom searchPost = new NewPostFrom();


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.getName().equals("anonymousUser")){
            model.addAttribute("oblubeneVlakna", null);
        }
        else{
            User user = userService.findByUsername(authentication.getName());
            model.addAttribute("oblubeneVlakna", user.getFavoriteTopics());
        }


        NewPostFrom searchPost = new NewPostFrom();

        model.addAttribute("currentPath", "null");
        model.addAttribute("roky", yearService.findAll());
        model.addAttribute("searchPost", searchPost);
        return "forum/forum";
    }

    @RequestMapping(value = "/forum/search")
    public String searchSpecificContent(Model model,
                                        @Valid @ModelAttribute("searchPost") NewPostFrom searchPost,
                                        BindingResult result) {
        if(result.hasErrors()) {
            model.addAttribute("currentPath", "null");
            model.addAttribute("roky", yearService.findAll());
            return "section/topic/topic";
        }

        List<Topic> topics = topicService.findAllByContent(searchPost.getContent());
        List<Topic> topics2 = topicService.findAllByTitle(searchPost.getContent());
        List<Post> posts = postService.findAllByContent(searchPost.getContent());

        List<Topic> topic_temp = new ArrayList<>();

        for(Post post : posts) {
            topic_temp.add(post.getTopic());
        }

        for(Topic topic : topics) {
            topic_temp.add(topic);
        }

        for(Topic topic : topics2) {
            topic_temp.add(topic);
        }

        List<Topic> listWithoutDuplicates = new ArrayList<>(
                new HashSet<>(topic_temp));

        model.addAttribute("vlakna", listWithoutDuplicates);

        System.out.println(searchPost.getContent());
        return "section/topic/specific_topics";
    }

    @RequestMapping("/403")
    public String accessdenied() {
        return "fragments/403";
    }

}
