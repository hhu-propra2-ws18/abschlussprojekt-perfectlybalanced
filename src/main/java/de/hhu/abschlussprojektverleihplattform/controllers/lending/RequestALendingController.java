package de.hhu.abschlussprojektverleihplattform.controllers.lending;

import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.service.LendingService;
import de.hhu.abschlussprojektverleihplattform.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;

@Controller
public class RequestALendingController {

    private final ProductService productService;

    private final LendingService lendingService;

    static final String requestalendingURL ="/requestalending";

    @Autowired
    public RequestALendingController(ProductService productService, LendingService lendingService) {
        this.productService = productService;
        this.lendingService = lendingService;
    }

    @GetMapping("/sendLendingRequest")
    public String gotoSendRequest(){
        return "sendLendingRequest";
    }

    @PostMapping("/requestalending")
    public String requestalending(@RequestParam Long id, Authentication auth) throws Exception{
        UserEntity user = (UserEntity) auth.getPrincipal();
        ProductEntity product = productService.getById(id);

        lendingService.requestLending(user,
                product,
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis() + 86400000)
        );
        return "redirect:/";
    }
}
