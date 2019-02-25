package de.hhu.abschlussprojektverleihplattform.controllers.product;

import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DefaultRouteController {

    private final ProductService productService;

    @Autowired
    public DefaultRouteController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String getDefaultRouteOverviewOfProducts(Model model) {
        List<ProductEntity> products = productService.showAll();

        model.addAttribute("products", products);

        return "productoverview";
    }
}
