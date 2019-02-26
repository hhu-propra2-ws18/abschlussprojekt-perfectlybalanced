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

import java.sql.Timestamp;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RequestALendingControllerTest {

    @Autowired
    WebApplicationContext context;

    @MockBean
    ProductService productService;

    @MockBean
    LendingService lendingService;

    @MockBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AuthenticatedUserService authenticatedUserService;

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
    public void lendingRequestTest() throws Exception {

        // Arrange
        UserEntity userBorrower = RandomTestData.newRandomTestUser();

        UserEntity userOwner = RandomTestData.newRandomTestUser();
        ProductEntity productEntity =
            RandomTestData
                .newRandomTestProduct(userOwner, RandomTestData.newRandomTestAddress());

        LendingEntity lending = RandomTestData.newRandomLendingStausDone(userBorrower, productEntity);
        userBorrower.setUserId(1L);
        userOwner.setUserId(2L);
        productEntity.setId(1L);
        lending.setId(1L);
        lending.setStatus(Lendingstatus.requested);
        
        // Mockito
        when(productService.getById(ArgumentMatchers.anyLong()))
            .thenReturn(productEntity);
        when(lendingService
                .requestLending(any(UserEntity.class), any(ProductEntity.class), any(Timestamp.class), any(Timestamp.class)))
            .thenReturn(lending);
        when(userService.findByUsername(anyString()))
            .thenReturn(userBorrower);

        // Act & Assert
        mockMvc.perform(
            post(
                RequestALendingController
                    .requestalendingURL + "?id=" + productEntity.getId())
                .param("start", "2019-02-25T15:15")
                .param("end", "2019-02-26T15:15")
                .with(csrf())
                .with(
                    user(authenticatedUserService
                        .loadUserByUsername(userBorrower.getUsername())))


        )
            .andExpect(status().is3xxRedirection());

    }
}