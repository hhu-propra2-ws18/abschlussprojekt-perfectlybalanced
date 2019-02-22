package de.hhu.abschlussprojektverleihplattform.controllers.lending;

import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.service.ProductService;
import de.hhu.abschlussprojektverleihplattform.service.UserService;
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

    @Test
    public void emptyTest(){}

    /*
    @Test
    @WithUserDetails("sarah")
    public void lendingRequestTest() throws Exception {


        UserEntity user_owner= RandomTestData.newRandomTestUser();
        userService.addUser(user_owner);
        ProductEntity productEntity =
                RandomTestData
                        .newRandomTestProduct(user_owner,RandomTestData.newRandomTestAddress());
        productService.addProduct(productEntity);


        mockMvc.perform(
                post(
                        RequestALendingController
                                .requestalendingURL+"?id="+productEntity.getId()
                ).with(
                        csrf()
                ).with(

                )
        )
                .andExpect(status().is3xxRedirection());

    }
    */
}
