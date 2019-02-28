
package de.hhu.abschlussprojektverleihplattform.controllers.conflict;

import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.service.LendingService;
import de.hhu.abschlussprojektverleihplattform.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ConflictResolutionController {

    private final LendingService lendingService;
    private final ProductService productService;

    @Autowired
    public ConflictResolutionController(
            LendingService lendingService,
            ProductService productService
    ) {
        this.lendingService = lendingService;
        this.productService = productService;
    }

    @GetMapping("/conflictcenter")
    public String showConflictCenter(Model model) {
        List<LendingEntity> allLendingConflicts = lendingService.getAllConflicts();
        model.addAttribute("allLendingConflicts", allLendingConflicts);
        return "conflictcenter";
    }

    @GetMapping("/conflictcenter/conflictdetail/{id}")
    public String showConflictDetails(
            Model model,
            @PathVariable Long id,
            @ModelAttribute("user") UserEntity userEntity) throws Exception {
        LendingEntity lending = lendingService.getLendingById(id);
        if (lending != null) {
            model.addAttribute("lending", lending);
            return "conflictdetail";
        }
        return "redirekt:/";
    }

    @PostMapping("/conflictcenter/decideForOwner")
    public String resolveConflictForOwner(
            @RequestParam Long id
    ) throws Exception {
        LendingEntity lend = lendingService.getLendingById(id);
        lendingService.ownerReceivesSuretyAfterConflict(lend);
        return "redirect:/conflictcenter";
    }

    @PostMapping("/conflictcenter/decideForBorrower")
    public String resolveConflictForBorrower(
            @RequestParam Long id
    ) throws Exception {
        LendingEntity lend = lendingService.getLendingById(id);
        lendingService.borrowerReceivesSuretyAfterConflict(lend);
        return "redirect:/conflictcenter";
    }
}
