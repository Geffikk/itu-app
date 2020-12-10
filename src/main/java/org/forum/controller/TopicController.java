package org.forum.controller;

import org.forum.entities.Post;
import org.forum.entities.Topic;
import org.forum.newform.NewPostFrom;
import org.forum.newform.NewTopicForm;
import org.forum.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@ComponentScan
@Controller
@RequestMapping("/forum/rok/{idRoku}/rocnik/{idRocnik}/skupina/{idSkupiny}/vlakno/")
public class TopicController {

    @Autowired
    private PostService postService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private SectionService sectionService;

    @Autowired
    private UserService userService;

    @Autowired
    private StudyYearService studyYearService;

    @Autowired
    private YearService yearService;




    @RequestMapping(value = "{idVlakno}")
    public String getTopicById(@PathVariable int idRoku, @PathVariable int idRocnik, @PathVariable int idSkupiny, @PathVariable int idVlakno, Model model) {

        Topic topic = topicService.findOne(idVlakno);

        NewPostFrom editPostFrom = new NewPostFrom();


        model.addAttribute("currentPath", yearService.findOne(idRoku).getName() + " / " + studyYearService.findOne(idRocnik).getName() + " / " + sectionService.findOne(idSkupiny).getName() + " / " + topicService.findOne(idVlakno).getTitle());
        model.addAttribute("skolskyRok", studyYearService.findOne(idRocnik));
        model.addAttribute("rok", yearService.findOne(idRoku));
        model.addAttribute("skupina", sectionService.findOne(idSkupiny));
        model.addAttribute("editPost", editPostFrom);
        model.addAttribute("topic", topic);
        model.addAttribute("posts", postService.findByTopic(idVlakno));
        model.addAttribute("newPost", new NewPostFrom());
        return "section/topic/topic";
    }



    @RequestMapping(value = "{idVlakno}/add")
    public String addTopicToFavorite(@PathVariable int idRocnik, @PathVariable int idRoku, @PathVariable int idSkupiny, @PathVariable int idVlakno, Model model) {

        Topic topic = topicService.findOne(idVlakno);
        NewPostFrom editPostFrom = new NewPostFrom();

        model.addAttribute("aktualnyRocnik", studyYearService.findOne(idRocnik));
        model.addAttribute("aktualnyRok", yearService.findOne(idRoku));
        model.addAttribute("aktualnaSkupina", sectionService.findOne(idSkupiny));
        // TODO: 10/12/2020
        // podla authorization.name zistit meno kto je prihlaseny najst uzivatela a pridat toto vlakno
        // do jeho oblubenych a redirect na tu istu stranku


        model.addAttribute("editPost", editPostFrom);
        model.addAttribute("currentPath", studyYearService.findOne(idRocnik).getName() + " / " + yearService.findOne(idRoku).getName() + " / " + sectionService.findOne(idSkupiny).getName() + " / " + topicService.findOne(idVlakno).getTitle());
        model.addAttribute("topic", topic);
        model.addAttribute("posts", postService.findByTopic(idVlakno));
        model.addAttribute("newPost", new NewPostFrom());
        return "section/topic/topic";
    }





    @RequestMapping(value = "new", method = RequestMethod.GET)
    public String getNewTopicForm(Model model) {
        model.addAttribute("newTopic", new NewTopicForm());
        model.addAttribute("sections", sectionService.findAll());
        return "section/topic/new_topic_form";
    }

    @RequestMapping(value = "{idTopic}/edit/{idPost}", method = RequestMethod.GET)
    public String editPost(@PathVariable int idPost,
                           @PathVariable int idTopic,
                       Authentication authentication,
                       @Valid @ModelAttribute("editPost") NewPostFrom editPost,
                       Model model) {

        Post post = postService.findOne(idPost);
        Topic topic = topicService.findOne(idTopic);

        editPost.setContent(post.getContent());
        model.addAttribute("editPost", editPost);
        model.addAttribute("topic", topic);
        model.addAttribute("posts", postService.findByTopic(idTopic));

        return "section/topic/edit_post";
    }

    @RequestMapping(value = "{idTopic}/save/{idPost}", method = RequestMethod.POST)
    public String editPost(@Valid @ModelAttribute("editPost") NewPostFrom editPost,
                          BindingResult result,
                          @PathVariable int idTopic,
                          @PathVariable int idPost,
                          Model model) {

        if(result.hasErrors()) {
            model.addAttribute("topic", topicService.findOne(idTopic));
            model.addAttribute("posts", postService.findByTopic(idTopic));
            return "section/topic/topic";
        }
        Post postEdited = postService.findOne(idPost);
        postEdited.setContent(editPost.getContent());
        postService.save(postEdited);

        model.asMap().clear();
        return "redirect:/topic/" + idTopic;
    }

    @RequestMapping(value = "{idTopic}", method = RequestMethod.POST)
    public String addPost(@Valid @ModelAttribute("newPost") NewPostFrom newPrispevok,
                          BindingResult result,
                          Authentication authentication,
                          @PathVariable int idTopic,
                          Model model) {

        if(result.hasErrors()) {
            model.addAttribute("topic", topicService.findOne(idTopic));
            model.addAttribute("posts", postService.findByTopic(idTopic));
            return "section/topic/topic";
        }

        Post post = new Post();
        post.setContent(newPrispevok.getContent());
        post.setTopic(topicService.findOne(idTopic));
        post.setUser(userService.findByUsername(authentication.getName()));
        postService.save(post);

        model.asMap().clear();
        return "redirect:/topic/" + idTopic;
    }

    @RequestMapping(value = "new", method = RequestMethod.POST)
    public String addNewTopic(@Valid @ModelAttribute("newTopic") NewTopicForm newTopic,
                                        BindingResult result,
                                        Authentication authentication,
                                        Model model) {

        if(result.hasErrors()) {
            model.addAttribute("sections", sectionService.findAll());
            return "new_topic_form";
        }

        Topic topic = new Topic();
        topic.setUser(userService.findByUsername(authentication.getName()));
        topic.setTitle(newTopic.getTitle());
        topic.setContent(newTopic.getContent());
        topic.setSection(sectionService.findOne(newTopic.getSectionId()));
        topicService.save(topic);

        return "redirect:/topic/" + topic.getId();

    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable int id,
                         Authentication authentication,
                         RedirectAttributes model) {

        Topic topic = topicService.findOne(id);

//        if(!topic.getSection().getModeratorsUsername().contains(authentication.getName()) &&
//                !authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
//            throw new AccessDeniedException("You dont have permission for this operation !");
//        }

        if(topic == null) {
            return "redirect:/";
        }
//        if(!authentication.getName().equals(topic.getUser().getUsername()) && !topic.getSection().getModeratorsUsername().contains(authentication.getName())) {
//            throw new AccessDeniedException("You dont have permission for this operation !");
//        }

        topicService.delete(topic);

        model.addFlashAttribute("message","topic.successfully.deleted");
        return "redirect:/section/" + topic.getSection().getId();
    }

}
