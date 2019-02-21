package de.hhu.abschlussprojektverleihplattform.controllers;

import de.hhu.abschlussprojektverleihplattform.model.AddressEntity;
import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.repository.ProductRepository;
import de.hhu.abschlussprojektverleihplattform.repository.UserRepository;
import de.hhu.abschlussprojektverleihplattform.utils.RandomTestData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

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

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

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
}