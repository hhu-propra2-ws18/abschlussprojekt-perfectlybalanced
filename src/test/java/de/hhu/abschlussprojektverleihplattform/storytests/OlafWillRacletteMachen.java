package de.hhu.abschlussprojektverleihplattform.storytests;


import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import de.hhu.abschlussprojektverleihplattform.model.Lendingstatus;
import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.service.LendingService;
import de.hhu.abschlussprojektverleihplattform.service.ProductService;
import de.hhu.abschlussprojektverleihplattform.service.UserService;
import de.hhu.abschlussprojektverleihplattform.service.propay.adapter.ProPayAdapter;
import de.hhu.abschlussprojektverleihplattform.service.propay.ProPayService;
import de.hhu.abschlussprojektverleihplattform.service.propay.model.Reservation;
import de.hhu.abschlussprojektverleihplattform.utils.RandomTestData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OlafWillRacletteMachen {

    @Autowired
    UserService userService;

    @Autowired
    ProPayService proPayService;

    @Autowired
    LendingService lendingService;

    @Autowired
    ProductService productService;

    @Autowired
    ProPayAdapter proPayAdapter;

    @Test
    public void test_olaf_raclette_machen() throws Exception{

        //olaf hat 30 Euro
        long olaf_old_wealth=30;
        UserEntity olaf = RandomTestData.newRandomTestUser();
        olaf.setFirstname("olaf");
        userService.addUser(olaf);
        proPayAdapter
                .createAccountIfNotAlreadyExistsAndIncreaseBalanceBy(
                        olaf.getUsername(),
                        olaf_old_wealth);

        UserEntity raclette_owner = RandomTestData.newRandomTestUser();
        userService.addUser(raclette_owner);

        ProductEntity raclette =
                RandomTestData.newRandomTestProduct(
                        raclette_owner,
                        RandomTestData.newRandomTestAddress()
                );
        raclette.setTitle("Raclette Grill");
        raclette.setCost(2);
        raclette.setSurety(10);

        productService.addProduct(raclette);

        long day=1000*60*60*24;
        Long currentMilis = Timestamp.valueOf(LocalDateTime.now()).getTime();
        Timestamp start = new Timestamp(currentMilis + 300000);
        Timestamp end = new Timestamp(currentMilis + day);

        LendingEntity raclette_lending = lendingService
                .requestLending(
                        olaf,raclette,start,end);



        lendingService.acceptLendingRequest(raclette_lending);

        assertEquals(Lendingstatus.confirmt,raclette_lending.getStatus());


        assertEquals(
                Arrays.stream(proPayAdapter.getAccount(olaf.getUsername()).reservations)
                        .reduce((r1,r2)->{
                            Reservation result = new Reservation();
                            result.amount=r1.amount+r2.amount;
                            return result;
                        }
                        ).map(reservation -> reservation.amount).get().longValue(),
                10L
        );

        lendingService.returnProduct(raclette_lending);
        assertEquals(Lendingstatus.returned,raclette_lending.getStatus());

        lendingService.acceptReturnedProduct(raclette_lending);
        assertEquals(Lendingstatus.done,raclette_lending.getStatus());

        assertEquals(
                proPayAdapter.getAccount(olaf.getUsername()).amount,
                olaf_old_wealth-raclette.getCost()
        );

        assertEquals(
                proPayAdapter.getAccount(olaf.getUsername()).reservations.length,
                0
        );


    }

}