package de.hhu.abschlussprojektverleihplattform.repository;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.utils.RandomTestData;
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
    public void testSavingSetsId(){
        UserEntity user = RandomTestData.newRandomTestUser();

        userRepository.saveUser(user);

        //fails if id is not set
        userRepository.findById(user.getUserId());
    }

    @Test
    public void saveOneUserToDatabase() {
        UserEntity user = RandomTestData.newRandomTestUser();

        userRepository.saveUser(user);
        UserEntity loadedUser = userRepository.findById(user.getUserId());

        Assert.assertEquals(user,loadedUser);
    }

    @Test
    public void findUserById() {
        UserEntity user = RandomTestData.newRandomTestUser();

        userRepository.saveUser(user);
        UserEntity loadedUser = userRepository.findById(user.getUserId());

        Assert.assertEquals(user,loadedUser);
    }

    @Test
    public void findUserByUsername() {
        UserEntity user = RandomTestData.newRandomTestUser();

        userRepository.saveUser(user);
        UserEntity loadedUser = userRepository.findByUsername(user.getUsername());

        Assert.assertEquals(user,loadedUser);
    }

    @Test
    public void findByEmail() {
        UserEntity user = RandomTestData.newRandomTestUser();

        userRepository.saveUser(user);
        UserEntity loadedUser = userRepository.findByEmail(user.getEmail());

        Assert.assertEquals(user,loadedUser);
    }

    @Test
    public void findByIdExpectNull() {
        try {
            UserEntity loadedUser = userRepository.findById((long) -9999);
            Assert.fail();
        }catch (Exception e){
            //pass
        }
    }

    @Test
    public void findByUsernameExpectNull() {
        try {
            UserEntity loadedUser = userRepository.findByUsername("");
            Assert.fail();
        }catch (Exception e){
            //pass
        }
    }

    @Test
    public void findByEmailExpectNull() {
        try {
            UserEntity loadedUser = userRepository.findByEmail("");
            Assert.fail();
        }catch (Exception e){
            //pass
        }
    }

    @Test
    public void getAllUsersAfterAddingOneUser() {
        int numberOfUserAtTestStart = userRepository.getNumberOfUsers();
        UserEntity user = RandomTestData.newRandomTestUser();
        userRepository.saveUser(user);
        List<UserEntity> allUser = userRepository.getAllUser();
        Assert.assertEquals(numberOfUserAtTestStart + 1, allUser.size());
    }
}
