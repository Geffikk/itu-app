package org.forum.controller;

import org.forum.entities.*;
import org.forum.entities.user.User;
import org.forum.newform.NewPostFrom;
import org.forum.newform.NewTopicForm;
import org.forum.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

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



    @RequestMapping(value = "{idVlakno}")
    public String getTopicById(@PathVariable int idVlakno, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Topic topic = topicService.findOne(idVlakno);
        NewPostFrom editPostFrom = new NewPostFrom();

        if(authentication.getName().equals("anonymousUser")){
            model.addAttribute("oblubeneVlakna", null);
            User user1 = new User();
            List<Topic> listOfTopics = new ArrayList<>();
            listOfTopics.add(new Topic());
            user1.setFavoriteTopics(listOfTopics);
            model.addAttribute("uzivatel", user1);

        }
        else{
            User user = userService.findByUsername(authentication.getName());
            model.addAttribute("oblubeneVlakna", user.getFavoriteTopics());
            model.addAttribute("uzivatel", user);
        }

        model.addAttribute("currentPath", topic.getSection().getStudyYear().getYear().getName() + "/" + topic.getSection().getStudyYear().getName() + "/" + topic.getSection().getName() + "/" + topic.getTitle());
        model.addAttribute("skolskyRok", topic.getSection().getStudyYear());
        model.addAttribute("rok", topic.getSection().getStudyYear().getYear());
        model.addAttribute("skupina", topic.getSection());
        model.addAttribute("editPost", editPostFrom);
        model.addAttribute("topic", topic);
        model.addAttribute("posts", postService.findByTopic(idVlakno));
        model.addAttribute("newPost", new NewPostFrom());
        try {
            model.addAttribute("user", userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        } catch (Exception e) {
            model.addAttribute("user", null);
        }
        model.addAttribute("stringIdTopic", String.valueOf(topic.getId()));
        return "section/topic/topic";
    }


    /** PRIDAJ VLAKNO K OBLUBENYM **/
    @RequestMapping(value = "{idVlakno}/addToFav")
    public String addTopicToFavorite(@PathVariable int idVlakno, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = new User();

        if(authentication.getName().equals("anonymousUser")){
            model.addAttribute("oblubeneVlakna", null);
            User user1 = new User();
            List<Topic> listOfTopics = new ArrayList<>();
            listOfTopics.add(new Topic());
            user1.setFavoriteTopics(listOfTopics);
            model.addAttribute("uzivatel", user1);
        }
        else{
            user = userService.findByUsername(authentication.getName());
            model.addAttribute("oblubeneVlakna", user.getFavoriteTopics());
            model.addAttribute("uzivatel", user);
        }

        Topic topic = topicService.findOne(idVlakno);
        NewPostFrom editPostFrom = new NewPostFrom();

        user.addFavoriteTopic(topic);
        userService.save(user);

        model.addAttribute("aktualnyRocnik", topic.getSection().getStudyYear());
        model.addAttribute("aktualnyRok", topic.getSection().getStudyYear().getYear());
        model.addAttribute("aktualnaSkupina", topic.getSection());

        model.addAttribute("editPost", editPostFrom);
        model.addAttribute("currentPath", topic.getSection().getStudyYear().getYear().getName() + " / " + topic.getSection().getStudyYear().getName() + " / " + topic.getSection().getName() + " / " + topic.getTitle());
        model.addAttribute("topic", topic);
        model.addAttribute("posts", postService.findByTopic(idVlakno));
        model.addAttribute("newPost", new NewPostFrom());
        return "redirect:/forum/rok/" + topic.getSection().getStudyYear().getYear().getId() + "/rocnik/" + topic.getSection().getStudyYear().getId() + "/skupina/" + topic.getSection().getId() + "/vlakno/" + topic.getId();
    }

    /** ODOBER VLAKNO Z OBLUBENYCH **/
    @RequestMapping(value = "{idVlakno}/deleteFromFav")
    public String deleteTopicFromFavorite(@PathVariable int idVlakno, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = new User();

        if(authentication.getName().equals("anonymousUser")){
            model.addAttribute("oblubeneVlakna", null);
            User tempUser = new User();
            tempUser.setUsername("ANONYMOUS");
            model.addAttribute("uzivatel", tempUser);
        }
        else{
            user = userService.findByUsername(authentication.getName());
            model.addAttribute("oblubeneVlakna", user.getFavoriteTopics());
            model.addAttribute("uzivatel", user);
        }

        Topic topic = topicService.findOne(idVlakno);
        NewPostFrom editPostFrom = new NewPostFrom();

        user.getFavoriteTopics().remove(topic);
        userService.save(user);

        model.addAttribute("aktualnyRocnik", topic.getSection().getStudyYear());
        model.addAttribute("aktualnyRok", topic.getSection().getStudyYear().getYear());
        model.addAttribute("aktualnaSkupina", topic.getSection());

        model.addAttribute("editPost", editPostFrom);
        model.addAttribute("currentPath", topic.getSection().getStudyYear().getYear().getName() + "/" + topic.getSection().getStudyYear().getName() + "/" + topic.getSection().getName() + "/" + topic.getTitle());
        model.addAttribute("topic", topic);
        model.addAttribute("posts", postService.findByTopic(idVlakno));
        model.addAttribute("newPost", new NewPostFrom());
        return "redirect:/forum/rok/" + topic.getSection().getStudyYear().getYear().getId() + "/rocnik/" + topic.getSection().getStudyYear().getId() + "/skupina/" + topic.getSection().getId() + "/vlakno/" + topic.getId();
    }

    /** PRECITAJ CELE VLAKNO **/
    @RequestMapping(value = "{idVlakno}/readWhole")
    public String makeTopicRead(@PathVariable int idVlakno, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.findByUsername(authentication.getName());
        Topic topic = topicService.findOne(idVlakno);


        if(user.getReadTopics().equals("")){
            user.setReadTopics(user.getReadTopics() + String.valueOf(topic.getId()));
        }
        else{
            user.setReadTopics(user.getReadTopics() + "," + String.valueOf(topic.getId()));
        }


        List<Post> listPrispevkov = postService.findByTopic(topic);
        for(Post post : listPrispevkov){
            post.setRead(true);
            postService.save(post);
        }
        model.addAttribute("stringIdTopic", String.valueOf(topic.getId()));
        return "redirect:/forum/rok/" + topic.getSection().getStudyYear().getYear().getId() + "/rocnik/" + topic.getSection().getStudyYear().getId() + "/skupina/" + topic.getSection().getId() + "/vlakno/" + topic.getId();
    }

    /** NOVE VLAKNO V SKUPINE **/
    @RequestMapping(value = "new", method = RequestMethod.GET)
    public String getNewTopicForm(@PathVariable int idSkupiny, Model model) {

        Section section = sectionService.findOne(idSkupiny);
        model.addAttribute("rok", section.getStudyYear().getYear());
        model.addAttribute("skolskyRok", section.getStudyYear());
        model.addAttribute("skupina", section);
        model.addAttribute("newTopic", new NewTopicForm());

        return "section/topic/new_topic_form";
    }


    /** EDIT POSTU V TOPICU **/
    @RequestMapping(value = "{idVlakna}/edit/{idPrispevku}", method = RequestMethod.GET)
    public String editPost(@PathVariable int idPrispevku,
                           @PathVariable int idVlakna,
                       Authentication authentication,
                       @Valid @ModelAttribute("editPost") NewPostFrom editPost,
                       Model model) {

        Post post = postService.findOne(idPrispevku);
        Topic topic = topicService.findOne(idVlakna);

        editPost.setContent(post.getContent());
        model.addAttribute("rok", topic.getSection().getStudyYear().getYear());
        model.addAttribute("skolskyRok", topic.getSection().getStudyYear());
        model.addAttribute("skupina", topic.getSection());
        model.addAttribute("editPost", editPost);
        model.addAttribute("idPost", idPrispevku);
        model.addAttribute("topic", topic);
        model.addAttribute("posts", postService.findByTopic(idVlakna));

        return "section/topic/edit_post";
    }

    /** UROB ZMENY V POSTE A ULOZ**/
    @RequestMapping(value = "{idVlakna}/save/{idPrispevku}", method = RequestMethod.POST)
    public String editPost(@Valid @ModelAttribute("editPost") NewPostFrom editPost,
                          BindingResult result,
                          @PathVariable int idVlakna,
                          @PathVariable int idPrispevku,
                          Model model) {
        Topic topic = topicService.findOne(idVlakna);

        if(result.hasErrors()) {
            model.addAttribute("topic", topic);
            model.addAttribute("posts", postService.findByTopic(idVlakna));
            return "section/topic/topic";
        }
        Post postEdited = postService.findOne(idPrispevku);
        postEdited.setContent(editPost.getContent());
        postService.save(postEdited);

        model.asMap().clear();
        return "redirect:/forum/rok/" + topic.getSection().getStudyYear().getYear().getId() + "/rocnik/" + topic.getSection().getStudyYear().getId() + "/skupina/" + topic.getSection().getId() + "/vlakno/" + topic.getId();
    }

    @RequestMapping(value = "{idTopic}", method = RequestMethod.POST)
    public String addPost(@Valid @ModelAttribute("newPost") NewPostFrom newPrispevok,
                          BindingResult result,
                          Authentication authentication,
                          @PathVariable int idRoku,
                          @PathVariable int idRocnik,
                          @PathVariable int idSkupiny,
                          @PathVariable Integer idTopic,
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

        /* NOTIFICATIONS */
        List<User> users = userService.findAll();
        List<String> list_of_not_id = new ArrayList<>();
        String str_list_of_not_id = "";

        for (User user : users) {
            for(String notification : user.getNotificationList()) {
                if(newPrispevok.getContent().indexOf(notification) == 0) {
                    list_of_not_id = user.getNotificationListId();
                    list_of_not_id.add(idTopic.toString());
                    str_list_of_not_id = user.notificationFromListToStringId(list_of_not_id);
                    user.setNotifications_id(str_list_of_not_id);
                    userService.save(user);
                }
            }
        }

        model.asMap().clear();
        return "redirect:/forum/rok/"+idRoku+"/rocnik/"+idRocnik+"/skupina/"+idSkupiny+"/vlakno/" + idTopic;
    }

    @RequestMapping(value = "new", method = RequestMethod.POST)
    public String addNewTopic(@Valid @ModelAttribute("newTopic") NewTopicForm newTopic,
                                        BindingResult result,
                                        @PathVariable int idSkupiny,
                                        Authentication authentication,
                                        Model model) {

        Section section = sectionService.findOne(idSkupiny);

        Topic topic = new Topic();
        topic.setUser(userService.findByUsername(authentication.getName()));
        topic.setTitle(newTopic.getTitle());
        topic.setContent(newTopic.getContent());
        topic.setSection(section);
        topicService.save(topic);

        int temp_id = topic.getId();

        /* NOTIFICATIONS */
        List<User> users = userService.findAll();
        List<String> list_of_not_id = new ArrayList<>();
        String str_list_of_not_id = "";

        for (User user : users) {
            for(String notification : user.getNotificationList()) {
                if(newTopic.getContent().indexOf(notification) == 0) {
                    list_of_not_id = user.getNotificationListId();
                    list_of_not_id.add(Integer.toString(temp_id));
                    str_list_of_not_id = user.notificationFromListToStringId(list_of_not_id);
                    user.setNotifications_id(str_list_of_not_id);
                    userService.save(user);
                }
                if(newTopic.getTitle().indexOf(notification) == 0) {
                    list_of_not_id = user.getNotificationListId();
                    list_of_not_id.add(Integer.toString(temp_id));
                    str_list_of_not_id = user.notificationFromListToStringId(list_of_not_id);
                    user.setNotifications_id(str_list_of_not_id);
                    userService.save(user);
                }
            }
        }

        return "redirect:/forum/rok/" + section.getStudyYear().getYear().getId() + "/rocnik/" + section.getStudyYear().getId() + "/skupina/" + section.getId() + "/vlakno/" + topic.getId();

    }

    @RequestMapping(value = "delete/{idVlakna}", method = RequestMethod.GET)
    public String delete(@PathVariable int idSkupiny,
                         @PathVariable int idVlakna,
                         Authentication authentication,
                         RedirectAttributes model) {
        Section section = sectionService.findOne(idSkupiny);
        Topic topic = topicService.findOne(idVlakna);

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
        return "redirect:/forum/rok/" + section.getStudyYear().getYear().getId() + "/rocnik/" + section.getStudyYear().getId() + "/skupina/" + section.getId();
    }

}
