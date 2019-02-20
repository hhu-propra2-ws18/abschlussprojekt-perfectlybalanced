package de.hhu.abschlussprojektverleihplattform.controllers;


import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.repository.UserRepository;
import de.hhu.abschlussprojektverleihplattform.security.AuthenticatedUserService;
import de.hhu.abschlussprojektverleihplattform.service.UserService;
import de.hhu.abschlussprojektverleihplattform.utils.RandomTestData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;

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

    @Autowired
    UserService userService;

    @Autowired
    AuthenticatedUserService authenticatedUserService;


    @Test
    @WithUserDetails("sarah")
    public void testcontrolleristhere() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Profile")))
                .andExpect(content().string(containsString("Email")))
                .andExpect(content().string(containsString("Benutzername")))
                .andExpect(content().string(containsString("Kontostand")));
    }

    //@WithUserDetails("sarah")
    @Test
    public void test_sarah_can_deposit_money_and_see_her_balance() throws Exception{

        //(new UserDetails("test","test"))
        UserEntity user= RandomTestData.newRandomTestUser();
        userService.addUser(user);

        Principal p=new Principal() {
            @Override
            public String getName() {
                return "test";
            }
        };

        String username=user.getUsername();


        mockMvc.perform(post("/profile/deposit?amount=100")
                .with(user(authenticatedUserService.loadUserByUsername(username)))
        )
            .andExpect(status().is3xxRedirection());
    }
}
