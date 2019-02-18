package de.hhu.abschlussprojektverleihplattform.controllers;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.service.CookieUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProductLendingRequestsController {

    @Autowired
    CookieUserService cookieUserService;

    @GetMapping("/lendingrequests")
    public String getLendingRequestsOverview(Model model, HttpServletRequest httpServletRequest)throws Exception{
        //TODO: determine logged in user,
        //otherwise redirect to login page

        UserEntity user = cookieUserService.getUserFromRequest(httpServletRequest);
        model.addAttribute("user",user);
        return "productlendingrequests";
    }
}
