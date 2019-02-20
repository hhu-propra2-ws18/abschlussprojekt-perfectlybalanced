package de.hhu.abschlussprojektverleihplattform.model.validation;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.service.IUserService;
import de.hhu.abschlussprojektverleihplattform.service.UserService;

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
    public boolean isValid(String username, ConstraintValidatorContext context) {
        UserEntity user = userService.findByUsername(username);
        return (username != null)
                && !username.contains(" ")
                && (user == null);
    }
}
