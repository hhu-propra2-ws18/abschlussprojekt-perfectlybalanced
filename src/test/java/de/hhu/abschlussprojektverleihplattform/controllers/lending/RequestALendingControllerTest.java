package de.hhu.abschlussprojektverleihplattform.controllers.lending;

import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.security.AuthenticatedUserService;
import de.hhu.abschlussprojektverleihplattform.service.ProductService;
import de.hhu.abschlussprojektverleihplattform.service.UserService;
import de.hhu.abschlussprojektverleihplattform.service.propay.ProPayService;
import de.hhu.abschlussprojektverleihplattform.utils.RandomTestData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RequestALendingControllerTest {

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AuthenticatedUserService authenticatedUserService;

    @Autowired
    ProPayService proPayService;

    @Test
    public void lendingRequestTest() throws Exception {
        UserEntity user_wannabe_borrower= RandomTestData.newRandomTestUser();
        userService.addUser(user_wannabe_borrower);

        proPayService.changeUserBalanceBy(user_wannabe_borrower.getUsername(),99999);

        UserEntity user_owner= RandomTestData.newRandomTestUser();
        userService.addUser(user_owner);
        ProductEntity productEntity =
                RandomTestData
                        .newRandomTestProduct(user_owner,RandomTestData.newRandomTestAddress());
        productService.addProduct(productEntity);

        System.out.println(productEntity.toString());


        mockMvc.perform(
                post(
                        RequestALendingController
                                .requestalendingURL+"?id="+productEntity.getId())
                        .param("start", "2019-02-25T15:15")
                        .param("end", "2019-02-26T15:15")
                        .with(csrf())
                        .with(
                                user(authenticatedUserService
                                        .loadUserByUsername(user_wannabe_borrower.getUsername())))


        )
                .andExpect(status().is3xxRedirection());

    }

}