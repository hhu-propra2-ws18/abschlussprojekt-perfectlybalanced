package de.hhu.abschlussprojektverleihplattform.controllers;

import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.repository.LendingRepository;
import de.hhu.abschlussprojektverleihplattform.service.LendingService;
import de.hhu.abschlussprojektverleihplattform.service.ProductService;
import de.hhu.abschlussprojektverleihplattform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductLendingRequestsController {

    private final ProductService productService;
    private final LendingService lendingService;
    private final UserService userService;
    //TODO: REMOVE REPO IT WAS JUST FPR TESTING!!!
    private final LendingRepository lendingRepository;

    @Autowired
    public ProductLendingRequestsController(
            ProductService productService,
            LendingService lendingService,
            UserService userService,
            LendingRepository lendingRepository) {

        this.productService = productService;
        this.lendingService = lendingService;
        this.userService = userService;
        this.lendingRepository = lendingRepository;
    }

    @GetMapping("/lendingrequests")
    public String getLendingRequestsOverview(Model model, Authentication auth) throws Exception {
        UserEntity user = (UserEntity) auth.getPrincipal();
        UserEntity loadedUser = userService.findByUsername("sarah");
        List<LendingEntity> lendings = lendingService.getAllRequestsForUser(loadedUser);
        List<LendingEntity> oldLendings = lendingRepository.getAllLendings();
        model.addAttribute("lendings", lendings);
        model.addAttribute("oldLendings", oldLendings);
        return "productlendingrequests";
    }

    @PostMapping("/lendingrequests/reject")
    public String handleRejection(Model model, @RequestParam Long id,
                                  Authentication auth)
            throws Exception{

        UserEntity user = (UserEntity) auth.getPrincipal();
        UserEntity loadedUser = userService.findByUsername("sarah");
        LendingEntity requestedLending = lendingService.getLendingById(id);
        lendingService.rejectLending(requestedLending);
        return "redirect:/lendingrequests";
    }





    /*TODO:
    PostMapping accept/deny request
    Get/Post Mappings to create a request
    GetMapping to show all Products the user has borrowed
    (Get/)Post Mappings to return Products
    GetMapping to Check returned Prdoduct
    (Get/)Post Mappings to accept a retuned product or create a conflict
    All the Views for the Mappings
     */
}
