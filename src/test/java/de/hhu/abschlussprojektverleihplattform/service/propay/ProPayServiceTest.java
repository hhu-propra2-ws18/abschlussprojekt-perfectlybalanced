package de.hhu.abschlussprojektverleihplattform.service.propay;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProPayServiceTest {

    @Autowired
    private ProPayService proPayService;

    private String make_new_user(){
        return RandomStringUtils.random(10,true,false);
    }

    @Test
    public void testnewuserhaszerobalance() throws Exception{
        String generated_username = this.make_new_user();
        Assert.assertEquals(0,this.proPayService.getBalance(generated_username));
    }

    @Test
    public void test_can_create_account() {
        try {
            this.proPayService.createAccountIfNotExists("alice");
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void test_increaseuser_has_positive_balance(){
        try {
            this.proPayService.createAccountIfNotExists("alice");
            this.proPayService.changeUserBalanceBy("alice",10000);

            Assert.assertTrue(this.proPayService.getBalance("alice")>=0);
        }catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void test_can_make_payment() throws Exception{
        String user1 = this.make_new_user();
        String user2 = this.make_new_user();

        this.proPayService.createAccountIfNotExists(user1);
        this.proPayService.createAccountIfNotExists(user2);

        this.proPayService.changeUserBalanceBy(user1,1);
        this.proPayService.makePayment(user1,user2,1);

        Assert.assertEquals(this.proPayService.getBalance(user2),1);
    }
}
