package de.hhu.abschlussprojektverleihplattform.storytests;

import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.service.LendingService;
import de.hhu.abschlussprojektverleihplattform.service.ProductService;
import de.hhu.abschlussprojektverleihplattform.service.UserService;
import de.hhu.abschlussprojektverleihplattform.service.propay.ProPayService;
import de.hhu.abschlussprojektverleihplattform.service.propay.model.Account;
import de.hhu.abschlussprojektverleihplattform.service.propay.model.Reservation;
import de.hhu.abschlussprojektverleihplattform.utils.RandomTestData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KathrinHaeckslerAusleihenTest {

    @Autowired
    UserService userService;

    @Autowired
    ProPayService proPayService;

    @Autowired
    LendingService lendingService;

    @Autowired
    ProductService productService;

    @Test
    public void test() throws Exception{

        //user kathrin mit geld erstellen
        UserEntity kathrin = RandomTestData.newRandomTestUser();
        kathrin.setFirstname("kathrin");
        userService.addUser(kathrin);

        long kathrin_balance_before=500;

        proPayService.changeUserBalanceBy(kathrin.getUsername(),kathrin_balance_before);

        //hacksler product mit besitzer erstellen
        UserEntity owner = RandomTestData.newRandomTestUser();
        userService.addUser(owner);
        ProductEntity hacksler =
                RandomTestData.newRandomTestProduct(
                        owner,RandomTestData.newRandomTestAddress()
                );
        hacksler.setTitle("ein hacksler fuer alle faelle");
        int hacksler_surety=300;
        hacksler.setSurety(hacksler_surety);

        int hacksler_cost=5;
        hacksler.setCost(hacksler_cost);
        productService.addProduct(hacksler);

        long days_2=1000*60*60*24*2;
        Long currentMilis = Timestamp.valueOf(LocalDateTime.now()).getTime();
        Timestamp start = new Timestamp(currentMilis + 30000);
        Timestamp end = new Timestamp(currentMilis + days_2);
        LendingEntity hacksler_lending =
                lendingService.requestLending(kathrin, hacksler, start, end);

        //leih anfrage wird angenommen
        lendingService.acceptLendingRequest(hacksler_lending);

        //assert that amount is reserved on kathrins account
        Reservation[] reservations
         = proPayService.getAccount(kathrin.getUsername()).reservations;
        assertEquals(reservations.length,1);
        assertEquals(reservations[0].amount,hacksler_surety);

        //geliehenes produkt wird zurueck gegeben
        lendingService.returnProduct(hacksler_lending);

        //besitzer findet es in guten zustand
        lendingService.acceptReturnedProduct(hacksler_lending);

        Account kathrin_account_after_lending=
                proPayService.getAccount(kathrin.getUsername());

        //assert that reservation is released and the lending cost is paid
        assertEquals(
                kathrin_account_after_lending.amount,
                kathrin_balance_before-hacksler_cost*2
        );
        assertEquals(kathrin_account_after_lending.reservations.length,0);

        assertEquals(proPayService.getAccount(owner.getUsername()).amount,hacksler_cost*2);
    }
}
