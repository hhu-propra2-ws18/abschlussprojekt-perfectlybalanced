package de.hhu.abschlussprojektverleihplattform.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomEmailTest {

    @Autowired
    private Validator validator;

    @Test
    public void validEmail() {
        // Arrange
        String email = "sam+love@test.de";
        UserEntity userEntity = new UserEntity("Sam", "Love", "sam", "sassel", email);

        // Act
        Set<ConstraintViolation<UserEntity>> violations = validator.validate(userEntity);

        // Assert
        Assert.assertTrue("Email ist gültig.", violations.isEmpty());
    }

    @Test
    public void allEmailsAreValid() {
        // Arrange
        List<String> emails = new ArrayList<>();
        emails.add("test@domain.de");
        emails.add("test@domain.co.uk");
        emails.add("user.test@domain.de");
        emails.add("user+test@domain.de");
        emails.add("user-test@domain.de");
        emails.add("user-test@domain-url.de");
        emails.add("user_test@domain_url.de");
        emails.add("user1@domain.de");
        emails.add("us1er123@domain.de");

        List<UserEntity> users = multipleUsers(emails, emails.size());

        List<Integer> expected = new ArrayList<>();
        for (int k = 0; k < emails.size(); k++) {
            expected.add(0);
        }

        // Act
        List<Integer> result = new ArrayList<>();
        for (UserEntity user : users) {
            Set<ConstraintViolation<UserEntity>> violations = validator.validate(user);
            result.add(violations.size());
        }

        // Assert
        Assert.assertTrue("Alle Emails sind gültig", result.equals(expected));
    }

    @Test
    public void invalidEmail() {
        // Arrange
        String email = "@";
        UserEntity user = testUser(email);
        // Act
        Set<ConstraintViolation<UserEntity>> violations = validator.validate(user);

        // Assert
        Assert.assertEquals(1, violations.size());
    }

    @Test
    public void allEmailsAreInvalid() {
        // Arrange
        List<String> emails = new ArrayList<>();
        emails.add("1");
        emails.add(".@test.de");
        emails.add("test user@test.de");
        emails.add(".user@test.de");
        emails.add("user@test.de.");
        emails.add("test@test..de");
        emails.add("test@test.d");
        emails.add("test@test.commerce");
        emails.add("test@test");

        List<UserEntity> users = multipleUsers(emails, emails.size());

        List<Integer> expected = new ArrayList<>();
        for (int k = 0; k < emails.size(); k++) {
            expected.add(1);
        }

        // Act
        List<Integer> result = new ArrayList<>();
        for (UserEntity user : users) {
            Set<ConstraintViolation<UserEntity>> violations = validator.validate(user);
            result.add(violations.size());
        }

        // Assert
        Assert.assertTrue("Alle Emails sind ungültig.", result.equals(expected));
    }

    private UserEntity testUser(String email) {
        return new UserEntity("test", "test", "test", "tester", email);

    }

    private List<UserEntity> multipleUsers(List<String> emails, int size) {
        List<UserEntity> users = new ArrayList<>();
        int i = 0;
        for (String email : emails) {
            UserEntity userEntity = new UserEntity("test", "test", "test" + i, "tester", email);
            users.add(userEntity);
            i++;
        }

        return users;
    }
}
