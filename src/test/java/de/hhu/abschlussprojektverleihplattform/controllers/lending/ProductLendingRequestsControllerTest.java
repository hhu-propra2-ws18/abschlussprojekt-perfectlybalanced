package de.hhu.abschlussprojektverleihplattform.controllers.lending;

import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import de.hhu.abschlussprojektverleihplattform.model.Lendingstatus;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.security.AuthenticatedUserService;
import de.hhu.abschlussprojektverleihplattform.service.LendingService;
import de.hhu.abschlussprojektverleihplattform.service.UserService;
import de.hhu.abschlussprojektverleihplattform.utils.RandomTestData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductLendingRequestsControllerTest {

    @Autowired
    private ProductLendingRequestsController controller;

    @MockBean
    private LendingService lendingService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    AuthenticatedUserService authenticatedUserService;

    private Random randomID = new Random();

    @Test
    public void contexLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    public void testcontrolleristhere() throws Exception {
        UserEntity randomUser = RandomTestData.newRandomTestUser();
        randomUser.setUserId(randomID.nextLong());

        when(userService.findByUsername(randomUser.getUsername())).thenReturn(randomUser);

        mockMvc.perform(get("/lendingrequests")
                .with(user(authenticatedUserService.loadUserByUsername(
                        randomUser.getUsername()
                )))
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Leihanfragen")));
    }

    @Test
    public void rejectRequest() throws Exception {
        UserEntity randomUser = RandomTestData.newRandomTestUser();
        randomUser.setUserId(randomID.nextLong());

        LendingEntity lending = new LendingEntity();
        lending.setId(randomID.nextLong());
        lending.setStatus(Lendingstatus.requested);
        System.out.println(lending);

        when(userService.findByUsername(randomUser.getUsername())).thenReturn(randomUser);

        mockMvc.perform(post(
                ProductLendingRequestsController.lendingRequestsRejectURL +"?id=" +lending.getId())
                .with(csrf())
                .with(user(authenticatedUserService.loadUserByUsername(
                        randomUser.getUsername()
                )))
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(ProductLendingRequestsController.class));
        verify(lendingService).getLendingById(lending.getId());
        verify(lendingService).denyLendingRequest(null);
    }

    @Test
    public void acceptRequest() throws Exception {
        UserEntity randomUser = RandomTestData.newRandomTestUser();
        randomUser.setUserId(randomID.nextLong());
        LendingEntity lending = new LendingEntity();
        lending.setId(randomID.nextLong());
        lending.setStatus(Lendingstatus.requested);
        System.out.println(lending);

        when(userService.findByUsername(randomUser.getUsername())).thenReturn(randomUser);

        mockMvc
            .perform(post(
                ProductLendingRequestsController.lendingRequestsAcceptURL+"?id=" +lending.getId())
                .with(csrf())
                .with(user(authenticatedUserService
                    .loadUserByUsername(randomUser.getUsername()))))
            .andExpect(status().is3xxRedirection())
            .andExpect(handler().handlerType(ProductLendingRequestsController.class));

        verify(lendingService).getLendingById(lending.getId());
        verify(lendingService).acceptLendingRequest(null);
    }
}
