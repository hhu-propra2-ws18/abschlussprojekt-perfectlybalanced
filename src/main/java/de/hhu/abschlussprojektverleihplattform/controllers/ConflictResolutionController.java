package de.hhu.abschlussprojektverleihplattform.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class ConflictResolutionController {
    @GetMapping("/conflictcenter")
    public String showConflictCenter(Model model){
        return "conflictcenter";
    }
}
