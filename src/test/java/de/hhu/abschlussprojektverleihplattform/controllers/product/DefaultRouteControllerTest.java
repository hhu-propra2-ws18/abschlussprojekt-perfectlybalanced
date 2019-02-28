package de.hhu.abschlussprojektverleihplattform.controllers.product;


import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.security.AuthenticatedUserService;
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

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DefaultRouteControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    AuthenticatedUserService authenticatedUserService;


    @Test
    public void testControllerIsThere() throws Exception {
        UserEntity randomUser = RandomTestData.newRandomTestUser();

        when(userService.findByUsername(randomUser.getUsername())).thenReturn(randomUser);

        mockMvc
            .perform(get("/")
                .with(user(authenticatedUserService.loadUserByUsername(randomUser.getUsername()))))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Verleihplattform")))
            .andExpect(content().string(containsString("Logout")))
            .andExpect(content().string(containsString("Meine Produkte")))
            .andExpect(content().string(containsString("Neues Produkt")))
            .andExpect(content().string(containsString("Start")));
    }
}
