package de.hhu.abschlussprojektverleihplattform.controllers;

import de.hhu.abschlussprojektverleihplattform.repository.UserRepository;
import de.hhu.abschlussprojektverleihplattform.service.CookieUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @Autowired
    CookieUserService cookieUserService;

    @Autowired
    UserRepository userRepository;




    @GetMapping("/login")
    public String getLogin(
        HttpServletRequest httpServletRequest,
        HttpServletResponse response
    ) throws Exception{

        try {
            cookieUserService.getUserFromRequest(httpServletRequest).getUsername();
            return "redirect:/";
        } catch (Exception e){
            return "login";
        }
    }

    @PostMapping("/login")
    public String postLogin(@RequestParam(value="username")String username,
                            @RequestParam(value="password")String password,
                            HttpServletResponse response){
        //TODO: actually check user credentials submitted
        try {
            userRepository.findByUsername(username).getUsername();
        } catch (Exception e) {
            return "login";
        }
        response.addCookie(
                new Cookie(
                        CookieUserService.cookieName,
                        userRepository.findByUsername(username).getUserId()+""
                )
        );

        //cookies and redirects dont work well together
        return "profile";
    }

}
