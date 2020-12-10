package org.forum.controller;


import org.forum.entities.StudyYear;
import org.forum.entities.Year;
import org.forum.service.SectionService;
import org.forum.service.StudyYearService;
import org.forum.service.YearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@ComponentScan
@Controller
@RequestMapping("/forum/rok/")
public class YearController {

    @Autowired
    private YearService yearService;

    @Autowired
    private StudyYearService studyYearService;

    @Autowired
    private SectionService sectionService;

    /** STUDY YEAR OVERVIEW **/
    @RequestMapping("{idRoku}")
    public String getStudyYearsFromYear(@PathVariable int idRoku,
                                           Model model) {

        model.addAttribute("currentPath", yearService.findOne(idRoku).getName() + " / ");
        model.addAttribute("skolskeRoky", yearService.findOne(idRoku).getStudyYears());
        model.addAttribute("roky", yearService.findAll());
        model.addAttribute("rok", yearService.findOne(idRoku));
        model.addAttribute("idRoku", idRoku);

        return "forum/forumRocniky";
    }
}
