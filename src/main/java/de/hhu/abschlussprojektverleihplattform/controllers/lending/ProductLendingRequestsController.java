package de.hhu.abschlussprojektverleihplattform.controllers.lending;

import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.service.LendingService;
import de.hhu.abschlussprojektverleihplattform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductLendingRequestsController {

    private final LendingService lendingService;
    private final UserService userService;

    @Autowired
    public ProductLendingRequestsController(
        LendingService lendingService,
        UserService userService
    ) {
        this.lendingService = lendingService;
        this.userService = userService;
    }

    @GetMapping("/lendingrequests")
    public String getLendingRequestsOverview(Model model, Authentication auth) {
        UserEntity user = (UserEntity) auth.getPrincipal();
        List<LendingEntity> lendings = lendingService.getAllRequestsForUser(user);
        List<LendingEntity> oldLendings = lendingService.getAllLendings();
        boolean checkMyLendings = lendings.isEmpty();
        List<LendingEntity> returnedProducts = lendingService.getReturnedLendingFromUser(user);
        boolean checkMyReturns = returnedProducts.isEmpty();
        model.addAttribute("checkMyLendings", checkMyLendings);
        model.addAttribute("lendings", lendings);
        model.addAttribute("oldLendings", oldLendings);
        model.addAttribute("returnedProducts", returnedProducts);
        model.addAttribute("checkMyReturns", checkMyReturns);
        return "productlendingrequests";
    }

    @PostMapping("/lendingrequests/reject")
    public String handleRejection(
        @RequestParam Long id,
        Authentication auth
    ) {
        auth.getPrincipal();
        userService.findByUsername("sarah");
        LendingEntity requestedLending = lendingService.getLendingById(id);
        lendingService.denyLendingRequest(requestedLending);
        return "redirect:/lendingrequests";
    }

    @PostMapping("/lendingrequests/accept")
    public String handleAccept(
        @RequestParam Long id,
        Authentication auth
    ) {
        auth.getPrincipal();
        userService.findByUsername("sarah");
        LendingEntity requestedLending = lendingService.getLendingById(id);
        lendingService.acceptLendingRequest(requestedLending);
        return "redirect:/lendingrequests";
    }

    @PostMapping("/lendingrequests/rejectReturn")
    public String handleGoodReturn(
        @RequestParam Long id,
        Authentication auth
    ) {
        auth.getPrincipal();
        userService.findByUsername("sarah");
        LendingEntity requestedLending = lendingService.getLendingById(id);
        lendingService.denyReturnedProduct(requestedLending);
        return "redirect:/lendingrequests";
    }

    @PostMapping("/lendingrequests/acceptReturn")
    public String handleBadReturn(
        @RequestParam Long id,
        Authentication auth
    ) {
        auth.getPrincipal();
        userService.findByUsername("sarah");
        LendingEntity requestedLending = lendingService.getLendingById(id);
        lendingService.acceptReturnedProduct(requestedLending);
        return "redirect:/lendingrequests";
    }

    // TODO:
    // Get/Post Mappings to create a request
    // GetMapping to show all Products the user has borrowed
    // (Get/)Post Mappings to return Products
    // GetMapping to Check returned Prdoduct
    // (Get/)Post Mappings to accept a retuned product or create a conflict
    // All the Views for the Mappings
}
