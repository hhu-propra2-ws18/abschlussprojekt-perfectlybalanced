package de.hhu.abschlussprojektverleihplattform.service.propay;

import de.hhu.abschlussprojektverleihplattform.service.propay.model.Account;
import de.hhu.abschlussprojektverleihplattform.service.propay.model.Reservation;
import de.hhu.abschlussprojektverleihplattform.utils.RandomTestData;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest

public class ProPayServiceTest {

    @Autowired
    private ProPayService proPayService;

    public static String make_new_user(){
        return RandomTestData.newRandomTestUser().getUsername();
    }

    @Test
    public void testNewUserHasZeroBalance() throws Exception {
        String generated_username = RandomTestData.newRandomTestUser().getUsername();
        Assert.assertEquals(Long.valueOf(0), proPayService.usersCurrentBalance(generated_username));
    }

    @Test
    public void testCanCreateAccount() {
        String user1 = RandomTestData.newRandomTestUser().getUsername();
        try {
            proPayService
                    .proPayAdapter
                    .createAccountIfNotAlreadyExistsAndIncreaseBalanceBy(user1,0);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testCanIncreaseUserBalance() {
        try {
            String user1 = make_new_user();
            proPayService
                    .proPayAdapter
                    .createAccountIfNotAlreadyExistsAndIncreaseBalanceBy(user1,1);

            Assert.assertTrue(this.proPayService.usersCurrentBalance(user1) == 1);
        } catch (Exception e) {
            Assert.fail();
        }
    }



    @Test
    public void testCanMakePayment() throws Exception {
        String user1 = make_new_user();
        String user2 = make_new_user();

        proPayService
                .proPayAdapter
                .createAccountIfNotAlreadyExistsAndIncreaseBalanceBy(user1,0);
        proPayService
                .proPayAdapter
                .createAccountIfNotAlreadyExistsAndIncreaseBalanceBy(user2,0);

        proPayService
                .proPayAdapter
                .createAccountIfNotAlreadyExistsAndIncreaseBalanceBy(user1,1);

        proPayService
                .proPayAdapter
                .makePayment(user1,user2,1);

        Assert.assertEquals(proPayService.usersCurrentBalance(user2), Long.valueOf(1));
    }



    @Test
    public void canMakeReservation() throws Exception {

        //propay does not work yet. there is an issue in their repository

        String user1 = make_new_user();
        String user2 = make_new_user();

        proPayService.proPayAdapter.createAccountIfNotAlreadyExistsAndIncreaseBalanceBy(user2,0);
        proPayService.proPayAdapter.createAccountIfNotAlreadyExistsAndIncreaseBalanceBy(user1,10);

        proPayService.reservateAmount(user1, user2, 1);

        Account user1_account = this.proPayService.proPayAdapter.getAccount(user1);

        Reservation[] reservations = user1_account.reservations;

        Assert.assertEquals(reservations.length, 1);
        Assert.assertEquals(reservations[0].amount, 1);


    }

    @Test
    public void canReleaseReservation() throws Exception {
        //make users
        String user1 = make_new_user();
        String user2 = make_new_user();
        proPayService.proPayAdapter.createAccountIfNotAlreadyExistsAndIncreaseBalanceBy(user2,0);

        proPayService.proPayAdapter.createAccountIfNotAlreadyExistsAndIncreaseBalanceBy(user1,10);

        //make reservation
        Reservation reservation
            = proPayService.proPayAdapter.makeReservation(user1, user2, 1);

        Assert.assertEquals(proPayService.proPayAdapter.getAccount(user1).reservations.length, 1);

        //release reservation
        proPayService.returnReservatedMoney(user1, reservation.id);

        //check account for reserved money and no reservations present
        Account user1_account = proPayService.proPayAdapter.getAccount(user1);

        Assert.assertEquals(user1_account.reservations.length, 0);
    }

    @Test
    public void canPunishReservation() throws Exception{
        //make users
        String user1 = make_new_user();
        String user2 = make_new_user();

        proPayService.proPayAdapter.createAccountIfNotAlreadyExistsAndIncreaseBalanceBy(user2,0);
        proPayService.proPayAdapter.createAccountIfNotAlreadyExistsAndIncreaseBalanceBy(user1,10);

        Reservation reservation=proPayService
            .proPayAdapter.makeReservation(user1,user2,1);

        proPayService.proPayAdapter.punishReservation(user1,reservation.id);

        //assert that user1 has no reservations anymore
        Assert.assertEquals(0,proPayService.proPayAdapter.getAccount(user1).reservations.length);

        //assert that user2 received the reserved amount
        Assert.assertEquals(1,proPayService.proPayAdapter.getAccount(user2).amount);
    }
}