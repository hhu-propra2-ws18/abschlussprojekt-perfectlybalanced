package de.hhu.abschlussprojektverleihplattform.controllers;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserProfileController {

    @GetMapping("/profile")
    public String getProfile(Model model, Authentication auth) {

        //this page should only be available to logged in users.
        //otherwise it would redirect to login page

        UserEntity user = (UserEntity) auth.getPrincipal();
        model.addAttribute("user", user);

        //TODO: make it redirect to login page for not logged in users

        return "profile";
    }
}
