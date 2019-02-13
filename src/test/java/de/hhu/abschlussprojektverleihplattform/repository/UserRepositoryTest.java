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
    public void startConfigTestOneUseerIsInDatabase(){

        int numberOfUsers = userRepository.getNumberOfUsers();
        Assert.assertEquals(1, numberOfUsers);

    }

    @Test
    public void saveOneUserToDatabase(){

        UserEntity user = new UserEntity("firstName", "LastName", "username", "password", "email");
        userRepository.saveUser(user);
        int numberOfUsers = userRepository.getNumberOfUsers();
        Assert.assertEquals(2, numberOfUsers);

    }

    @Test
    public void startConfigTestLoadMaxMusterMann(){

        UserEntity user = new UserEntity("Max", "Mustermann", "MMustermann", "MaxMuster223", "Max@Mustermann.de");
        UserEntity loadedUser = userRepository.findById(1L);
        Assert.assertTrue(user.getFirstname().equals(loadedUser.getFirstname()) &&
                                    user.getLastname().equals(loadedUser.getLastname()) &&
                                    user.getUsername().equals(loadedUser.getUsername()) &&
                                    user.getEmail().equals(loadedUser.getEmail()));

    }

    @Test
    public void getAllUserWithStartConfig(){
        List<UserEntity> allUser = userRepository.getAllUser();
        Assert.assertEquals(1, allUser.size());
    }

    @Test
    public void getAllUsersAfterAddingOneUser(){
        UserEntity user = new UserEntity("firstName", "LastName", "username", "password", "email");
        userRepository.saveUser(user);
        List<UserEntity> allUser = userRepository.getAllUser();
        Assert.assertEquals(2, allUser.size());
    }
}