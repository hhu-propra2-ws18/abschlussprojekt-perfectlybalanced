package de.hhu.abschlussprojektverleihplattform.model.validation;

import de.hhu.abschlussprojektverleihplattform.service.IUserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private IUserService userService;

    public UniqueEmailValidator(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return (userService.findByEmail(email) == null)
            && (email != null);
    }
}
