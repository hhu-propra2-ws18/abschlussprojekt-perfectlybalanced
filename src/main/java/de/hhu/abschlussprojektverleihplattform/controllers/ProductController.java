package de.hhu.abschlussprojektverleihplattform.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductController {

    // PostMapping fehlt noch

    @GetMapping("/addproduct")
    public String getAddProduct(Model model) {
        return "addproduct";
    }

    @GetMapping("/editproduct")
    public String getEditProduct(Model model) {
        // Was noch fehlt: Einträge vom Produkt in den Formularfeldern anzeigen lassen
        return "editproduct";
    }

    @GetMapping("/removeproduct")
    public String getRemoveProduct(Model model) {
        return "removeproduct";
    }

    @GetMapping("/productdetail")
    public String getProductDetails(Model model) {
        return "productdetailedview";
    }
}

