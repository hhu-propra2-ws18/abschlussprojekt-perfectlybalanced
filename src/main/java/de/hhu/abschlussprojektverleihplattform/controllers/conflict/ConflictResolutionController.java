
package de.hhu.abschlussprojektverleihplattform.controllers.conflict;

import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import de.hhu.abschlussprojektverleihplattform.model.Lendingstatus;
import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.service.LendingService;
import de.hhu.abschlussprojektverleihplattform.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
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
    public String showConflictCenter (Model model) {
        List<LendingEntity> allLendingConflicts = lendingService.getAllConflicts();
        model.addAttribute("allLendingConflicts", allLendingConflicts);
        return "conflictcenter";
    }

    @GetMapping("/conflictdetail/{id}")
    public String showConflictDetails (
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

    @PostMapping("/conflictdetail/{id}")
    public String postResolveConflict (
            @ModelAttribute("lending") @Valid LendingEntity lendingEntity,
            @PathVariable Long id
            ) throws Exception {
        if (id == lendingEntity.getBorrower().getUserId()) {
            lendingService.borrowerReceivesSuretyAfterConflict(lendingEntity);
            lendingEntity.setStatus(Lendingstatus.done);
        }
        else {
            lendingService.ownerReceivesSuretyAfterConflict(lendingEntity);
            lendingEntity.setStatus(Lendingstatus.done);
        }
        return "redirect:/";
    }
}
