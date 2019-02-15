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
public class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    //@WithMockUser
    public void testcontrolleristhere() throws Exception {
        mockMvc.perform(get("/addproduct").cookie(new Cookie(CookieUserService.cookieName,"1")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Artikel einstellen")))
                .andExpect(content().string(containsString("Titel")))
                .andExpect(content().string(containsString("Beschreibung")))
                .andExpect(content().string(containsString("Kosten")))
                .andExpect(content().string(containsString("Kaution")));
    }
}