package de.hhu.abschlussprojektverleihplattform.controllers;

import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.repository.ProductRepository;
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

    @Autowired
    ProductRepository productRepository;


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

    @GetMapping("/productdetail/{id}")
    public String getProductDetails(Model model, Long id, HttpServletRequest httpServletRequest) {
        ProductEntity product = productRepository.getProductById(id);
        if(product != null) {
            model.addAttribute("product", product);
        }
        return "productdetailedview";
    }

    //TODO: GetMappings+Views to see all Product and start a request
    // (Request itself is in ProductLendingRequestController)
}

