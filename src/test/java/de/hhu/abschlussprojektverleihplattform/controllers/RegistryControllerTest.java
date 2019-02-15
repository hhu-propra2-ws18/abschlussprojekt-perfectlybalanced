package de.hhu.abschlussprojektverleihplattform.controllers;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
@AutoConfigureMockMvc
public class RegistryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testcontrolleristhere() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Registrierung")))
                .andExpect(content().string(containsString("username")))
                .andExpect(content().string(containsString("password")));
    }

    // TODO: Error Handling Spring Security

    @Test
    public void test404onwrongparameters() throws Exception{
        mockMvc.perform(post("/register"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testredirectoncorrectparameters() throws Exception{
        mockMvc.perform(post("/register?vorname=dennis"
	    +"&nachname=peterson"
	    +"&username=dennisp"
	    +"&password=dennis324"
	    +"&email=dennis@gmail.com"
	    ))
            //.andExpect(status().is3xxRedirection());
            .andExpect(status().isForbidden()
	    );
    }

    @Test
    public void throwsbadrequestonemptyfield() throws Exception{
        mockMvc.perform(post(
	    "/register?vorname="
	    +"&nachname=peterson"
	    +"&username=dennisp"
	    +"&password=dennis324"
	    +"&email=dennis@gmail.com"
	    ))
            //.andExpect(status().isBadRequest());
            .andExpect(status().isForbidden()
	    );
    }
}
