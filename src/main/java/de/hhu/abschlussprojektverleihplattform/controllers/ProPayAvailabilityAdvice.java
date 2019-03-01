package de.hhu.abschlussprojektverleihplattform.controllers;

import de.hhu.abschlussprojektverleihplattform.service.propay.ProPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class ProPayAvailabilityAdvice {

    @Autowired
    ProPayService proPayService;

    @ModelAttribute("propayavailability")
    public boolean propayavailable() {
        try {
            proPayService.isAvailable();
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
