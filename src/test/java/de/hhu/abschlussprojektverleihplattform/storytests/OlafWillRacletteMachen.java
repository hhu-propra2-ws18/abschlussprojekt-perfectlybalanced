package de.hhu.abschlussprojektverleihplattform.storytests;


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
public class OlafWillRacletteMachen {

    @Autowired
    UserService userService;

    @Autowired
    ProPayService proPayService;

    @Autowired
    LendingService lendingService;

    @Autowired
    ProductService productService;

    @Test
    public void test_olaf_raclette_machen() throws Exception{

        //olaf hat 30 Euro
        UserEntity olaf = RandomTestData.newRandomTestUser();
        olaf.setFirstname("olaf");
        userService.addUser(olaf);
        proPayService.changeUserBalanceBy(olaf.getUsername(),30);

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

        Timestamp[] successivetimestamps = RandomTestData.new2Timestamps1DayApart();

        lendingService
                .requestLending(
                        olaf,raclette,successivetimestamps[0],successivetimestamps[1]);

        //TODO
    }

}