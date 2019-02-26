package de.hhu.abschlussprojektverleihplattform.controllers.lending;

import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import de.hhu.abschlussprojektverleihplattform.model.Lendingstatus;
import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.security.AuthenticatedUserService;
import de.hhu.abschlussprojektverleihplattform.service.LendingService;
import de.hhu.abschlussprojektverleihplattform.service.ProductService;
import de.hhu.abschlussprojektverleihplattform.service.UserService;
import de.hhu.abschlussprojektverleihplattform.service.propay.ProPayService;
import de.hhu.abschlussprojektverleihplattform.utils.RandomTestData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MyLendingsControllerTest {

    @Autowired
    WebApplicationContext context;

    @MockBean
    LendingService lendingService;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    AuthenticatedUserService authenticatedUserService;

    @Autowired
    ProductService productService;

    @Autowired
    ProPayService proPayService;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCanSeeMyLendingRequest() throws Exception {

        UserEntity userOwner = RandomTestData.newRandomTestUser();
        UserEntity userBorrower = RandomTestData.newRandomTestUser();
        ProductEntity productEntity = RandomTestData.newRandomTestProduct(
            userOwner,
            RandomTestData.newRandomTestAddress()
        );

        LendingEntity lending = RandomTestData.newRandomLendingStausDone(userBorrower, productEntity);
        lending.setStatus(Lendingstatus.confirmt);

        List<LendingEntity> confirmedLendings = new ArrayList<>();
        confirmedLendings.add(lending);

        userOwner.setUserId(1L);
        userOwner.setUserId(2L);
        productEntity.setId(1L);
        lending.setId(1L);
/*
        proPayService.changeUserBalanceBy(userBorrower.getUsername(), 100000);

        //user2 wants to lend
        lendingService.requestLending(
            userBorrower,
            productEntity,
            timestamps[0],
            timestamps[1]
        );


        //lending request accepted
        lendingService.acceptLendingRequest(
            lendingService.getAllRequestsForUser(userOwner).get(0)
        );*/

        when(lendingService.getAllLendings()).thenReturn(confirmedLendings);
        when(lendingService.getAllConfirmedLendings(confirmedLendings)).thenReturn(confirmedLendings);
        when(userService.findByUsername(ArgumentMatchers.anyString())).thenReturn(userBorrower);

        //user2 should see the products he is currently lending
        mockMvc
            .perform(get(MyLendingsController.url)
                .with(csrf())
                .with(user(authenticatedUserService
                    .loadUserByUsername(userBorrower.getUsername()))))
            .andExpect(content()
                .string(containsString(
                    productEntity.getTitle())));
    }
}