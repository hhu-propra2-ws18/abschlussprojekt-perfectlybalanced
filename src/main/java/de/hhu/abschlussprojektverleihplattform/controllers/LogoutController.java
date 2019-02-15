package de.hhu.abschlussprojektverleihplattform.controllers;

import de.hhu.abschlussprojektverleihplattform.repository.UserRepository;
import de.hhu.abschlussprojektverleihplattform.service.CookieUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LogoutController {

    public static final String logoutid="4833723405235982357";

    @Autowired
    CookieUserService cookieUserService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {

        response.addCookie(new Cookie(CookieUserService.cookieName,logoutid));
        return "redirect:/login";
    }

}
