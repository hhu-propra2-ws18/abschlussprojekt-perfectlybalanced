package de.hhu.abschlussprojektverleihplattform;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
@AutoConfigureMockMvc
public class ProductLendingRequestsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testcontrolleristhere() throws Exception {
        mockMvc.perform(get("/lendingrequests"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Leih Anfragen")));
    }
}