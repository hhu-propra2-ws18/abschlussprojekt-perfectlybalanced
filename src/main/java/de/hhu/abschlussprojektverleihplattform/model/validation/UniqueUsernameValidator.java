package de.hhu.abschlussprojektverleihplattform.model.validation;

import de.hhu.abschlussprojektverleihplattform.service.IUserService;
import de.hhu.abschlussprojektverleihplattform.service.UserService;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private IUserService userService;

    public UniqueUsernameValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context)  throws EmptyResultDataAccessException {
        try {
            if(userService.findByUsername(username) != null) {
                return false;
            }
            return (!username.isEmpty())
                && !username.contains(" ");
        } catch (EmptyResultDataAccessException e) {
            return (!username.isEmpty())
                && !username.contains(" ");
        }
    }
}
