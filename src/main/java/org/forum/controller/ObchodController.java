package org.forum.controller;

import org.forum.entities.user.User;
import org.forum.newform.NewPostFrom;
import org.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ComponentScan
@Controller
@RequestMapping("/obchod/")
public class ObchodController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/unlock1")
    public String unlock1(Model model) {

        Collection<SimpleGrantedAuthority> oldAuthorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_QUICK");
        List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<SimpleGrantedAuthority>();
        updatedAuthorities.add(authority);
        updatedAuthorities.addAll(oldAuthorities);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
                        SecurityContextHolder.getContext().getAuthentication().getCredentials(),
                        updatedAuthorities)
        );

        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<String> list = user.getRoleList();
        String str_list = "";

        list.add("QUICK");
        str_list = user.roleFromListToString(list);
        System.out.println(str_list);
        user.setRole(str_list);
        userService.save(user);

        return "redirect:/obchod";
    }

    @RequestMapping(value = "/unlock2")
    public String unlock2(Model model) {

        Collection<SimpleGrantedAuthority> oldAuthorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_TEXT");
        List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<SimpleGrantedAuthority>();
        updatedAuthorities.add(authority);
        updatedAuthorities.addAll(oldAuthorities);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
                        SecurityContextHolder.getContext().getAuthentication().getCredentials(),
                        updatedAuthorities)
        );

        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<String> list = user.getRoleList();
        String str_list = "";

        list.add("TEXT");
        str_list = user.roleFromListToString(list);
        System.out.println(str_list);
        user.setRole(str_list);
        userService.save(user);

        return "redirect:/obchod";
    }

    @RequestMapping(value = "/unlock3")
    public String unlock3(Model model) {

        Collection<SimpleGrantedAuthority> oldAuthorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_NOTIF");
        List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<SimpleGrantedAuthority>();
        updatedAuthorities.add(authority);
        updatedAuthorities.addAll(oldAuthorities);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
                        SecurityContextHolder.getContext().getAuthentication().getCredentials(),
                        updatedAuthorities)
        );

        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<String> list = user.getRoleList();
        String str_list = "";

        list.add("NOTIF");
        str_list = user.roleFromListToString(list);
        System.out.println(str_list);
        user.setRole(str_list);
        userService.save(user);

        return "redirect:/obchod";
    }

    @RequestMapping(value = "/unlock4")
    public String unlock4(Model model,
                          @Valid @ModelAttribute("newPost") NewPostFrom newPrispevok,
                          BindingResult result) {

        if(result.hasErrors()) {
            model.addAttribute("user", userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
            return "section/topic/topic";
        }

        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        user.setUsername(newPrispevok.getContent());
        userService.save(user);

        Collection<SimpleGrantedAuthority> nowAuthorities =(Collection<SimpleGrantedAuthority>)SecurityContextHolder
                .getContext().getAuthentication().getAuthorities();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(newPrispevok.getContent(), SecurityContextHolder.getContext().getAuthentication().getCredentials(), nowAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/obchod";
    }
}
