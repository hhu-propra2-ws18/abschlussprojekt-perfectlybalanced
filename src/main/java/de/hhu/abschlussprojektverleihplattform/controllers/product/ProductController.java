package de.hhu.abschlussprojektverleihplattform.controllers.product;

import de.hhu.abschlussprojektverleihplattform.model.AddressEntity;
import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.service.ILendingService;
import de.hhu.abschlussprojektverleihplattform.service.IProductService;
import de.hhu.abschlussprojektverleihplattform.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.List;

@Controller
public class ProductController {

    private final IUserService userService;
    private final IProductService productService;
    private final ILendingService lendingService;

    @Autowired

    public ProductController(
            IUserService userService,
            IProductService productService,
            ILendingService lendingService) {
        this.userService = userService;
        this.productService = productService;
        this.lendingService = lendingService;
    }


    @ModelAttribute
    public void addAttributes(Model model, Authentication auth) {
        UserEntity authUser = (UserEntity) auth.getPrincipal();
        model.addAttribute("user", authUser);
    }


    @GetMapping("/addproduct")
    public String getAddProduct(Model model) {
        model.addAttribute("product", new ProductEntity());
        model.addAttribute("address", new AddressEntity());

        return "addproduct";
    }


    @PostMapping("/addproduct")
    public String postAddProduct(@ModelAttribute("product") @Valid ProductEntity productEntity,
        BindingResult bindingResultProduct,
        @ModelAttribute("address") @Valid AddressEntity addressEntity,
        BindingResult bindingResultAddress,
        @ModelAttribute("user") UserEntity userEntity){

        if(bindingResultProduct.hasErrors() || bindingResultAddress.hasErrors()) {
            return "addproduct";
        }

        productEntity.setLocation(addressEntity);
        productEntity.setOwner(userEntity);
        productService.addProduct(productEntity);
        return "redirect:/";
    }

    @GetMapping("/editproduct/{id}")
    public String getEditProduct(Model model,
            @PathVariable Long id,
            @ModelAttribute("user") UserEntity userEntity) {
        ProductEntity product = productService.getById(id);
        if(product != null && product.getOwner().getUserId().equals(userEntity.getUserId())) {
            model.addAttribute("product", product);
            model.addAttribute("address", product.getLocation());
            return "editproduct";
        }
        return "redirect:/myproducts";
    }

    @PostMapping("/editproduct/{id}")
    public String postEditProduct(
        @ModelAttribute("product") @Valid ProductEntity productEntity,
        BindingResult bindingResultProduct,
        @ModelAttribute("address") @Valid AddressEntity addressEntity,
        BindingResult bindingResultAddress,
        @ModelAttribute("user") UserEntity userEntity,
        @PathVariable Long id
    ){
        if(bindingResultProduct.hasErrors() || bindingResultAddress.hasErrors()) {
            return "editproduct";
        }
        productEntity.setLocation(addressEntity);
        productEntity.setOwner(userEntity);
        productService.editProduct(productEntity);
        return "redirect:/myproducts";
    }

    @GetMapping("/removeproduct")
    public String getRemoveProduct(Model model) {
        return "removeproduct";
    }

    @GetMapping("/productdetail/{id}")
    public String getProductDetails(
            Model model,
            @PathVariable Long id,
            @ModelAttribute("user") UserEntity userEntity) {
        ProductEntity product = productService.getById(id);
        if(product != null) {
            model.addAttribute("product", product);
            model.addAttribute("ListOfReservatedTimes", lendingService.getTime(product));
            return "productdetailedview";
        }
        return "redirect:/";
    }

    @GetMapping("/myproducts")
    public String getMyProducts(Model model, @ModelAttribute("user") UserEntity user) {
        List<ProductEntity> myProducts = productService.getAllProductsFromUser(user);
        boolean gotNoProducts = myProducts.isEmpty();
        model.addAttribute("myProducts", myProducts);
        model.addAttribute("gotNoProducts", gotNoProducts);
        return "myproducts";
    }


}

