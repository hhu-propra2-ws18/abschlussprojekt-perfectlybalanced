package de.hhu.abschlussprojektverleihplattform.controllers.product;

import de.hhu.abschlussprojektverleihplattform.model.AddressEntity;
import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.service.LendingService;
import de.hhu.abschlussprojektverleihplattform.service.ProductService;
import de.hhu.abschlussprojektverleihplattform.service.UserService;
import de.hhu.abschlussprojektverleihplattform.utils.RandomTestData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Autowired
    LendingService lendingService;

    @Test
    @WithUserDetails("sarah")
    public void testDetailsControllerIsThere() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) auth.getPrincipal();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(user, address);

        productService.addProduct(product);

        mockMvc.perform(get("/productdetail/" + product.getId().toString()))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Detailansicht")))
            .andExpect(content().string(containsString(product.getTitle())))
            .andExpect(content().string(containsString(product.getDescription())));
    }



    @Test
    @WithUserDetails("sarah")
    public void testAddControllerIsThere() throws Exception {
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
    public void testEditControllerIsThere() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) auth.getPrincipal();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(user, address);

        productService.addProduct(product);

        mockMvc.perform(get("/editproduct/" + product.getId().toString()))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Artikel bearbeiten")))
            .andExpect(content().string(containsString("Titel")))
            .andExpect(content().string(containsString("Beschreibung")))
            .andExpect(content().string(containsString("Kosten")))
            .andExpect(content().string(containsString("Kaution")));
    }

    @Test
    @WithUserDetails("admin")
    public void testMyProductsControllerIsThere() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) auth.getPrincipal();
        AddressEntity address1 = RandomTestData.newRandomTestAddress();
        ProductEntity product1 = RandomTestData.newRandomTestProduct(user, address1);
        productService.addProduct(product1);
        AddressEntity address2 = RandomTestData.newRandomTestAddress();
        ProductEntity product2 = RandomTestData.newRandomTestProduct(user, address2);
        productService.addProduct(product2);

        List<ProductEntity> myproducts = productService.getAllProductsFromUser(user);

        mockMvc.perform(get("/myproducts"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString(myproducts.get(0).getTitle())))
            .andExpect(content().string(containsString(myproducts.get(0).getDescription())))
            .andExpect(content().string(containsString(myproducts.get(1).getTitle())))
            .andExpect(content().string(containsString(myproducts.get(1).getDescription())));
    }

    @Test
    @WithUserDetails("sarah")
    public void testPostValidProductAdd() throws Exception {
        mockMvc.perform(post("/addproduct")
            .param("description","Beschreibung zum TestProdukt")
            .param("title", "TestProdukt")
            .param("surety", "100")
            .param("cost", "100")
            .param("street", "Teststraße")
            .param("housenumber", "1")
            .param("postcode", "11111")
            .param("city", "Teststadt")
            .with(csrf()))
            .andExpect(redirectedUrl("/"))
            .andExpect(status().isFound());
    }

    @Test
    @WithUserDetails("sarah")
    public void testPostProductAddWrongTitle() throws Exception {
        mockMvc.perform(post("/addproduct")
            .param("description","Beschreibung zum TestProdukt")
            .param("title", "T")
            .param("surety", "100")
            .param("cost", "100")
            .param("street", "Teststraße")
            .param("housenumber", "1")
            .param("postcode", "11111")
            .param("city", "Teststadt")
            .with(csrf()))
            .andExpect(content().string(
            containsString("Titel muss zwischen 5 und 50 Zeichen lang sein.")));
    }

    @Test
    @WithUserDetails("sarah")
    public void testPostProductAddWrongCost() throws Exception {
        mockMvc.perform(post("/addproduct")
            .param("description","Beschreibung zum TestProdukt")
            .param("title", "TestProdukt")
            .param("surety", "100")
            .param("cost", "-1")
            .param("street", "Teststraße")
            .param("housenumber", "1")
            .param("postcode", "11111")
            .param("city", "Teststadt")
            .with(csrf()))
            .andExpect(content().string(
            containsString("Kosten muss mindestens einen Wert ab 0 Euro haben.")));
    }


    @Test
    @WithUserDetails("sarah")
    public void testPostProductAddWrongStreet() throws Exception {
        mockMvc.perform(post("/addproduct")
            .param("description","Beschreibung zum TestProdukt")
            .param("title", "TestProdukt")
            .param("surety", "100")
            .param("cost", "100")
            .param("street", "Test")
            .param("housenumber", "1")
            .param("postcode", "11111")
            .param("city", "Teststadt")
            .with(csrf()))
            .andExpect(content().string(
            containsString("Adresse muss mindestens 5 Zeichen lang sein.")));
    }


    @Test
    @WithUserDetails("sarah")
    public void testPostValidProductEdit() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) auth.getPrincipal();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(user, address);

        productService.addProduct(product);

        mockMvc.perform(post("/editproduct/" + product.getId().toString())
            .param("description","Beschreibung zum TestProdukt")
            .param("title", "TestProdukt")
            .param("surety", "100")
            .param("cost", "100")
            .param("street", "Teststraße")
            .param("housenumber", "1")
            .param("postcode", "11111")
            .param("city", "Teststadt")
            .with(csrf()))
            .andExpect(redirectedUrl("/myproducts"))
            .andExpect(status().isFound());
    }

    @Test
    @WithUserDetails("sarah")
    public void testPostProductEditWrongTitle() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) auth.getPrincipal();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(user, address);

        productService.addProduct(product);

        mockMvc.perform(post("/editproduct/" + product.getId().toString())
            .param("description","Beschreibung zum TestProdukt")
            .param("title", "T")
            .param("surety", "100")
            .param("cost", "100")
            .param("street", "Teststraße")
            .param("housenumber", "1")
            .param("postcode", "11111")
            .param("city", "Teststadt")
            .with(csrf()))
            .andExpect(content().string(
            containsString("Titel muss zwischen 5 und 50 Zeichen lang sein.")));
    }

    @Test
    @WithUserDetails("sarah")
    public void testPostProductEditWrongCost() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) auth.getPrincipal();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(user, address);

        productService.addProduct(product);

        mockMvc.perform(post("/editproduct/" + product.getId().toString())
            .param("description","Beschreibung zum TestProdukt")
            .param("title", "TestProdukt")
            .param("surety", "100")
            .param("cost", "-1")
            .param("street", "Teststraße")
            .param("housenumber", "1")
            .param("postcode", "11111")
            .param("city", "Teststadt")
            .with(csrf()))
            .andExpect(content().string(
            containsString("Kosten muss mindestens einen Wert ab 0 Euro haben.")));
    }


    @Test
    @WithUserDetails("sarah")
    public void testPostProductEditWrongStreet() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) auth.getPrincipal();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(user, address);

        productService.addProduct(product);

        mockMvc.perform(post("/editproduct/" + product.getId().toString())
            .param("description","Beschreibung zum TestProdukt")
            .param("title", "TestProdukt")
            .param("surety", "100")
            .param("cost", "100")
            .param("street", "Test")
            .param("housenumber", "1")
            .param("postcode", "11111")
            .param("city", "Teststadt")
            .with(csrf()))
            .andExpect(content().string(
            containsString("Adresse muss mindestens 5 Zeichen lang sein.")));
    }


}