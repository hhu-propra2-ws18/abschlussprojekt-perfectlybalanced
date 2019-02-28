package de.hhu.abschlussprojektverleihplattform.service;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.utils.RandomTestData;
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
        UserEntity user = RandomTestData.newRandomTestUser();
        userService.addUser(user);
        UserEntity expected = userService.findByUsername(user.getUsername());

        Assert.assertEquals(user,expected);
    }

    @Test
    public void findUserById() {
        UserEntity newUser = RandomTestData.newRandomTestUser();
        userService.addUser(newUser);
        UserEntity loadedUser = userService.showUserById(newUser.getUserId());
        Assert.assertEquals(newUser,loadedUser);
    }

    @Test
    public void cannotFindUserById(){
        try {
            UserEntity test = userService.showUserById(48329L);
            Assert.fail();
        }catch (Exception e){
            //pass
        }
    }
}
