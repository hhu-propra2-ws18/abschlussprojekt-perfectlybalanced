package de.hhu.abschlussprojektverleihplattform.service.propay;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.service.UserService;
import de.hhu.abschlussprojektverleihplattform.utils.RandomTestData;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest

public class ProPayAPIBehaviorDocumentation {

    @Autowired
    private ProPayService proPayService;

    @Autowired
    UserService userService;

    @Test
    public void testPropayApiThatPaymentToNotCreatedAccountSucceeds() throws Exception{
        UserEntity user1 = RandomTestData.newRandomTestUser();
        UserEntity user2 = RandomTestData.newRandomTestUser();
        userService.addUser(user1);
        userService.addUser(user2);

        proPayService
                .proPayAdapter
                .createAccountIfNotAlreadyExistsAndIncreaseBalanceBy(user1.getUsername(),10);


        proPayService.proPayAdapter.makePayment(user1.getUsername(),user2.getUsername(),1);
    }
}
