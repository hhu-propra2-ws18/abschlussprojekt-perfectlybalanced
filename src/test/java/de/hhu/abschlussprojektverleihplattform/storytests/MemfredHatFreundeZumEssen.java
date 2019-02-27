package de.hhu.abschlussprojektverleihplattform.storytests;

import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.service.LendingService;
import de.hhu.abschlussprojektverleihplattform.service.ProductService;
import de.hhu.abschlussprojektverleihplattform.service.UserService;
import de.hhu.abschlussprojektverleihplattform.service.propay.ProPayService;
import de.hhu.abschlussprojektverleihplattform.utils.RandomTestData;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;

@RunWith(SpringRunner.class)
@SpringBootTest

public class MemfredHatFreundeZumEssen {

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

        //memfred hat 30 Euro
        UserEntity memfred = RandomTestData.newRandomTestUser();
        memfred.setFirstname("memfred");
        userService.addUser(memfred);
        proPayService.changeUserBalanceBy(memfred.getUsername(),30);

        UserEntity teller_owner = RandomTestData.newRandomTestUser();
        userService.addUser(teller_owner);

        ProductEntity teller_3 =
                RandomTestData.newRandomTestProduct(
                        teller_owner,
                        RandomTestData.newRandomTestAddress()
                );
        teller_3.setTitle("3 Teller");
        teller_3.setCost(1);
        teller_3.setSurety(40);

        productService.addProduct(teller_3);

        Timestamp[] timestamps = RandomTestData.new2Timestamps1DayApart();
        try {
            lendingService.requestLending(memfred, teller_3, timestamps[0], timestamps[1]);
            Assert.fail();
        }catch (Exception e){
            System.out.println();
        }
    }
}
