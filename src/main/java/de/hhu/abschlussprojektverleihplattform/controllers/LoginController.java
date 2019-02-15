package de.hhu.abschlussprojektverleihplattform.controllers;

import de.hhu.abschlussprojektverleihplattform.repository.UserRepository;
import de.hhu.abschlussprojektverleihplattform.service.CookieUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @Autowired
    CookieUserService cookieUserService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/login")
    public String getLogin(HttpServletRequest httpServletRequest){
        try {
            cookieUserService.getUserFromRequest(httpServletRequest);
        }catch (Exception e){
            return "login";
        }
        return "redirect:/";
    }

    @PostMapping("/login")
    public String postLogin(@RequestParam(value="username")String username,
                            @RequestParam(value="password")String password,
                            HttpServletRequest httpServletRequest){
        //TODO: actually check user credentials submitted
        return "redirect:/profile";
    }

}
