package de.hhu.abschlussprojektverleihplattform.service.propay;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.service.UserService;
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

import static de.hhu.abschlussprojektverleihplattform.service.propay.ProPayUtils.make_new_user;

@RunWith(SpringRunner.class)
@SpringBootTest

public class ProPayServiceTest {

    @Autowired
    private ProPayService proPayService;

    @Autowired
    UserService userService;

    @Test
    public void testNewUserHasZeroBalance() throws Exception {
        String generated_username = make_new_user();
        Assert.assertEquals(0, this.proPayService.getBalance(generated_username));
    }

    @Test
    public void testCanCreateAccount() {
        String user1 = make_new_user();
        try {
            this.proPayService.createAccountIfNotExists(user1);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testCanIncreaseUserBalance() {
        UserEntity user = RandomTestData.newRandomTestUser();
        userService.addUser(user);
        try {
            this.proPayService.createAccountIfNotExists(user.getUsername());
            this.proPayService.changeUserBalanceBy(user.getUsername(), 1);

            Assert.assertTrue(this.proPayService.getBalance(user.getUsername()) == 1);
        } catch (Exception e) {
            Assert.fail();
        }
    }



    @Test
    public void testCanMakePayment() throws Exception {
        UserEntity user1 = RandomTestData.newRandomTestUser();
        UserEntity user2 = RandomTestData.newRandomTestUser();
        userService.addUser(user1);
        userService.addUser(user2);

        this.proPayService.createAccountIfNotExists(user1.getUsername());
        this.proPayService.createAccountIfNotExists(user2.getUsername());

        this.proPayService.changeUserBalanceBy(user1.getUsername(), 1);
        this.proPayService.makePayment(user1.getUsername(), user2.getUsername(), 1);

        Assert.assertEquals(this.proPayService.getBalance(user2.getUsername()), 1);
    }



    @Test
    public void canMakeReservation() throws Exception {

        //propay does not work yet. there is an issue in their repository

        UserEntity user1 = RandomTestData.newRandomTestUser();
        UserEntity user2 = RandomTestData.newRandomTestUser();
        userService.addUser(user1);
        userService.addUser(user2);

        //to create their accounts
        this.proPayService.createAccountIfNotExists(user1.getUsername());
        this.proPayService.createAccountIfNotExists(user2.getUsername());

        //because their hibernate has an exception where a transient instance is not saved
        //which is a property of their Reservation Entity.
        //so we have to make them save both accounts
        //so they can save that reservation
        //the amount has to be not 0

        proPayService.changeUserBalanceBy(user1.getUsername(),10);

        this.proPayService.makeReservationFromSourceUserToTargetUser(user1.getUsername(),
                user2.getUsername(),
                1);

        Account user1_account = this.proPayService.getAccount(user1.getUsername());

        Reservation[] reservations = user1_account.reservations;

        Assert.assertEquals(reservations.length, 1);
        Assert.assertEquals(reservations[0].amount, 1);


    }

    @Test
    public void canReleaseReservation() throws Exception {
        //make users
        UserEntity user1 = RandomTestData.newRandomTestUser();
        UserEntity user2 = RandomTestData.newRandomTestUser();
        userService.addUser(user1);
        userService.addUser(user2);

        proPayService.createAccountIfNotExists(user1.getUsername());
        proPayService.createAccountIfNotExists(user2.getUsername());

        proPayService.changeUserBalanceBy(user1.getUsername(), 10);

        //make reservation
        Reservation reservation
            = proPayService.makeReservationFromSourceUserToTargetUser(user1.getUsername(),
                user2.getUsername(),
                1);

        Assert.assertEquals(proPayService.getAccount(user1.getUsername()).reservations.length, 1);

        //release reservation
        proPayService.returnReservedAmount(user1.getUsername(), reservation.id);

        //check account for reserved money and no reservations present
        Account user1_account = proPayService.getAccount(user1.getUsername());

        Assert.assertEquals(user1_account.reservations.length, 0);
    }

    @Test
    public void canPunishReservation() throws Exception{
        //make users
        UserEntity user1 = RandomTestData.newRandomTestUser();
        UserEntity user2 = RandomTestData.newRandomTestUser();
        userService.addUser(user1);
        userService.addUser(user2);

        proPayService.createAccountIfNotExists(user1.getUsername());
        proPayService.createAccountIfNotExists(user2.getUsername());

        proPayService.changeUserBalanceBy(user1.getUsername(),10);

        Reservation reservation=proPayService
            .makeReservationFromSourceUserToTargetUser(user1.getUsername(),user2.getUsername(),1);

        proPayService.punishReservedAmount(user1.getUsername(),reservation.id);

        //assert that user1 has no reservations anymore
        Assert.assertEquals(0,
                proPayService.getAccount(user1.getUsername()).reservations.length);

        //assert that user2 received the reserved amount
        Assert.assertEquals(1,proPayService.getAccount(user2.getUsername()).amount);
    }
}