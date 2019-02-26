package de.hhu.abschlussprojektverleihplattform.service.propay;

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
        String user1 = make_new_user();
        String user2 = make_new_user();

        proPayService.createAccountIfNotExists(user1);
        proPayService.changeUserBalanceBy(user1,10);

        proPayService.makePayment(user1,user2,1);
    }
}
