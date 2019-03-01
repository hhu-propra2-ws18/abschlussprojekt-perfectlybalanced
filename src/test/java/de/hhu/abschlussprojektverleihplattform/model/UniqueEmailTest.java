package de.hhu.abschlussprojektverleihplattform.model;

import de.hhu.abschlussprojektverleihplattform.service.UserService;
import de.hhu.abschlussprojektverleihplattform.utils.RandomTestData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Random;
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

    private Random randomID = new Random();

    @Test
    public void validateDuplicatedEmail() throws Exception {
        // Arrange
        String email = "duplicated@mail.de";
        UserEntity user = new UserEntity(
            "Max", "Mock", "loginUserName12", "mocking", email
        );
        user.setUserId(randomID.nextLong());

        UserEntity userWithSameMail = RandomTestData.newRandomTestUser();
        userWithSameMail.setEmail(email);
        userWithSameMail.setUserId(randomID.nextLong());

        when(userService.findByEmail(ArgumentMatchers.anyString())).thenReturn(user);

        // Act
        Set<ConstraintViolation<UserEntity>> violations = validator.validate(user);

        // Assert
        assertEquals(1, violations.size());
    }
}
