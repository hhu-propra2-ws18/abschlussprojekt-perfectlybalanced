package de.hhu.abschlussprojektverleihplattform.controllers.lending;

import de.hhu.abschlussprojektverleihplattform.logic.Timespan;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RequestALendingController {

    @Autowired
    ProductService productService;

    @Autowired
    LendingService lendingService;

    public static final String sendLendingRequestURL="/lendingrequests/sendRequest";

    public static final String requestalendingURL ="lendingrequests/sendRequest";

    @GetMapping("lendingrequests/sendRequest")
    public String gotoSendRequest(Model model, @RequestParam Long id, Authentication auth){
        UserEntity user = (UserEntity) auth.getPrincipal();
        ProductEntity product = productService.getById(id);
        model.addAttribute("product", product);
        model.addAttribute("user", user);
        model.addAttribute("timespan", new Timespan());
        return "sendLendingRequest";
    }

    @PostMapping("lendingrequests/sendRequest")
    public String requestalending(@RequestParam Long id,
                                  Authentication auth,
                                  @ModelAttribute("lending") LendingEntity lending,
                                  Timespan timespan) throws Exception{



        UserEntity user = (UserEntity) auth.getPrincipal();
        ProductEntity product = productService.getById(id);

        //Timestamp start = Timestamp.valueOf(startTime.toString());
        //Timestamp end = Timestamp.valueOf(endTime.toString());


        boolean didrequest = lendingService.requestLending(user,
                product,
                timespan.getStart(),
                timespan.getEnd());
        if(!didrequest){
            throw new Exception("cannot make lending request");
        }

        return "redirect:/";
    }
}
