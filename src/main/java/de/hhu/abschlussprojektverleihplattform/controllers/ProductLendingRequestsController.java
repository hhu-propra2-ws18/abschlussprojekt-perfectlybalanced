package de.hhu.abschlussprojektverleihplattform.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductLendingRequestsController {

    @GetMapping("/lendingrequests")
    public String getLendingRequestsOverview(Model model){
        //TODO: determine logged in user,
        //otherwise redirect to login page

        return "productlendingrequests";
    }
}
