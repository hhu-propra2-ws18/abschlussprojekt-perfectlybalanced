package de.hhu.abschlussprojektverleihplattform.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserProfileController {

    @GetMapping
    public String getProfile(Model model){

        //this page should only be available to logged in users.
        //otherwise it would redirect to login page

        //TODO: make it redirect to login page for not logged in users

        return "profile";
    }
}
