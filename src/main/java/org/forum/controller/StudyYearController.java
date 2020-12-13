package org.forum.controller;


import org.forum.entities.StudyYear;
import org.forum.entities.Year;
import org.forum.entities.user.User;
import org.forum.service.SectionService;
import org.forum.service.StudyYearService;
import org.forum.service.UserService;
import org.forum.service.YearService;
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

    /** STUDY YEAR OVERVIEW **/
    @RequestMapping("{idRocnik}")
    public String getSectionsFromStudyYear(@PathVariable int idRoku, @PathVariable int idRocnik,
                                       Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Year year = yearService.findOne(idRoku);
        StudyYear studyYear = studyYearService.findOne(idRocnik);

        if(authentication.getName().equals("anonymousUser")){
            model.addAttribute("oblubeneVlakna", null);
        }
        else{
            User user = userService.findByUsername(authentication.getName());
            model.addAttribute("oblubeneVlakna", user.getFavoriteTopics());
        }

        model.addAttribute("rok", year);
        model.addAttribute("currentPath", year.getName() + " / " + studyYear.getName() + " / ");
        model.addAttribute("skolskyRok", studyYear);
        model.addAttribute("skolskeRoky", studyYearService.findByYear(year));
        model.addAttribute("skupiny", sectionService.findByStudyYear(studyYear));
        model.addAttribute("idSkolskehoRoku", idRocnik);
        try {
            model.addAttribute("user", userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        } catch (Exception e) {
            model.addAttribute("user", null);
        }

        return "forum/forumSkupiny";
    }
}
