package org.forum.controller;

import org.forum.entities.*;
import org.forum.entities.user.User;
import org.forum.newform.NewPostFrom;
import org.forum.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@ComponentScan
@Controller
@RequestMapping(value = "/forum/rok/{idRoku}/rocnik/{idRocnik}/skupina/{idSkupiny}/vlakno/{idVlakno}/prispevok")
public class PostController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private YearService yearService;

    @Autowired
    private StudyYearService studyYearService;

    @Autowired
    private SectionService sectionService;

    @Autowired
    private TopicService topicService;

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable int id,
                         Authentication authentication,
                         RedirectAttributes model) {
        Post post = postService.findOne(id);

        if (post == null || authentication == null || authentication.getName() == null) {
            return "redirect:/";
        }

        postService.delete(post);

        model.addFlashAttribute("message", "post.successfully.deleted");
        return "redirect:/topic/" + post.getTopic().getId();
    }

    @RequestMapping(value = "/pridaj1/{idPrispevku}")
    private String incrementRankingOfPost(@PathVariable int idRoku, @PathVariable int idRocnik,
                                          @PathVariable int idSkupiny, @PathVariable int idVlakno,
                                          @PathVariable int idPrispevku, Model model, Authentication authentication){
        Post post = postService.findOne(idPrispevku);
        Year year = yearService.findOne(idRoku);
        StudyYear studyYear = studyYearService.findOne(idRocnik);
        Section section = sectionService.findOne(idSkupiny);
        Topic topic = topicService.findOne(idVlakno);
        User user = userService.findByUsername(authentication.getName());

        post.addUserToLiked(user);
        post.setRanking(post.getRanking()+1);
        postService.save(post);

        return "redirect:/forum/rok/" + year.getId() + "/rocnik/" + studyYear.getId() + "/skupina/" + section.getId() + "/vlakno/" + topic.getId();
    }


    @RequestMapping(value = "/odcitaj1/{idPrispevku}")
    private String decrementRankingOfPost(@PathVariable int idRoku, @PathVariable int idRocnik,
                                          @PathVariable int idSkupiny, @PathVariable int idVlakno,
                                          @PathVariable int idPrispevku, Model model, Authentication authentication){
        Post post = postService.findOne(idPrispevku);
        Year year = yearService.findOne(idRoku);
        StudyYear studyYear = studyYearService.findOne(idRocnik);
        Section section = sectionService.findOne(idSkupiny);
        Topic topic = topicService.findOne(idVlakno);
        User user = userService.findByUsername(authentication.getName());


        post.addUserToLiked(user);
        post.setRanking(post.getRanking()-1);
        postService.save(post);




        return "redirect:/forum/rok/" + year.getId() + "/rocnik/" + studyYear.getId() + "/skupina/" + section.getId() + "/vlakno/" + topic.getId();
    }
}
