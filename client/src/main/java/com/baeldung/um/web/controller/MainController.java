package com.baeldung.um.web.controller;

import com.baeldung.um.web.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/user")
class MainController {

    @Autowired
    private OAuth2RestTemplate restTemplate;

    //

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView list() {
        final List<User> users = restTemplate.getForObject("http://localhost:8080/resource-server/api/users/", List.class);
        return new ModelAndView("list", "users", users);
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(User user) {
        final User created = restTemplate.postForObject("http://localhost:8080/resource-server/api/users/", user, User.class);
        System.out.println(created);
        return "redirect:/user";
    }

    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(@ModelAttribute final User user) {
        return "form";
    }

}
