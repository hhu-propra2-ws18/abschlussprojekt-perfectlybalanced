package de.hhu.abschlussprojektverleihplattform.controllers;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.repository.UserRepository;
import de.hhu.abschlussprojektverleihplattform.service.CookieUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserProfileController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CookieUserService cookieUserService;

    @GetMapping("/profile")
    public String getProfile(Model model, HttpServletRequest httpServletRequest){

        //this page should only be available to logged in users.
        //otherwise it would redirect to login page

        UserEntity user;

        //TODO: make it redirect to login page for not logged in users
        try {
            user = cookieUserService.getUserFromRequest(httpServletRequest);
        }catch (Exception e){
            return "login";
        }

        model.addAttribute("user", user);

        return "profile";
    }
}
