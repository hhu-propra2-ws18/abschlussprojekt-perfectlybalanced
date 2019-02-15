package de.hhu.abschlussprojektverleihplattform.controllers;

import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.repository.ProductRepository;
import de.hhu.abschlussprojektverleihplattform.repository.UserRepository;
import de.hhu.abschlussprojektverleihplattform.service.CookieUserService;
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

    @Autowired
    CookieUserService cookieUserService;

    @GetMapping("/addproduct")
    public String getAddProduct(Model model, HttpServletRequest httpServletRequest) {
        UserEntity user;

        try {
            user = cookieUserService.getUserFromRequest(httpServletRequest);
        }catch (Exception e){
            return "login";
        }

        return "addproduct";
    }

    @GetMapping("/editproduct")
    public String getEditProduct(Model model, HttpServletRequest httpServletRequest) {

        UserEntity user;

        try {
            cookieUserService.getUserFromRequest(httpServletRequest);
        }catch (Exception e){
            return "login";
        }

        return "editproduct";
    }

    @GetMapping("/removeproduct")
    public String getRemoveProduct(Model model, HttpServletRequest httpServletRequest) {
        UserEntity user;

        try {
            cookieUserService.getUserFromRequest(httpServletRequest);
        }catch (Exception e){
            return "login";
        }

        return "removeproduct";
    }

    @GetMapping("/productdetail/{id}")
    public String getProductDetails(Model model, Long id, HttpServletRequest httpServletRequest) {
        UserEntity user;

        try {
            cookieUserService.getUserFromRequest(httpServletRequest);
        }catch (Exception e){
            return "login";
        }

        ProductEntity product = productRepository.getProductById(id);
        model.addAttribute("product", product);

        return "productdetailedview";
    }
}

