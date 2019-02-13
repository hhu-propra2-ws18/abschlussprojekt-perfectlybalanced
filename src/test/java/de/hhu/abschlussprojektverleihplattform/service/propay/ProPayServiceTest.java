package de.hhu.abschlussprojektverleihplattform.service.propay;

import de.hhu.abschlussprojektverleihplattform.service.propay.model.Account;
import de.hhu.abschlussprojektverleihplattform.service.propay.model.Reservation;
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
        String user1=this.make_new_user();
        try {
            this.proPayService.createAccountIfNotExists(user1);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void test_can_increase_user_balance(){
        try {
            String user1=this.make_new_user();
            this.proPayService.createAccountIfNotExists(user1);
            this.proPayService.changeUserBalanceBy(user1,1);

            Assert.assertTrue(this.proPayService.getBalance(user1)==1);
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

    @Test
    public void can_make_reservation() throws Exception{

        //propay does not work yet. there is an issue in their repository

        /*
        String user1 = this.make_new_user();
        String user2 = this.make_new_user();

        this.proPayService.changeUserBalanceBy(user1,1);

        this.proPayService.makeReservationFromSourceUserToTargetUser(user1,user2,1);

        Account user1_account = this.proPayService.getAccount(user1);

        Reservation[] reservations = user1_account.reservations;

        Assert.assertEquals(reservations.length,1);
        Assert.assertEquals(reservations[0].amount,1);

        */
    }
}
