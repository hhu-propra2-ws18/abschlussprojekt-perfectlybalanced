package de.hhu.abschlussprojektverleihplattform.controllers.lending;

import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.service.LendingService;
import de.hhu.abschlussprojektverleihplattform.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;

@Controller
public class RequestALendingController {

    private final ProductService productService;

    private final LendingService lendingService;

    public static final String requestalendingURL ="/lendingrequests/sendRequest";

    @Autowired
    public RequestALendingController(ProductService productService, LendingService lendingService) {
        this.productService = productService;
        this.lendingService = lendingService;
    }


    @GetMapping(requestalendingURL)
    public String gotoSendRequest(Model model, @RequestParam Long id, Authentication auth){
        UserEntity user = (UserEntity) auth.getPrincipal();
        ProductEntity product = productService.getById(id);
        model.addAttribute("product", product);
        model.addAttribute("user", user);
        return "sendLendingRequest";
    }

    @PostMapping(requestalendingURL)
    public String requestalending(
        @RequestParam Long id,
        Authentication auth,
        @RequestParam("start") String start,
        @RequestParam("end") String end
    ) throws Exception {

        UserEntity user = (UserEntity) auth.getPrincipal();
        ProductEntity product = productService.getById(id);

        String startTimeStampString = start + "T01:00:00";
        String endTimeStampString = end + "T01:00:00";

        Timestamp startTimestamp
            = Timestamp.valueOf(startTimeStampString.replace("T", " "));
        Timestamp endTimestamp
            = Timestamp.valueOf(endTimeStampString.replace("T", " "));

        LendingEntity didrequest = lendingService.requestLending(user,
            product,
            startTimestamp,
            endTimestamp);

        if(didrequest == null){
            throw new Exception("cannot make lending request");
        }

        return "redirect:/";
    }  
}
