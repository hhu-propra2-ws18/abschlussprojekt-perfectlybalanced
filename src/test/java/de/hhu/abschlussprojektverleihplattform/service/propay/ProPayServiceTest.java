package de.hhu.abschlussprojektverleihplattform.service.propay;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;


public class ProPayServiceTest {

    private String make_new_user(){
        return RandomStringUtils.random(10,true,false);
    }

    @Test
    public void testnewuserhaszerobalance() throws Exception{
        String generated_username = this.make_new_user();
        Assert.assertEquals(0,ProPayService.getInstance().getBalance(generated_username));
    }

    @Test
    public void test_can_create_account() {
        try {
            ProPayService.getInstance().createAccountIfNotExists("alice");
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void test_increaseuser_has_positive_balance(){
        try {
            ProPayService.getInstance().createAccountIfNotExists("alice");
            ProPayService.getInstance().changeUserBalanceBy("alice",10000);

            Assert.assertTrue(ProPayService.getInstance().getBalance("alice")>=0);
        }catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void test_can_make_payment() throws Exception{
        String user1 = this.make_new_user();
        String user2 = this.make_new_user();

        ProPayService.getInstance().createAccountIfNotExists(user1);
        ProPayService.getInstance().createAccountIfNotExists(user2);

        ProPayService.getInstance().changeUserBalanceBy(user1,1);
        ProPayService.getInstance().makePayment(user1,user2,1);

        Assert.assertEquals(ProPayService.getInstance().getBalance(user2),1);
    }
}
