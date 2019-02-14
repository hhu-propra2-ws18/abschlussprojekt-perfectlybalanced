package de.hhu.abschlussprojektverleihplattform.service.propay;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static de.hhu.abschlussprojektverleihplattform.service.propay.ProPayUtils.make_new_user;

public class ProPayAPIBehaviorDocumentation {


    @Autowired
    private ProPayService proPayService;

    @Test
    public void test_that_system_rejects_decrease_of_account_balance() throws Exception{
        String user1 = make_new_user();
        proPayService.createAccountIfNotExists(user1);
        try{
            proPayService.changeUserBalanceBy(user1,-1);
            Assert.fail();
        }catch (Exception e){

        }
    }

    @Test
    public void test_propay_api_that_payment_to_not_created_account_succeeds() throws Exception{
        String user1 = make_new_user();
        String user2 = make_new_user();

        proPayService.createAccountIfNotExists(user1);
        proPayService.changeUserBalanceBy(user1,10);

        proPayService.makePayment(user1,user2,1);
    }
}
