package de.hhu.abschlussprojektverleihplattform.controllers.product;

import de.hhu.abschlussprojektverleihplattform.model.AddressEntity;
import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.security.AuthenticatedUserService;
import de.hhu.abschlussprojektverleihplattform.service.LendingService;
import de.hhu.abschlussprojektverleihplattform.service.ProductService;
import de.hhu.abschlussprojektverleihplattform.service.UserService;
import de.hhu.abschlussprojektverleihplattform.utils.RandomTestData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    ProductService productService;

    @Autowired
    LendingService lendingService;

    @Autowired
    AuthenticatedUserService authenticatedUserService;

    @Test
    public void testDetailsControllerIsThere() throws Exception {
        UserEntity randomUser = RandomTestData.newRandomTestUser();
        randomUser.setUserId(1L);
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(randomUser, address);
        product.setId(1L);

        when(userService.findByUsername(randomUser.getUsername())).thenReturn(randomUser);
        when(productService.getById(product.getId())).thenReturn(product);

        mockMvc.perform(get("/productdetail/" + product.getId().toString())
                .with(user(authenticatedUserService.loadUserByUsername(
                        randomUser.getUsername()
                )))
        )
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Detailansicht")))
            .andExpect(content().string(containsString(product.getTitle())))
            .andExpect(content().string(containsString(product.getDescription())));
    }

    @Test
    public void testAddControllerIsThere() throws Exception {
        UserEntity randomUser = RandomTestData.newRandomTestUser();
        randomUser.setUserId(1L);

        when(userService.findByUsername(randomUser.getUsername())).thenReturn(randomUser);

        mockMvc.perform(get("/addproduct")
                .with(user(authenticatedUserService.loadUserByUsername(
                        randomUser.getUsername()
                )))
        )
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Artikel einstellen")))
            .andExpect(content().string(containsString("Titel")))
            .andExpect(content().string(containsString("Beschreibung")))
            .andExpect(content().string(containsString("Kosten")))
            .andExpect(content().string(containsString("Kaution")));
    }

    @Test
    public void testEditControllerIsThere() throws Exception {
        UserEntity randomUser = RandomTestData.newRandomTestUser();
        randomUser.setUserId(1L);
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(randomUser, address);
        product.setId(1L);


        when(userService.findByUsername(randomUser.getUsername())).thenReturn(randomUser);
        when(productService.getById(product.getId())).thenReturn(product);

        mockMvc.perform(get("/editproduct/" + product.getId().toString())
                .with(user(authenticatedUserService.loadUserByUsername(
                        randomUser.getUsername()
                )))
        )
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Artikel bearbeiten")))
            .andExpect(content().string(containsString("Titel")))
            .andExpect(content().string(containsString("Beschreibung")))
            .andExpect(content().string(containsString("Kosten")))
            .andExpect(content().string(containsString("Kaution")));
    }

    @Test
    public void testMyProductsControllerIsThere() throws Exception {
        UserEntity randomUser = RandomTestData.newRandomTestUser();
        randomUser.setUserId(1L);
        AddressEntity address1 = RandomTestData.newRandomTestAddress();
        ProductEntity product1 = RandomTestData.newRandomTestProduct(randomUser, address1);
        product1.setId(1L);
        AddressEntity address2 = RandomTestData.newRandomTestAddress();
        ProductEntity product2 = RandomTestData.newRandomTestProduct(randomUser, address2);
        product2.setId(2L);
        List<ProductEntity> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        when(userService.findByUsername(randomUser.getUsername())).thenReturn(randomUser);
        when(productService.getAllProductsFromUser(randomUser)).thenReturn(products);
        List<ProductEntity> myproducts = productService.getAllProductsFromUser(randomUser);

        mockMvc.perform(get("/myproducts")
                .with(user(authenticatedUserService.loadUserByUsername(
                        randomUser.getUsername()
                )))
        )
            .andExpect(status().isOk())
            .andExpect(content().string(containsString(myproducts.get(0).getTitle())))
            .andExpect(content().string(containsString(myproducts.get(0).getDescription())))
            .andExpect(content().string(containsString(myproducts.get(1).getTitle())))
            .andExpect(content().string(containsString(myproducts.get(1).getDescription())));
    }

    @Test
    public void testPostValidProductAdd() throws Exception {
        UserEntity randomUser = RandomTestData.newRandomTestUser();
        randomUser.setUserId(1L);

        when(userService.findByUsername(randomUser.getUsername())).thenReturn(randomUser);

        mockMvc.perform(post("/addproduct")
            .param("description","Beschreibung zum TestProdukt")
            .param("title", "TestProdukt")
            .param("surety", "100")
            .param("cost", "100")
            .param("street", "Teststraße")
            .param("housenumber", "1")
            .param("postcode", "11111")
            .param("city", "Teststadt")
            .with(csrf())
            .with(user(authenticatedUserService.loadUserByUsername(
                    randomUser.getUsername()
            )))
        )
            .andExpect(redirectedUrl("/"))
            .andExpect(status().isFound());
    }

    @Test
    public void testPostProductAddWrongTitle() throws Exception {
        UserEntity randomUser = RandomTestData.newRandomTestUser();
        randomUser.setUserId(1L);

        when(userService.findByUsername(randomUser.getUsername())).thenReturn(randomUser);

        mockMvc.perform(post("/addproduct")
            .param("description","Beschreibung zum TestProdukt")
            .param("title", "T")
            .param("surety", "100")
            .param("cost", "100")
            .param("street", "Teststraße")
            .param("housenumber", "1")
            .param("postcode", "11111")
            .param("city", "Teststadt")
            .with(csrf())
            .with(user(authenticatedUserService.loadUserByUsername(
                    randomUser.getUsername()
            )))
        )
            .andExpect(content().string(
            containsString("Titel muss zwischen 5 und 50 Zeichen lang sein.")));
    }

    @Test
    public void testPostProductAddWrongCost() throws Exception {
        UserEntity randomUser = RandomTestData.newRandomTestUser();
        randomUser.setUserId(1L);

        when(userService.findByUsername(randomUser.getUsername())).thenReturn(randomUser);

        mockMvc.perform(post("/addproduct")
            .param("description","Beschreibung zum TestProdukt")
            .param("title", "TestProdukt")
            .param("surety", "100")
            .param("cost", "-1")
            .param("street", "Teststraße")
            .param("housenumber", "1")
            .param("postcode", "11111")
            .param("city", "Teststadt")
            .with(csrf())
            .with(user(authenticatedUserService.loadUserByUsername(
                    randomUser.getUsername()
            )))
        )
            .andExpect(content().string(
            containsString("Kosten muss mindestens einen Wert ab 0 Euro haben.")));
    }


    @Test
    public void testPostProductAddWrongStreet() throws Exception {
        UserEntity randomUser = RandomTestData.newRandomTestUser();
        randomUser.setUserId(1L);

        when(userService.findByUsername(randomUser.getUsername())).thenReturn(randomUser);

        mockMvc.perform(post("/addproduct")
            .param("description","Beschreibung zum TestProdukt")
            .param("title", "TestProdukt")
            .param("surety", "100")
            .param("cost", "100")
            .param("street", "Test")
            .param("housenumber", "1")
            .param("postcode", "11111")
            .param("city", "Teststadt")
            .with(csrf())
            .with(user(authenticatedUserService.loadUserByUsername(
                    randomUser.getUsername()
            )))
        )
            .andExpect(content().string(
            containsString("Adresse muss mindestens 5 Zeichen lang sein.")));
    }


    @Test
    public void testPostValidProductEdit() throws Exception {
        UserEntity randomUser = RandomTestData.newRandomTestUser();
        randomUser.setUserId(1L);
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(randomUser, address);
        product.setId(1L);

        when(userService.findByUsername(randomUser.getUsername())).thenReturn(randomUser);
        when(productService.getById(product.getId())).thenReturn(product);

        mockMvc.perform(post("/editproduct/" + product.getId().toString())
            .param("description","Beschreibung zum TestProdukt")
            .param("title", "TestProdukt")
            .param("surety", "100")
            .param("cost", "100")
            .param("street", "Teststraße")
            .param("housenumber", "1")
            .param("postcode", "11111")
            .param("city", "Teststadt")
            .with(csrf())
            .with(user(authenticatedUserService.loadUserByUsername(
                    randomUser.getUsername()
            )))
        )
            .andExpect(redirectedUrl("/myproducts"))
            .andExpect(status().isFound());
    }

    @Test
    public void testPostProductEditWrongTitle() throws Exception {
        UserEntity randomUser = RandomTestData.newRandomTestUser();
        randomUser.setUserId(1L);
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(randomUser, address);
        product.setId(1L);

        when(userService.findByUsername(randomUser.getUsername())).thenReturn(randomUser);
        when(productService.getById(product.getId())).thenReturn(product);

        mockMvc.perform(post("/editproduct/" + product.getId().toString())
            .param("description","Beschreibung zum TestProdukt")
            .param("title", "T")
            .param("surety", "100")
            .param("cost", "100")
            .param("street", "Teststraße")
            .param("housenumber", "1")
            .param("postcode", "11111")
            .param("city", "Teststadt")
            .with(csrf())
            .with(user(authenticatedUserService.loadUserByUsername(
                    randomUser.getUsername()
            )))
        )
            .andExpect(content().string(
            containsString("Titel muss zwischen 5 und 50 Zeichen lang sein.")));
    }

    @Test
    public void testPostProductEditWrongCost() throws Exception {
        UserEntity randomUser = RandomTestData.newRandomTestUser();
        randomUser.setUserId(1L);
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(randomUser, address);
        product.setId(1L);

        when(userService.findByUsername(randomUser.getUsername())).thenReturn(randomUser);
        when(productService.getById(product.getId())).thenReturn(product);

        mockMvc.perform(post("/editproduct/" + product.getId().toString())
            .param("description","Beschreibung zum TestProdukt")
            .param("title", "TestProdukt")
            .param("surety", "100")
            .param("cost", "-1")
            .param("street", "Teststraße")
            .param("housenumber", "1")
            .param("postcode", "11111")
            .param("city", "Teststadt")
            .with(csrf())
            .with(user(authenticatedUserService.loadUserByUsername(
                    randomUser.getUsername()
            )))
        )
            .andExpect(content().string(
            containsString("Kosten muss mindestens einen Wert ab 0 Euro haben.")));
    }


    @Test
    public void testPostProductEditWrongStreet() throws Exception {
        UserEntity randomUser = RandomTestData.newRandomTestUser();
        randomUser.setUserId(1L);
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(randomUser, address);
        product.setId(1L);

        when(userService.findByUsername(randomUser.getUsername())).thenReturn(randomUser);
        when(productService.getById(product.getId())).thenReturn(product);


        mockMvc
            .perform(post("/editproduct/" + product.getId().toString())
                .param("description","Beschreibung zum TestProdukt")
                .param("title", "TestProdukt")
                .param("surety", "100")
                .param("cost", "100")
                .param("street", "Test")
                .param("housenumber", "1")
                .param("postcode", "11111")
                .param("city", "Teststadt")
                .with(csrf())
                .with(user(authenticatedUserService.loadUserByUsername(randomUser.getUsername()))))
            .andExpect(content()
                .string(containsString("Adresse muss mindestens 5 Zeichen lang sein.")));
    }
}