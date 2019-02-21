package de.hhu.abschlussprojektverleihplattform.controllers;

import de.hhu.abschlussprojektverleihplattform.model.AddressEntity;
import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.Role;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.repository.ProductRepository;
import de.hhu.abschlussprojektverleihplattform.repository.UserRepository;
import de.hhu.abschlussprojektverleihplattform.service.LendingService;
import de.hhu.abschlussprojektverleihplattform.service.ProductService;
import de.hhu.abschlussprojektverleihplattform.utils.RandomTestData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @MockBean
    ProductService productService;

    @MockBean
    LendingService lendingService;

    @Test
    @WithUserDetails("sarah")
    public void testaddcontrolleristhere() throws Exception {
        mockMvc.perform(get("/addproduct"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Artikel einstellen")))
                .andExpect(content().string(containsString("Titel")))
                .andExpect(content().string(containsString("Beschreibung")))
                .andExpect(content().string(containsString("Kosten")))
                .andExpect(content().string(containsString("Kaution")));
    }

    @Test
    @WithUserDetails("sarah")
    public void testeditcontrolleristhere() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity)  auth.getPrincipal();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(user, address);

        productRepository.saveProduct(product);

        mockMvc.perform(get("/editproduct/" + product.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Artikel bearbeiten")))
                .andExpect(content().string(containsString("Titel")))
                .andExpect(content().string(containsString("Beschreibung")))
                .andExpect(content().string(containsString("Kosten")))
                .andExpect(content().string(containsString("Kaution")));
    }

    @Test
    @WithUserDetails("sarah")
    public void lendingRequestTest() throws Exception {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity)  auth.getPrincipal();

        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1L);


        mockMvc.perform(post("/request/?id=2").with(csrf()))
                .andExpect(status().is3xxRedirection());

        verify(productService).getById(any());
        verify(lendingService).requestLending(any(), any(), any(), any());

    }
}