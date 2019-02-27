package de.hhu.abschlussprojektverleihplattform.model;

import de.hhu.abschlussprojektverleihplattform.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest

public class UniqueEmailTest {

    @MockBean
    private UserService userService;

    @Autowired
    private Validator validator;

    @Test
    public void validateDuplicatedEmail() throws Exception {
        // Arrange
        String email = "duplicated@mail.de";
        UserEntity user = new UserEntity(
            "Max", "Mock", "loginUserName12", "mocking", email
        );

        when(userService.findByEmail(ArgumentMatchers.anyString())).thenReturn(user);

        // Act
        Set<ConstraintViolation<UserEntity>> violations = validator.validate(user);

        // Assert
        assertEquals(2, violations.size());
    }
}
