package org.forum.controller;


import org.forum.entities.StudyYear;
import org.forum.entities.Year;
import org.forum.entities.user.User;
import org.forum.newform.NewPostFrom;
import org.forum.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@ComponentScan
@Controller
@RequestMapping("/forum/rok/{idRoku}/rocnik/")
public class StudyYearController {


    @Autowired
    private StudyYearService studyYearService;

    @Autowired
    private SectionService sectionService;

    @Autowired
    private YearService yearService;

    @Autowired
    private UserService userService;

    @Autowired
    private TopicService topicService;

    /** STUDY YEAR OVERVIEW **/
    @RequestMapping("{idRocnik}")
    public String getSectionsFromStudyYear(@PathVariable int idRoku, @PathVariable int idRocnik,
                                       Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        StudyYear studyYear = studyYearService.findOne(idRocnik);

        if(authentication.getName().equals("anonymousUser")){
            model.addAttribute("oblubeneVlakna", null);
        }
        else{
            User user = userService.findByUsername(authentication.getName());
            model.addAttribute("oblubeneVlakna", user.getFavoriteTopics());
        }

        model.addAttribute("aktualneTemy", topicService.findRecent());
        model.addAttribute("rok", studyYear.getYear());
        model.addAttribute("currentPath", studyYear.getYear().getName() + "/" + studyYear.getName() + "/");
        model.addAttribute("skolskyRok", studyYear);
        model.addAttribute("skolskeRoky", studyYearService.findByYear(studyYear.getYear()));
        model.addAttribute("skupiny", sectionService.findByStudyYear(studyYear));
        model.addAttribute("idSkolskehoRoku", idRocnik);
        try {
            model.addAttribute("user", userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        } catch (Exception e) {
            model.addAttribute("user", null);
        }
        NewPostFrom searchPost = new NewPostFrom();
        model.addAttribute("searchPost", searchPost);

        return "forum/forumSkupiny";
    }
}
