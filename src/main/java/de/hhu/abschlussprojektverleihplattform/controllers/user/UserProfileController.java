package de.hhu.abschlussprojektverleihplattform.controllers.user;

import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.service.LendingService;
import de.hhu.abschlussprojektverleihplattform.service.propay.ProPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UserProfileController {

    @Autowired
    public ProPayService proPayService;

    @Autowired
    private LendingService lendingService;

    @GetMapping("/profile")
    public String getProfile(Model model, Authentication auth) throws Exception{
        UserEntity user = (UserEntity) auth.getPrincipal();
        List<LendingEntity> reminder = lendingService
            .getAllReminder(lendingService
                .getAllLendingsForUser(user));

        model.addAttribute("user", user);
        model.addAttribute("user_balance",proPayService.getBalance(user.getUsername()));
        model.addAttribute("lending_reminder", reminder);

        return "profile";
    }

    @PostMapping("/profile/deposit")
    public String depositAmountIntoPropay(
        Authentication auth
    ) throws Exception{

        UserEntity user = (UserEntity) auth.getPrincipal();
        proPayService.changeUserBalanceBy(user.getUsername(),100);
        return "redirect:/profile";
    }
}
