package org.forum.controller;

import org.forum.entities.Topic;
import org.forum.newform.NewPostFrom;
import org.forum.service.PostService;
import org.forum.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@ComponentScan
@Controller
public class TopicExtraController {

    @Autowired
    private TopicService topicService;

    @Autowired
    private PostService postService;

    @RequestMapping(value = "vlakno/{idVlakno}")
    public String getTopicById(@PathVariable int idVlakno, Model model) {

        Topic topic = topicService.findOne(idVlakno);
        NewPostFrom editPostFrom = new NewPostFrom();

        model.addAttribute("topic", topic);
        model.addAttribute("skolskyRok", topic.getSection().getStudyYear());
        model.addAttribute("rok", topic.getSection().getStudyYear().getYears());
        model.addAttribute("skupina", topic.getSection());
        model.addAttribute("posts", postService.findByTopic(idVlakno));

        //model.addAttribute("currentPath", yearService.findOne(idRoku).getName() + " / " + topic.getSection().getStudyYear().getName() + " / " + topic.getSection().getName() + " / " + topicService.findOne(idVlakno).getTitle());

        /*model.addAttribute("rok", yearService.findOne(idRoku));
        model.addAttribute("skupina", sectionService.findOne(idSkupiny));
        model.addAttribute("editPost", editPostFrom);
        model.addAttribute("topic", topic);
        model.addAttribute("posts", postService.findByTopic(idVlakno));
        model.addAttribute("newPost", new NewPostFrom());*/
        return "section/topic/topic";
    }
}
