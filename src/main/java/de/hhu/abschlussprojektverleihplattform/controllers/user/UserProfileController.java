package de.hhu.abschlussprojektverleihplattform.controllers.user;

import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.service.LendingService;
import de.hhu.abschlussprojektverleihplattform.service.propay.adapter.ProPayAdapter;
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

    private final ProPayService proPayService;
    private final ProPayAdapter proPayAdapter;
    private final LendingService lendingService;

    @Autowired
    public UserProfileController(
        ProPayService proPayService,
        ProPayAdapter proPayAdapter,
        LendingService lendingService) {

        this.proPayService = proPayService;
        this.proPayAdapter = proPayAdapter;
        this.lendingService = lendingService;
    }

    @GetMapping("/profile")
    public String getProfile(Model model, Authentication auth) throws Exception {
        UserEntity user = (UserEntity) auth.getPrincipal();
        List<LendingEntity> getUserLendings = lendingService.getAllLendingsForUser(user);

        List<LendingEntity> reminder = lendingService
            .getAllReminder(getUserLendings);

        model.addAttribute("user", user);
        model.addAttribute("user_balance", proPayService.usersCurrentBalance(user.getUsername()));
        model.addAttribute("lending_reminder", reminder);
        return "profile";
    }

    @PostMapping("/profile/deposit")
    public String depositAmountIntoPropay(
        Authentication auth
    ) throws Exception {

        UserEntity user = (UserEntity) auth.getPrincipal();
        proPayAdapter.createAccountIfNotAlreadyExistsAndIncreaseBalanceBy(user.getUsername(), 100);
        return "redirect:/profile";
    }
}
