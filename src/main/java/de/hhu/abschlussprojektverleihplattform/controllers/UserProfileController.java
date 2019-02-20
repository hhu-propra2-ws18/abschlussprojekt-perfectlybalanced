package de.hhu.abschlussprojektverleihplattform.controllers;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.service.propay.ProPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserProfileController {

    @Autowired
    public ProPayService proPayService;

    @GetMapping("/profile")
    public String getProfile(Model model, Authentication auth) throws Exception{

        //this page should only be available to logged in users.
        //otherwise it would redirect to login page

        UserEntity user = (UserEntity) auth.getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("user_balance",proPayService.getBalance(user.getUsername()));

        //TODO: make it redirect to login page for not logged in users

        return "profile";
    }

    @PostMapping("/profile/deposit")
    public String depositAmountIntoPropay(@RequestParam Long amount, Authentication auth) throws Exception{
        if(amount<=0){
            return "redirect:/profile";
        }

        UserEntity user = (UserEntity) auth.getPrincipal();
        proPayService.changeUserBalanceBy(user.getUsername(),amount);
        return "redirect:/profile";
    }
}
