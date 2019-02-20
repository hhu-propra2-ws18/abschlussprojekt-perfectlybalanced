package de.hhu.abschlussprojektverleihplattform.service;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private IUserService userService;


    @Test
    public void saveOneUser() {
        UserEntity test = new UserEntity("Max",
                "Mustermann",
                "maxmustermann",
                "password",
                "test@mailtest.test");

        userService.addUser(test);
        UserEntity expected = userService.findByUsername(test.getUsername());

        Assert.assertEquals(expected.getUsername(), test.getUsername());

    }

    @Test
    public void findUserById() {
        UserEntity newUser = new UserEntity("Moritz", "Mustermann", "moritzmustermann", "password", "moritz@tester.com");
        userService.addUser(newUser);
        UserEntity expected = userService.findByUsername("moritzmustermann");

        UserEntity test = userService.showUserById(expected.getUserId());

        Assert.assertEquals(test, expected);
    }
}
