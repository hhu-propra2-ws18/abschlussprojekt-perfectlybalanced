package de.hhu.abschlussprojektverleihplattform.service.propay;

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

    @Test
    public void testPropayApiThatPaymentToNotCreatedAccountSucceeds() throws Exception{
        String user1 = ProPayServiceTest.make_new_user();
        String user2 = ProPayServiceTest.make_new_user();

        proPayService.proPayAdapter.createAccountIfNotAlreadyExistsAndIncreaseBalanceBy(user1,10);

        proPayService.proPayAdapter.makePayment(user1,user2,1);
    }
}
