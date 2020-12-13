package org.forum.controller;

import org.forum.entities.Section;
import org.forum.entities.StudyYear;
import org.forum.entities.Year;
import org.forum.entities.user.User;
import org.forum.newform.NewSectionForm;
import org.forum.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@ComponentScan
@Controller
@RequestMapping("/forum/rok/{idRoku}/rocnik/{idRocnik}/skupina/")
public class SectionController {

    @Autowired
    private SectionService sectionService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private UserService userService;

    @Autowired
    private YearService yearService;

    @Autowired
    private StudyYearService studyYearService;

    /** SECTION OVERVIEW **/
    @RequestMapping("{idSkupiny}")
    public String getTopicsFromSection(@PathVariable int idRoku, @PathVariable int idRocnik, @PathVariable int idSkupiny,
                                       Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Section section = sectionService.findOne(idSkupiny);

        if(authentication.getName().equals("anonymousUser")){
            model.addAttribute("oblubeneVlakna", null);
        }
        else{
            User user = userService.findByUsername(authentication.getName());
            model.addAttribute("oblubeneVlakna", user.getFavoriteTopics());
        }

        model.addAttribute("currentPath", section.getStudyYear().getYear().getName() + "/" + section.getStudyYear().getName() + "/" + section.getName() + "/");
        model.addAttribute("skolskyRok", section.getStudyYear());
        model.addAttribute("roky", yearService.findAll());
        model.addAttribute("vlakna", topicService.findBySection(idSkupiny));
        model.addAttribute("rok", section.getStudyYear().getYear());
        model.addAttribute("skupiny", sectionService.findByStudyYear(section.getStudyYear()));
        model.addAttribute("skupina", section);
        model.addAttribute("skupinaId", idSkupiny);
        try {
            model.addAttribute("user", userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        } catch (Exception e) {
            model.addAttribute("user", null);
        }

        return "section/section";
    }


    @RequestMapping(value = "new", method = RequestMethod.GET)
    public String getNewSectionForm(Model model) {
        model.addAttribute("newSection", new NewSectionForm());
        return "section/new_section_form";
    }

    /** CREATE NEW SECTION AND REDIRECT TO THIS SECTION VIEW**/
    @RequestMapping(value = "new", method = RequestMethod.POST)
    public String processAndAddNewSection(
            @Valid @ModelAttribute("newSection") NewSectionForm newSkupina,
            BindingResult result,
            Authentication authentication) {

        if (result.hasErrors()) {
            return "section/new_section_form";
        }
        User user = userService.findByUsername(authentication.getName());

        Section section = new Section();
        section.setName(newSkupina.getName());


        section = sectionService.save(section);
        return "redirect:/section/" + section.getId();
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable int id,
                         Authentication authentication,
                         RedirectAttributes model) {
        User user = userService.findByUsername(authentication.getName());
        Section section = sectionService.findOne(id);
        sectionService.delete(id);

        model.addFlashAttribute("message", "section.successfully.deleted");
        return "redirect:/home";
    }




}
