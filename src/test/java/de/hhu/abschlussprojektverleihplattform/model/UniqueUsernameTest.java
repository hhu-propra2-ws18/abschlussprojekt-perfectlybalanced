package de.hhu.abschlussprojektverleihplattform.model;

import de.hhu.abschlussprojektverleihplattform.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest

public class UniqueUsernameTest {

    @MockBean
    private UserService userService;

    @Autowired
    private Validator validator;

    @Test
    public void validateDuplicatedUsername() throws Exception {
        // Arrange
        String login = "Duplo";
        UserEntity user = new UserEntity(
            "Max",
            "Mock",
            login,
            "mocking",
            "mock@test.com");

        when(userService.findByUsername(ArgumentMatchers.anyString())).thenReturn(user);

        // Act
        Set<ConstraintViolation<UserEntity>> violations = validator.validate(user);

        // Assert
        assertEquals(1, violations.size());
    }

    @Test
    public void validateBlankSpaceUsername() throws Exception {
        // Arrange
        String login = " ";

        // Act
        UserEntity newUser = new UserEntity(
            "Moritz",
            "Mock2",
            login,
            "mocking",
            "mock2@test.com");
        Set<ConstraintViolation<UserEntity>> violations = validator.validate(newUser);

        // Assert
        assertEquals(2, violations.size());
    }

    @Test
    public void validateWhiteSpaceInUsername() throws Exception {
        // Arrange
        String username = "test space";

        // Act
        UserEntity newUser = new UserEntity(
            "Moritz",
            "Mock2",
            username,
            "mocking",
            "mock2@test.com");

        Set<ConstraintViolation<UserEntity>> violations = validator.validate(newUser);

        // Assert
        assertEquals(1, violations.size());
    }

    @Test
    public void validateMultipleWhiteSpacesInUsername() throws Exception {
        // Arrange
        String username = "  test    space  ";

        // Act
        UserEntity newUser = new UserEntity(
            "Moritz",
            "Mock2",
            username,
            "mocking",
            "mock2@test.com");
        Set<ConstraintViolation<UserEntity>> violations = validator.validate(newUser);

        // Assert
        assertEquals(1, violations.size());
    }
}
