package de.hhu.abschlussprojektverleihplattform.controllers;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class RegistryController {

    @GetMapping("/register")
    public String getRegisterPage(Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/profile";
        }

        model.addAttribute("user", new UserEntity());
        return "registry";
    }

    @PostMapping("/register")
    public String postRegisterUser(
            @RequestParam(value="username")String username,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "vorname") String vorname,
            @RequestParam(value = "nachname") String nachname,
            @RequestParam(value = "email") String email
    ){
        //TODO: check supplied info for duplication with existing user.
        //TODO: insert new user into db
        //TODO: login the new user
        //TODO: throw exception if fields are empty
        //TODO: validate email and other fields

        return "redirect:/";
    }
}
