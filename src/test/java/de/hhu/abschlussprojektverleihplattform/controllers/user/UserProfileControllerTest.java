package de.hhu.abschlussprojektverleihplattform.controllers.user;


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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserProfileControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    AuthenticatedUserService authenticatedUserService;


    @Test
    public void testControllerIsThere() throws Exception {
        UserEntity user = RandomTestData.newRandomTestUser();
        user.setUserId(1L);

        when(userService.findByUsername(user.getUsername())).thenReturn(user);

        mockMvc.perform(get("/profile")
                .with(user(authenticatedUserService.loadUserByUsername(
                        user.getUsername()
                )))
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Profile")))
                .andExpect(content().string(containsString("Email")))
                .andExpect(content().string(containsString("Benutzername")))
                .andExpect(content().string(containsString("Kontostand")));
    }

    @Test
    public void testSarahCanDepositMoneyAndSeeHerBalance() throws Exception{
        UserEntity user = RandomTestData.newRandomTestUser();
        user.setUserId(1L);

        when(userService.findByUsername(user.getUsername())).thenReturn(user);

        mockMvc.perform(post("/profile/deposit")
                .with(csrf())
                .with(user(authenticatedUserService.loadUserByUsername(user.getUsername())))
        )
            .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/profile")
            .with(user(authenticatedUserService.loadUserByUsername(user.getUsername())))
        ).andExpect(content().string(containsString(""+100)));
        //because /deposit deposits 100 Euros
    }
}
