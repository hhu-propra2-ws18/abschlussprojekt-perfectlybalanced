package de.hhu.abschlussprojektverleihplattform.controllers;

import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.service.LendingService;
import de.hhu.abschlussprojektverleihplattform.service.ProductService;
import de.hhu.abschlussprojektverleihplattform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ProductLendingRequestsController {

    @Autowired
    private ProductService productService;
    @Autowired
    private LendingService lendingService;
    @Autowired
    private UserService userService;

    @GetMapping("/lendingrequests")
    public String getLendingRequestsOverview(
            Model model,
            HttpServletRequest httpServletRequest,
            Authentication auth
    ) throws Exception {
        UserEntity user = (UserEntity) auth.getPrincipal();
        List<LendingEntity> lendings = lendingService.getAllRequestsForUser(user);
        model.addAttribute(lendings);
        return "productlendingrequests";
    }
}
