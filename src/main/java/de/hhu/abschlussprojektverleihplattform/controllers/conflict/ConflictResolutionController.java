
package de.hhu.abschlussprojektverleihplattform.controllers.conflict;

import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import de.hhu.abschlussprojektverleihplattform.service.LendingService;
import de.hhu.abschlussprojektverleihplattform.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

    @GetMapping("/admin")
    public String showConflictCenter(Model model) {
        List<LendingEntity> lendings = lendingService.getAllConflicts();
        model.addAttribute(lendings);
        return "conflictcenter";
    }

    @GetMapping("/admin/{id}")
    public String showConflictDetails(Model model, Long id) {
        // TODO: suche VerleiherID, inject Service
        return "conflictdetails";
    }
}
