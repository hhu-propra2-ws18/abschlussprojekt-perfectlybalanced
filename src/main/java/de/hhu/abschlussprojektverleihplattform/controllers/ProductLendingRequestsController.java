package de.hhu.abschlussprojektverleihplattform.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProductLendingRequestsController {

    @GetMapping("/lendingrequests")
    public String getLendingRequestsOverview(
        Model model
    )throws Exception{
        return "productlendingrequests";
    }
}
