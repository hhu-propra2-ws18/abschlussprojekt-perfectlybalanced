package de.hhu.abschlussprojektverleihplattform.service.propay;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.service.UserService;
import de.hhu.abschlussprojektverleihplattform.utils.RandomTestData;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static de.hhu.abschlussprojektverleihplattform.service.propay.ProPayUtils.make_new_user;


@RunWith(SpringRunner.class)
@SpringBootTest

public class ProPayAPIBehaviorDocumentation {

    @Autowired
    private ProPayService proPayService;

    @Autowired
    UserService userService;

    @Test
    public void testThatSystemRejectsDecreaseOfAccountBalance() throws Exception{
        String user1 = make_new_user();
        proPayService.createAccountIfNotExists(user1);
        try{
            proPayService.changeUserBalanceBy(user1,-1);
            Assert.fail();
        }catch (Exception e){
            //TODO
        }
    }

    @Test
    public void testPropayApiThatPaymentToNotCreatedAccountSucceeds() throws Exception{
        UserEntity user1 = RandomTestData.newRandomTestUser();
        UserEntity user2 = RandomTestData.newRandomTestUser();
        userService.addUser(user1);
        userService.addUser(user2);

        proPayService.createAccountIfNotExists(user1.getUsername());
        proPayService.changeUserBalanceBy(user1.getUsername(),10);

        proPayService.makePayment(user1.getUsername(),user2.getUsername(),1);
    }
}
