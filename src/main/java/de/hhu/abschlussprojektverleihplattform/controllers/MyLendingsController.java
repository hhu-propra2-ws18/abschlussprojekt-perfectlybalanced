package de.hhu.abschlussprojektverleihplattform.controllers;

import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.service.LendingService;
import de.hhu.abschlussprojektverleihplattform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MyLendingsController {

    public static final String url="/mylendings";

    //this can be extended to also show past lendings.
    //this is benefit for people who want to lend things multiple times so they can find those products

    @Autowired
    UserService userService;

    @Autowired
    LendingService lendingService;

    @GetMapping("/mylendings")
    public String getMyCurrentLendings(Model model, Authentication auth){
        UserEntity user = (UserEntity) auth.getPrincipal();

        model.addAttribute("user",user);
        List<LendingEntity> lendings = lendingService.getAllLendingsForUser(user);

        model.addAttribute("lendings",lendings);
        return "mycurrentlendings";
    }
}
