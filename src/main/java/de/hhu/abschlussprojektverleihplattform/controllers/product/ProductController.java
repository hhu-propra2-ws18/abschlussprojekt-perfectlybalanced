package de.hhu.abschlussprojektverleihplattform.controllers.product;

import de.hhu.abschlussprojektverleihplattform.model.AddressEntity;
import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.Productstatus;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.service.ILendingService;
import de.hhu.abschlussprojektverleihplattform.service.IProductService;
import de.hhu.abschlussprojektverleihplattform.service.ISellService;
import de.hhu.abschlussprojektverleihplattform.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ProductController {

    private final IProductService productService;
    private final ILendingService lendingService;
    private final ISellService sellService;

    @Autowired

    public ProductController(
        IProductService productService,
        ILendingService lendingService,
        ISellService sellService
    ) {
        this.productService = productService;
        this.lendingService = lendingService;
        this.sellService = sellService;
    }


    @ModelAttribute
    public void addAttributes(Model model, Authentication auth) {
        UserEntity authUser = (UserEntity) auth.getPrincipal();
        model.addAttribute("user", authUser);
    }


    @GetMapping("/addproduct")
    public String getAddProduct(Model model) {
        return "addproduct";
    }

    @GetMapping("/addproductlending")
    public String getLendProduct(Model model) {
        model.addAttribute("product", new ProductEntity());
        model.addAttribute("address", new AddressEntity());
        return "addproductlending";
    }

    @GetMapping("/addproductselling")
    public String getSellProduct(Model model) {
        model.addAttribute("product", new ProductEntity());
        model.addAttribute("address", new AddressEntity());
        return "addproductselling";
    }

    @PostMapping("/addproductlending")
    public String postLendProduct(
        @ModelAttribute("product") @Valid ProductEntity productEntity,
        BindingResult bindingResultProduct,
        @ModelAttribute("address") @Valid AddressEntity addressEntity,
        BindingResult bindingResultAddress,
        @ModelAttribute("user") UserEntity userEntity) {

        if (bindingResultProduct.hasErrors() || bindingResultAddress.hasErrors()) {
            return "addproductlending";
        }

        productEntity.setLocation(addressEntity);
        productEntity.setOwner(userEntity);
        productEntity.setStatus(Productstatus.forLending);
        productService.addProduct(productEntity);
        return "redirect:/";
    }

    @PostMapping("/addproductselling")
    public String postSellProduct(
        @ModelAttribute("product") @Valid ProductEntity productEntity,
        BindingResult bindingResultProduct,
        @ModelAttribute("address") @Valid AddressEntity addressEntity,
        BindingResult bindingResultAddress,
        @ModelAttribute("user") UserEntity userEntity) {

        if (bindingResultProduct.hasErrors() || bindingResultAddress.hasErrors()) {
            return "addproductselling";
        }

        productEntity.setLocation(addressEntity);
        productEntity.setOwner(userEntity);
        productEntity.setStatus(Productstatus.forBuying);
        productService.addProduct(productEntity);
        return "redirect:/";
    }

    @GetMapping("/editproduct/{id}")
    public String getEditProduct(
        Model model,
        @PathVariable Long id,
        @ModelAttribute("user") UserEntity userEntity) {
        ProductEntity product = productService.getById(id);
        if (product != null && product.getOwner().getUserId().equals(userEntity.getUserId())) {
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
    ) {
        if (bindingResultProduct.hasErrors() || bindingResultAddress.hasErrors()) {
            return "editproduct";
        }
        ProductEntity oldProduct = productService.getById(id);
        productEntity.setLocation(addressEntity);
        productEntity.setOwner(userEntity);
        productEntity.setStatus(oldProduct.getStatus());
        productService.editProduct(productEntity);
        return "redirect:/myproducts";
    }

    @GetMapping("/productdetail/{id}")
    public String getProductDetails(
        Model model,
        @PathVariable Long id,
        @ModelAttribute("user") UserEntity userEntity) {
        ProductEntity product = productService.getById(id);
        if (product != null) {
            model.addAttribute("product", product);
            model.addAttribute("ListOfReservatedTimes", lendingService.getAvailableTime(product));
            model.addAttribute("ListOfStrings", lendingService.getAvailabilityStrings(product));
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

    @GetMapping("/buyrequests/sendRequest")
    public String gotoBuyRequest(
        Model model,
        @RequestParam Long id,
        Authentication auth) {

        UserEntity user = (UserEntity) auth.getPrincipal();
        ProductEntity product = productService.getById(id);
        if (product == null) {
            return "redirect:/";
        }

        model.addAttribute("user", user);
        model.addAttribute("product", product);

        return "sendBuyRequest";
    }

    @PostMapping("/buyrequests/sendRequest")
    public String performBuyRequest(
        Model model,
        @RequestParam Long id,
        Authentication auth
    ) throws Exception {
        UserEntity user = (UserEntity) auth.getPrincipal();
        ProductEntity product = productService.getById(id);
        sellService.buyProduct(user, product);
        return "redirect:/";
    }
}

