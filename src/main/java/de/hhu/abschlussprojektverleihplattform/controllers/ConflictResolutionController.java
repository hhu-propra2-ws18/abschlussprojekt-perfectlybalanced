
package de.hhu.abschlussprojektverleihplattform.controllers;

import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.service.LendingService;
import de.hhu.abschlussprojektverleihplattform.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

//@Controller
public class ConflictResolutionController {

    @Autowired
    private LendingService lendingService;
    @Autowired
    private ProductService productService;

    @GetMapping("/conflictcenter")
    public String showConflictCenter(Model model) {
        List<LendingEntity> lendings = lendingService.getAllConflicts();
        model.addAttribute(lendings);
        return "conflictcenter";
    }

    @GetMapping("/conflictdetails/{id}")
    public String showConflictDetails(Model model, Long id) {
        // TODO: suche VerleiherID, inject Service
        return "conflictdetails";
    }
}
