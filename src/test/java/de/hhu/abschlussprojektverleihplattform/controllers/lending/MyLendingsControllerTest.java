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
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
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

    @MockBean
    UserService userService;

    @Autowired
    AuthenticatedUserService authenticatedUserService;

    @MockBean
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
    public void testCanSeeMyLendingRequest() throws Exception{
        UserEntity user_owner = RandomTestData.newRandomTestUser();
        user_owner.setUserId(5L);
        UserEntity user_wannabe_borrower = RandomTestData.newRandomTestUser();
        user_wannabe_borrower.setUserId(2L);
        ProductEntity productEntity = RandomTestData.newRandomTestProduct(
            user_owner,
            RandomTestData.newRandomTestAddress()
        );
        productEntity.setId(1L);
        Timestamp[] timestamps = RandomTestData.new2SuccessiveTimestamps();

        when(userService.findByUsername(
                user_wannabe_borrower.getUsername()))
                .thenReturn(user_wannabe_borrower);
        when(userService.findByUsername(
                user_owner.getUsername()))
                .thenReturn(user_owner);
        when(productService.getById(
                productEntity.getId()))
                .thenReturn(productEntity);

        proPayService.changeUserBalanceBy(user_wannabe_borrower.getUsername(),100000);

        //user2 wants to lend
        lendingService.requestLending(
            user_wannabe_borrower,
            productEntity,
            timestamps[0],
            timestamps[1]
        );


        //lending request accepted
        lendingService.acceptLendingRequest(
            lendingService.getAllRequestsForUser(user_owner).get(0)
        );


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
