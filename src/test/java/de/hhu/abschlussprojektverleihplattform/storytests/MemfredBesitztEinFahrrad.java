package de.hhu.abschlussprojektverleihplattform.storytests;

import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemfredBesitztEinFahrrad {

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

        UserEntity memfred = RandomTestData.newRandomTestUser();
        memfred.setFirstname("memfred");

        userService.addUser(memfred);

        ProductEntity fahrrad = RandomTestData
                .newRandomTestProduct(memfred,RandomTestData.newRandomTestAddress());

        int fahrrad_cost=4;
        int fahrrad_surety=100;

        fahrrad.setCost(fahrrad_cost);
        fahrrad.setSurety(fahrrad_surety);

        productService.addProduct(fahrrad);

        UserEntity borrower = RandomTestData.newRandomTestUser();
        userService.addUser(borrower);
        int borrower_old_wealth=1000;
        proPayService.changeUserBalanceBy(borrower.getUsername(),borrower_old_wealth);

        Timestamp[] timespan = RandomTestData.new2Timestamps1DayApart();

        LendingEntity fahrrad_lending =
                lendingService.requestLending(borrower,fahrrad,timespan[0],timespan[1]);

        lendingService.acceptLendingRequest(fahrrad_lending);

        //borrower returns bike in bad condition
        lendingService.returnProduct(fahrrad_lending);

        //memfred creates a conflict
        lendingService.denyReturnedProduct(fahrrad_lending);

        //conflict is decided for memfred
        lendingService.ownerReceivesSuretyAfterConflict(fahrrad_lending);

        //memfred receives cost and surety
        assertEquals(
                proPayService.getAccount(memfred.getUsername()).amount,
                fahrrad_cost+fahrrad_surety
        );
    }
}
