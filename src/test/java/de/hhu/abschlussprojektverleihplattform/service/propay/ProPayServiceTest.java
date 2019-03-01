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
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest

public class ProPayServiceTest {

    @Autowired
    private ProPayService proPayService;

    @Autowired
    UserService userService;

    public static String make_new_user(){
        return RandomTestData.newRandomTestUser().getUsername();
    }

    @Test
    public void testNewUserHasZeroBalance() throws Exception {
        UserEntity user = RandomTestData.newRandomTestUser();
        userService.addUser(user);
        String generated_username = user.getUsername();
        Assert.assertEquals(Long.valueOf(0),
                proPayService.usersCurrentBalance(generated_username));
    }

    @Test
    public void testCanCreateAccount() {
        UserEntity user = RandomTestData.newRandomTestUser();
        userService.addUser(user);
        String user1 = user.getUsername();
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
        UserEntity user = RandomTestData.newRandomTestUser();
        userService.addUser(user);
        try {
            proPayService
                    .proPayAdapter
                    .createAccountIfNotAlreadyExistsAndIncreaseBalanceBy(user.getUsername(),1);

            Assert.assertEquals(1, (long) this.proPayService.usersCurrentBalance(user.getUsername()));
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

        proPayService
                .proPayAdapter
                .createAccountIfNotAlreadyExistsAndIncreaseBalanceBy(user1.getUsername(),0);
        proPayService
                .proPayAdapter
                .createAccountIfNotAlreadyExistsAndIncreaseBalanceBy(user2.getUsername(),0);

        proPayService
                .proPayAdapter
                .createAccountIfNotAlreadyExistsAndIncreaseBalanceBy(user1.getUsername(),1);

        proPayService
                .proPayAdapter
                .makePayment(user1.getUsername(),user2.getUsername(),1);

        Assert.assertEquals(
                proPayService.usersCurrentBalance(user2.getUsername()), Long.valueOf(1));
    }



    @Test
    public void canMakeReservation() throws Exception {

        //propay does not work yet. there is an issue in their repository

        UserEntity user1 = RandomTestData.newRandomTestUser();
        UserEntity user2 = RandomTestData.newRandomTestUser();
        userService.addUser(user1);
        userService.addUser(user2);

        proPayService
                .proPayAdapter
                .createAccountIfNotAlreadyExistsAndIncreaseBalanceBy(user2.getUsername(),0);
        proPayService
                .proPayAdapter
                .createAccountIfNotAlreadyExistsAndIncreaseBalanceBy(user1.getUsername(),10);

        proPayService.reservateAmount(user1.getUsername(), user2.getUsername(), 1);

        Account user1_account = this.proPayService.proPayAdapter.getAccount(user1.getUsername());

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

        proPayService
                .proPayAdapter
                .createAccountIfNotAlreadyExistsAndIncreaseBalanceBy(user2.getUsername(),0);

        proPayService
                .proPayAdapter
                .createAccountIfNotAlreadyExistsAndIncreaseBalanceBy(user1.getUsername(),10);


        //make reservation
        Reservation reservation
            = proPayService
                .proPayAdapter
                .makeReservation(user1.getUsername(), user2.getUsername(), 1);

        Assert.assertEquals(proPayService
                .proPayAdapter
                .getAccount(user1.getUsername()).reservations.length, 1);

        //release reservation
        proPayService.returnReservatedMoney(user1.getUsername(), reservation.id);

        //check account for reserved money and no reservations present
        Account user1_account = proPayService.proPayAdapter.getAccount(user1.getUsername());

        Assert.assertEquals(user1_account.reservations.length, 0);
    }

    @Test
    public void canPunishReservation() throws Exception{
        //make users
        UserEntity user1 = RandomTestData.newRandomTestUser();
        UserEntity user2 = RandomTestData.newRandomTestUser();
        userService.addUser(user1);
        userService.addUser(user2);

        proPayService
                .proPayAdapter
                .createAccountIfNotAlreadyExistsAndIncreaseBalanceBy(user2.getUsername(),0);
        proPayService
                .proPayAdapter
                .createAccountIfNotAlreadyExistsAndIncreaseBalanceBy(user1.getUsername(),10);

        Reservation reservation=proPayService
            .proPayAdapter
                .makeReservation(user1.getUsername(),user2.getUsername(),1);

        proPayService
                .proPayAdapter
                .punishReservation(user1.getUsername(),reservation.id);

        //assert that user1 has no reservations anymore
        Assert.assertEquals(0,
                proPayService.proPayAdapter.getAccount(user1.getUsername()).reservations.length);

        //assert that user2 received the reserved amount
        Assert.assertEquals(1,
                proPayService.proPayAdapter.getAccount(user2.getUsername()).amount);
    }
}