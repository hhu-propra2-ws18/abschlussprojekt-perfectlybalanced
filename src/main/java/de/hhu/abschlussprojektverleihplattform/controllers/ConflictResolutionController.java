package de.hhu.abschlussprojektverleihplattform.controllers;

import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ConflictResolutionController {

    @GetMapping("/conflictcenter")
    public String showConflictCenter(Model model){
        // TODO: rufe alle Verleiher auf mit Lendingstatus = conflict
        List<LendingEntity> allLendingConflicts;
        //model.addAttribute("all", allLendingConflicts);
        return "conflictcenter";
    }

    @GetMapping("/conflictdetails/{id}")
    public String showConflictDetails(Model model, Long id) {
        // TODO: suche VerleiherID, inject Service
        return "conflictdetails";
    }
}
