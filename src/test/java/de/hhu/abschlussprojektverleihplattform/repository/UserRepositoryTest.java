package de.hhu.abschlussprojektverleihplattform.repository;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {


    @Autowired
    private UserRepository userRepository;


    @Test
    public void saveOneUserToDatabase() {
        UserEntity user = new UserEntity("firstName", "LastName", "username", "password", "email");
        userRepository.saveUser(user);
        UserEntity loadedUser = userRepository.getUserByFirstname(user.getFirstname());
        Assert.assertTrue(user.getFirstname().equals(loadedUser.getFirstname()) &&
                user.getLastname().equals(loadedUser.getLastname()) &&
                user.getUsername().equals(loadedUser.getUsername()) &&
                user.getPassword().equals(loadedUser.getPassword()) &&
                user.getEmail().equals(loadedUser.getEmail()));
    }

    @Test
    public void startConfigTestLoadMaxMusterMann() {

        UserEntity user = new UserEntity("Max", "Mustermann", "MMustermann", "MaxMuster223", "Max@Mustermann.de");
        UserEntity loadedUser = userRepository.findById(1L);
        Assert.assertTrue(user.getFirstname().equals(loadedUser.getFirstname()) &&
                user.getLastname().equals(loadedUser.getLastname()) &&
                user.getUsername().equals(loadedUser.getUsername()) &&
                user.getEmail().equals(loadedUser.getEmail()));

    }

    @Test
    public void getUserByUsername() {

        UserEntity user = new UserEntity("Max", "Mustermann", "MMustermann", "MaxMuster223", "Max@Mustermann.de");
        UserEntity loadedUser = userRepository.findByUsername("MMustermann");
        Assert.assertTrue(user.getFirstname().equals(loadedUser.getFirstname()) &&
                user.getLastname().equals(loadedUser.getLastname()) &&
                user.getUsername().equals(loadedUser.getUsername()) &&
                user.getEmail().equals(loadedUser.getEmail()));

    }


    @Test
    public void getAllUsersAfterAddingOneUser() {
        int numberOfUserAtTestStart = userRepository.getNumberOfUsers();
        UserEntity user = new UserEntity("vorname", "LastName", "username2", "password", "email");
        userRepository.saveUser(user);
        List<UserEntity> allUser = userRepository.getAllUser();
        Assert.assertEquals(numberOfUserAtTestStart + 1, allUser.size());
    }
}