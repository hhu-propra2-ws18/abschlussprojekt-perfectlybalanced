package de.hhu.abschlussprojektverleihplattform.controllers;

import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import de.hhu.abschlussprojektverleihplattform.model.Lendingstatus;
import de.hhu.abschlussprojektverleihplattform.service.LendingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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

    @Test
    public void contexLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    @WithUserDetails("sarah")
    public void testcontrolleristhere() throws Exception {
        mockMvc.perform(get("/lendingrequests"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Leihanfragen")));
    }


    @Test
    @WithUserDetails("sarah")
    public void rejectRequest() throws Exception {
        LendingEntity lending = new LendingEntity();
        lending.setId(2L);
        lending.setStatus(Lendingstatus.requested);

        mockMvc.perform(post("/lendingrequests/reject?id=2")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(ProductLendingRequestsController.class));

        verify(lendingService).getLendingById(2L);
        verify(lendingService).denyLendingRequest(null);
    }

    @Test
    @WithUserDetails("sarah")
    public void acceptRequest() throws Exception {
        LendingEntity lending = new LendingEntity();
        lending.setId(2L);
        lending.setStatus(Lendingstatus.requested);

        mockMvc.perform(post("/lendingrequests/accept?id=2")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(ProductLendingRequestsController.class));

        verify(lendingService).getLendingById(2L);
        verify(lendingService).acceptLendingRequest(null);
    }

}
