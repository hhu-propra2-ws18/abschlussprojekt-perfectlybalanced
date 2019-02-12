package de.hhu.abschlussprojektverleihplattform.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultRouteController {

    @GetMapping("/")
    public String getDefaultRouteOverviewOfProducts(Model model){

        //TODO: redirect if user is not logged in

        return "productoverview";
    }
}
