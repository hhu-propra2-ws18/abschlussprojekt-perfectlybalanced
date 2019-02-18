package de.hhu.abschlussprojektverleihplattform.controllers;

import de.hhu.abschlussprojektverleihplattform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProductController {

    // PostMapping fehlt noch

    @Autowired
    UserRepository userRepository;

    @GetMapping("/addproduct")
    public String getAddProduct(Model model, HttpServletRequest httpServletRequest) {
        return "addproduct";
    }

    @GetMapping("/editproduct")
    public String getEditProduct(Model model, HttpServletRequest httpServletRequest) {
        return "editproduct";
    }

    @GetMapping("/removeproduct")
    public String getRemoveProduct(Model model, HttpServletRequest httpServletRequest) {
        return "removeproduct";
    }

    @GetMapping("/productdetail")
    public String getProductDetails(
        Model model,
        HttpServletRequest httpServletRequest
    ) throws Exception{
        return "productdetailedview";
    }
}

