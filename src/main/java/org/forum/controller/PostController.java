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

    @RequestMapping(value = "/delete/{idPrispevok}", method = RequestMethod.GET)
    public String delete(@PathVariable int idVlakno,@PathVariable int idPrispevok,
                         Authentication authentication,
                         RedirectAttributes model) {
        Post post = postService.findOne(idPrispevok);
        Topic topic = topicService.findOne(idVlakno);

        if (post == null || authentication == null || authentication.getName() == null) {
            return "redirect:/";
        }

        postService.delete(post);

        model.addFlashAttribute("message", "post.successfully.deleted");
        return "redirect:/forum/rok/" + topic.getSection().getStudyYear().getYear().getId() + "/rocnik/" + topic.getSection().getStudyYear().getId() + "/skupina/" + topic.getSection().getId() + "/vlakno/" + topic.getId() ;
    }

    @RequestMapping(value = "/pridaj1/{idPrispevku}")
    private String incrementRankingOfPost(@PathVariable int idVlakno,
                                          @PathVariable int idPrispevku, Model model, Authentication authentication){
        Post post = postService.findOne(idPrispevku);
        Topic topic = topicService.findOne(idVlakno);
        User user = userService.findByUsername(authentication.getName());

        post.addUserToLiked(user);
        post.setRanking(post.getRanking()+1);
        postService.save(post);

        return "redirect:/forum/rok/" + topic.getSection().getStudyYear().getYear().getId() + "/rocnik/" + topic.getSection().getStudyYear().getId() + "/skupina/" + topic.getSection().getId() + "/vlakno/" + topic.getId();
    }


    @RequestMapping(value = "/odcitaj1/{idPrispevku}")
    private String decrementRankingOfPost( @PathVariable int idVlakno,
                                          @PathVariable int idPrispevku, Authentication authentication){
        Post post = postService.findOne(idPrispevku);
        Topic topic = topicService.findOne(idVlakno);
        User user = userService.findByUsername(authentication.getName());


        post.addUserToLiked(user);
        post.setRanking(post.getRanking()-1);
        postService.save(post);

        return "redirect:/forum/rok/" + topic.getSection().getStudyYear().getYear().getId() + "/rocnik/" + topic.getSection().getStudyYear().getId() + "/skupina/" + topic.getSection().getId() + "/vlakno/" + topic.getId();
    }

    @RequestMapping(value = "/{idPrispevku}/zavri")
    private String closeTopic(@PathVariable int idVlakno,
                              @PathVariable int idPrispevku){

        Topic topic = topicService.findOne(idVlakno);
        Post post = postService.findOne(idPrispevku);
        User creatorOfPost =  post.getUser();

        //Pridaj peniaze zakladatelovi prispevku
        creatorOfPost.setMoney(post.getUser().getMoney() + 1000);

        //Pridaj body zakladatelovi prispevku
        creatorOfPost.setPoints(post.getUser().getPoints() + 1000);

        //Nastavit prispevok ako riesenie
        post.setSolution(true);

        //zavriet topic
        topic.setClosed(true);

        //ulozit do db
        userService.save(creatorOfPost);
        topicService.save(topic);
        postService.save(post);

        return "redirect:/forum/rok/" + topic.getSection().getStudyYear().getYear().getId() + "/rocnik/" + topic.getSection().getStudyYear().getId() + "/skupina/" + topic.getSection().getId() + "/vlakno/" + topic.getId();
    }
}
