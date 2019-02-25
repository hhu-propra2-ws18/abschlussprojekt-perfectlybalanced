package de.hhu.abschlussprojektverleihplattform.storytests;

import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.service.LendingService;
import de.hhu.abschlussprojektverleihplattform.service.ProductService;
import de.hhu.abschlussprojektverleihplattform.service.UserService;
import de.hhu.abschlussprojektverleihplattform.service.propay.ProPayService;
import de.hhu.abschlussprojektverleihplattform.utils.RandomTestData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KathrinHaeckslerAusleihen {

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
        proPayService.changeUserBalanceBy(kathrin.getUsername(),1000);

        //hacksler product mit besitzer erstellen
        UserEntity owner = RandomTestData.newRandomTestUser();
        userService.addUser(owner);
        ProductEntity hacksler =
                RandomTestData.newRandomTestProduct(
                        owner,RandomTestData.newRandomTestAddress()
                );
        hacksler.setTitle("ein hacksler fuer alle faelle");
        hacksler.setSurety(300);
        hacksler.setCost(5);
        productService.addProduct(hacksler);

        Timestamp[] timestamps = RandomTestData.new2SuccessiveTimestamps();
        long days_2=1000*60*60*24*2;
        timestamps[1].setTime(timestamps[0].getTime()+days_2);
        LendingEntity hacksler_lending = lendingService.requestLending(kathrin, hacksler, timestamps[0], timestamps[1]);

        //leih anfrage wird angenommen
        lendingService.acceptLendingRequest(hacksler_lending);

        //geliehenes produkt wird zurueck gegeben
        lendingService.returnProduct(hacksler_lending);

        //besitzer findet es in guten zustand
        lendingService.acceptReturnedProduct(hacksler_lending);

        //TODO: assert all the assumptions inbetween the actions
    }
}
