package de.hhu.abschlussprojektverleihplattform.controllers.lending;

import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.service.LendingService;
import de.hhu.abschlussprojektverleihplattform.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;

@Controller
public class RequestALendingController {

    @Autowired
    ProductService productService;

    @Autowired
    LendingService lendingService;

    public static final String sendLendingRequestURL="/sendLendingRequest";

    public static final String requestalendingURL ="/requestalending";

    @GetMapping("/sendLendingRequest")
    public String gotoSendRequest(Model model, @RequestParam Long id, Authentication auth){
        UserEntity user = (UserEntity) auth.getPrincipal();
        ProductEntity product = productService.getById(id);
        return "sendLendingRequest";
    }

    @PostMapping("/requestalending")
    public String requestalending(@RequestParam Long id, Authentication auth) throws Exception{
        UserEntity user = (UserEntity) auth.getPrincipal();
        ProductEntity product = productService.getById(id);

        lendingService.requestLending(user,
                product,
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis() + 86400000));

        return "redirect:/";
    }
}
