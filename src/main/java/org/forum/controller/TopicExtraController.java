package org.forum.controller;

import org.forum.entities.Section;
import org.forum.entities.Topic;
import org.forum.entities.user.User;
import org.forum.newform.NewPostFrom;
import org.forum.service.PostService;
import org.forum.service.TopicService;
import org.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@ComponentScan
@Controller
public class TopicExtraController {

    @Autowired
    private TopicService topicService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "vlakno/{idVlakno}")
    public String getTopicById(@PathVariable int idVlakno, Model model) {

        Topic topic = topicService.findOne(idVlakno);
        NewPostFrom editPostFrom = new NewPostFrom();

        model.addAttribute("topic", topic);
        model.addAttribute("skolskyRok", topic.getSection().getStudyYear());
        model.addAttribute("rok", topic.getSection().getStudyYear().getYear());
        model.addAttribute("skupina", topic.getSection());
        model.addAttribute("posts", postService.findByTopic(idVlakno));
        model.addAttribute("newPost", new NewPostFrom());
        model.addAttribute("editPost", editPostFrom);
        model.addAttribute("currentPath", topic.getSection().getStudyYear().getYear().getName() + " / " + topic.getSection().getStudyYear().getName() + " / " + topic.getSection().getName() + " / " + topicService.findOne(idVlakno).getTitle());

        return "section/topic/topic";
    }

    @RequestMapping(value = "vlakno1/{idVlakno}")
    public String getTopic1ById(@PathVariable int idVlakno, Model model) {

        Topic topic = topicService.findOne(idVlakno);
        NewPostFrom editPostFrom = new NewPostFrom();

        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<String> notif_id = user.getNotificationListId();

        notif_id.remove(Integer.toString(idVlakno));

        String notif_id_str = user.notificationFromListToStringId(notif_id);
        user.setNotifications_id(notif_id_str);
        userService.save(user);

        model.addAttribute("topic", topic);
        model.addAttribute("skolskyRok", topic.getSection().getStudyYear());
        model.addAttribute("rok", topic.getSection().getStudyYear().getYear());
        model.addAttribute("skupina", topic.getSection());
        model.addAttribute("posts", postService.findByTopic(idVlakno));
        model.addAttribute("newPost", new NewPostFrom());
        model.addAttribute("editPost", editPostFrom);
        model.addAttribute("currentPath", topic.getSection().getStudyYear().getYear().getName() + " / " + topic.getSection().getStudyYear().getName() + " / " + topic.getSection().getName() + " / " + topicService.findOne(idVlakno).getTitle());

        return "section/topic/topic";
    }
}
