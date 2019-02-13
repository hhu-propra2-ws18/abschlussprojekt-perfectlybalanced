package de.hhu.abschlussprojektverleihplattform.controllers;

import de.hhu.abschlussprojektverleihplattform.database.DatabaseManager;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class RegistryController {

    @GetMapping("/register")
    public String getRegisterPage(){
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

        if(username.isEmpty() || password.isEmpty() || vorname.isEmpty() || nachname.isEmpty() ||email.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"some fields are empty");
        }

        //TODO: validate email and other fields

        return "redirect:/";
    }
}
