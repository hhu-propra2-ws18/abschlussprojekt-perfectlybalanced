package de.hhu.abschlussprojektverleihplattform.controllers;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String getLogin(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/profile";
        }

        return "login";
    }

    /*
    @PostMapping("/login")
    public String postLogin(@RequestParam(value="username")String username, @RequestParam(value="password")String password){
        //TODO: actually check user credentials submitted
        return "redirect:/profile";
    }
    */
}
