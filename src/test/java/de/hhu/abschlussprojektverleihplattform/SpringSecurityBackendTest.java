package de.hhu.abschlussprojektverleihplattform;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@WebMvcTest
@AutoConfigureMockMvc
public class SpringSecurityBackendTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void contextLoads() {
    }

    @Test
    //@WithMockUser
    public void testSecurityWorks() throws Exception {
        mvc.perform(
                get("/login"))
                .andExpect(content().string(containsString("Verleihplattform")))
                .andExpect(content().string(containsString("Logout")))
                .andExpect(content().string(Matchers.containsString("Produkte ansehen")));

    }

}
