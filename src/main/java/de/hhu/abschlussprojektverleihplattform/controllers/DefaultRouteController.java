package de.hhu.abschlussprojektverleihplattform.controllers;

import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DefaultRouteController {

    @Autowired
    private ProductService productService;


    @GetMapping("/")
    public String getDefaultRouteOverviewOfProducts(Model model){

        //TODO: redirect if user is not logged in
        // Startseite für alle
        List<ProductEntity> products = productService.showAll();

        model.addAttribute("products", products);

        return "productoverview";
    }
}
