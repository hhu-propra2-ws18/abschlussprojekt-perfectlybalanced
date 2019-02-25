package de.hhu.abschlussprojektverleihplattform.model;

import de.hhu.abschlussprojektverleihplattform.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UniqueEmailTest {

    @Autowired
    private UserService userService;

    @Autowired
    private Validator validator;

    @Test
    public void validateDuplicatedEmail() throws Exception {
        // Arrange
        String email = "duplicated@mail.de";
        UserEntity user = new UserEntity("Max", "Mock", "loginUserName12", "mocking", email);
        userService.addUser(user);

        // Act
        UserEntity newUser = new UserEntity("Moritz", "Mock2", "loginUserName13", "mocking", email);
        Set<ConstraintViolation<UserEntity>> violations = validator.validate(newUser);

        // Assert
        assertEquals(1, violations.size());
    }
}
