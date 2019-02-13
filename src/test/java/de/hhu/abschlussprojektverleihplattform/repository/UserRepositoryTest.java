package de.hhu.abschlussprojektverleihplattform.repository;

import de.hhu.abschlussprojektverleihplattform.AbschlussprojektVerleihplattformApplication;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import org.apache.coyote.http11.Constants;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.ws.WebEndpoint;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {


    @Autowired
    private UserRepository userRepository;

    @Test
    public void startConfigTestOneUseerIsInDatabase(){

        int numberOfUsers = userRepository.getNumberOfUsers();
        Assert.assertTrue(numberOfUsers == 1);

    }

    @Test
    public void saveOneUserToDatabase(){

        UserEntity user = new UserEntity("firstName", "LastName", "username", "password", "email");
        userRepository.saveUser(user);
        int numberOfUsers = userRepository.getNumberOfUsers();
        Assert.assertTrue(numberOfUsers == 2);

    }

    @Test
    public void startConfigTestLoadMaxMusterMann(){

        UserEntity user = new UserEntity("Max", "Mustermann", "MMustermann", "MaxMuster223", "Max@Mustermann.de");
        UserEntity loadedUser = userRepository.findById(new Long(1));
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" + loadedUser);
        Assert.assertTrue(user.getFirstname().equals(loadedUser.getFirstname()) &&
                                    user.getLastname().equals(loadedUser.getLastname()) &&
                                    user.getUsername().equals(loadedUser.getUsername()) &&
                                    user.getEmail().equals(loadedUser.getEmail()));

    }
}