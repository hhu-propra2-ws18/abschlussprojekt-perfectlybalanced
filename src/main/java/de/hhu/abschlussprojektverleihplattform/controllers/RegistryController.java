package de.hhu.abschlussprojektverleihplattform.controllers;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegistryController {

    private final IUserService userService;

    @Autowired
    public RegistryController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model){

        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();
        if(!(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/profile";
        }

        model.addAttribute("user", new UserEntity());
        return "registry";
    }

    @PostMapping("/register")
    public String postRegisterUser(@ModelAttribute("user") @Valid UserEntity userEntity,
                                   BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "registry";
        }

        String username = userEntity.getUsername();
        String password = userEntity.getPassword();

        userService.addUser(userEntity);
        userService.autoLogin(username, password);

        return "redirect:/profile";
    }
}
