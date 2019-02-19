package de.hhu.abschlussprojektverleihplattform.controllers;

import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.repository.ProductRepository;
import de.hhu.abschlussprojektverleihplattform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProductController {

    // PostMapping fehlt noch

    final UserRepository userRepository;

    final ProductRepository productRepository;

    @Autowired
    public ProductController(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }


    @GetMapping("/addproduct")
    public String getAddProduct(Model model) {
        return "addproduct";
    }

    @GetMapping("/editproduct")
    public String getEditProduct(Model model) {
        return "editproduct";
    }

    @GetMapping("/removeproduct")
    public String getRemoveProduct(Model model) {
        return "removeproduct";
    }

    @GetMapping("/productdetail/{id}")
    public String getProductDetails( Model model, @PathVariable Long id) {
        ProductEntity product = productRepository.getProductById(id);
        if(product != null) {
            model.addAttribute("product", product);
            return "productdetailedview";
        }
        return "redirect:/";
    }

    @GetMapping("/myproducts")
    public String getMyProducts(Model model) {
        return "myproducts";
    }
}

