package de.hhu.abschlussprojektverleihplattform.model.validation;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.service.IUserService;
import org.springframework.dao.EmptyResultDataAccessException;

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
    public boolean isValid(String email,
        ConstraintValidatorContext context) throws EmptyResultDataAccessException {
        try {
            if (userService.findByEmail(email) != null) {
                return false;
            }

            return !email.isEmpty();
        } catch (EmptyResultDataAccessException e) {
            return !email.isEmpty();
        }
    }
}
