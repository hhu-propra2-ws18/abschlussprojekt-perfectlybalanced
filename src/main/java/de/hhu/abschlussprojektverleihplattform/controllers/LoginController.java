package de.hhu.abschlussprojektverleihplattform.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String getLogin(){
        return "login";
    }

    @PostMapping("/login")
    public String postLogin(@RequestParam(value="username")String username, @RequestParam(value="password")String password){
        //TODO: actually check user credentials submitted
        return "redirect:/";
    }
}
