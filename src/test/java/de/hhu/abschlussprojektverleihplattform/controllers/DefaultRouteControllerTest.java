package de.hhu.abschlussprojektverleihplattform.controllers;


import de.hhu.abschlussprojektverleihplattform.service.CookieUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DefaultRouteControllerTest {

    @Autowired
    MockMvc mockMvc;



    @Test
    //@WithMockUser(value = "benutzername")
    public void testcontrolleristhere() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void test_with_user() throws Exception{
        mockMvc.perform(get("/").cookie(new Cookie(CookieUserService.cookieName,"1")))
            .andExpect(content().string(containsString("Verleihplattform")))
            .andExpect(content().string(containsString("Logout")))
            .andExpect(content().string(containsString("Produkte ansehen")));
    }
}
