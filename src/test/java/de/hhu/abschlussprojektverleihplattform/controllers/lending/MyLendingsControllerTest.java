package de.hhu.abschlussprojektverleihplattform.controllers.lending;

import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.security.AuthenticatedUserService;
import de.hhu.abschlussprojektverleihplattform.service.LendingService;
import de.hhu.abschlussprojektverleihplattform.service.ProductService;
import de.hhu.abschlussprojektverleihplattform.service.UserService;
import de.hhu.abschlussprojektverleihplattform.service.propay.ProPayService;
import de.hhu.abschlussprojektverleihplattform.utils.RandomTestData;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MyLendingsControllerTest {

    @Autowired
    LendingService lendingService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Autowired
    AuthenticatedUserService authenticatedUserService;

    @Autowired
    ProductService productService;

    @Autowired
    ProPayService proPayService;

    @Test
    public void doNothing() {
        //eine Testklasse ohne Tests ist nicht zulaessig
        Assert.assertTrue(true);
    }

    //TODO: den richten Test wieder aktivieren sobald die SQL-Abfrage gefixt ist

    @Test
    public void test_can_see_my_lending_request() throws Exception{

        UserEntity user_owner = RandomTestData.newRandomTestUser();
        userService.addUser(user_owner);

        UserEntity user_wannabe_borrower = RandomTestData.newRandomTestUser();
        userService.addUser(user_wannabe_borrower);

        ProductEntity productEntity = RandomTestData.newRandomTestProduct(
            user_owner,
            RandomTestData.newRandomTestAddress()
        );
        productService.addProduct(productEntity);

        Timestamp[] timestamps = RandomTestData.new2SuccessiveTimestamps();

        proPayService.changeUserBalanceBy(user_wannabe_borrower.getUsername(),100000);

        //user2 wants to lend
        boolean requestIsOk = lendingService.requestLending(
            user_wannabe_borrower,
            productEntity,
            timestamps[0],
            timestamps[1]
        );

        System.out.println("request is ok"+requestIsOk);

        //lending request accepted
        boolean acceptIsOk = lendingService.acceptLendingRequest(
            lendingService.getAllRequestsForUser(user_owner).get(0)
        );

        Assert.assertTrue(requestIsOk && acceptIsOk);

        //user2 should see the products he is currently lending
        mockMvc.perform(get(MyLendingsController.url)
            .with(user(authenticatedUserService.loadUserByUsername(
                user_wannabe_borrower.getUsername()
            )))
        )
            .andExpect(content()
                .string(containsString(
                    productEntity.getTitle()
                ))
            );
    }
}
