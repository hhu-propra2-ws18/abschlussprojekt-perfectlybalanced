package de.hhu.abschlussprojektverleihplattform.controllers.product;

import de.hhu.abschlussprojektverleihplattform.model.AddressEntity;
import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.Productstatus;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.security.AuthenticatedUserService;
import de.hhu.abschlussprojektverleihplattform.service.LendingService;
import de.hhu.abschlussprojektverleihplattform.service.ProductService;
import de.hhu.abschlussprojektverleihplattform.service.SellService;
import de.hhu.abschlussprojektverleihplattform.service.UserService;
import de.hhu.abschlussprojektverleihplattform.utils.RandomTestData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    @MockBean
    SellService sellService;

    @Autowired
    LendingService lendingService;

    @Autowired
    AuthenticatedUserService authenticatedUserService;

    private Random randomID = new Random();

    @Test
    public void testDetailsControllerIsThere() throws Exception {
        UserEntity randomUser = RandomTestData.newRandomTestUser();
        randomUser.setUserId(randomID.nextLong());
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(randomUser, address);
        product.setId(randomID.nextLong());

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
        randomUser.setUserId(randomID.nextLong());

        when(userService.findByUsername(randomUser.getUsername())).thenReturn(randomUser);

        mockMvc.perform(get("/addproduct")
                .with(user(authenticatedUserService.loadUserByUsername(
                        randomUser.getUsername()
                )))
        )
            .andExpect(status().isOk())
            .andExpect(content().string(containsString(
                    "Wählen Sie aus, ob sie Ihren Artikel verleihen oder verkaufen möchten"
            )));
    }

    @Test
    public void testAddSellingControllerIsThere() throws Exception {
        UserEntity randomUser = RandomTestData.newRandomTestUser();
        randomUser.setUserId(randomID.nextLong());

        when(userService.findByUsername(randomUser.getUsername())).thenReturn(randomUser);

        mockMvc.perform(get("/addproductselling")
                .with(user(authenticatedUserService.loadUserByUsername(
                        randomUser.getUsername()
                )))
                .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Artikel einstellen")))
                .andExpect(content().string(containsString("Titel")))
                .andExpect(content().string(containsString("Beschreibung")))
                .andExpect(content().string(containsString("Preis")));
    }

    @Test
    public void testAddLendingControllerIsThere() throws Exception {
        UserEntity randomUser = RandomTestData.newRandomTestUser();
        randomUser.setUserId(randomID.nextLong());

        when(userService.findByUsername(randomUser.getUsername())).thenReturn(randomUser);

        mockMvc.perform(get("/addproductlending")
                .with(user(authenticatedUserService.loadUserByUsername(
                        randomUser.getUsername()
                )))
                .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Artikel einstellen")))
                .andExpect(content().string(containsString("Titel")))
                .andExpect(content().string(containsString("Beschreibung")))
                .andExpect(content().string(containsString("Kosten")))
                .andExpect(content().string(containsString("Kaution")));
    }

    @Test
    public void testEditControllerIsThereSellingProduct() throws Exception {
        UserEntity randomUser = RandomTestData.newRandomTestUser();
        randomUser.setUserId(randomID.nextLong());
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(randomUser, address);
        product.setStatus(Productstatus.forBuying);
        product.setId(randomID.nextLong());


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
            .andExpect(content().string(containsString("Preis")));
    }

    @Test
    public void testEditControllerIsThereLendingProduct() throws Exception {
        UserEntity randomUser = RandomTestData.newRandomTestUser();
        randomUser.setUserId(randomID.nextLong());
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(randomUser, address);
        product.setStatus(Productstatus.forLending);
        product.setId(randomID.nextLong());


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
        randomUser.setUserId(randomID.nextLong());
        AddressEntity address1 = RandomTestData.newRandomTestAddress();
        ProductEntity product1 = RandomTestData.newRandomTestProduct(randomUser, address1);
        product1.setId(randomID.nextLong());
        AddressEntity address2 = RandomTestData.newRandomTestAddress();
        ProductEntity product2 = RandomTestData.newRandomTestProduct(randomUser, address2);
        product2.setId(randomID.nextLong());
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
    public void testPostValidProductAddSelling() throws Exception {
        UserEntity randomUser = RandomTestData.newRandomTestUser();
        randomUser.setUserId(randomID.nextLong());

        when(userService.findByUsername(randomUser.getUsername())).thenReturn(randomUser);

        mockMvc.perform(post("/addproductselling")
            .param("description","Beschreibung zum TestProdukt")
            .param("title", "TestProdukt")
            .param("price", "100")
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
    public void testPostValidProductAddLending() throws Exception {
        UserEntity randomUser = RandomTestData.newRandomTestUser();
        randomUser.setUserId(randomID.nextLong());

        when(userService.findByUsername(randomUser.getUsername())).thenReturn(randomUser);

        mockMvc.perform(post("/addproductlending")
                .param("description","Beschreibung zum TestProdukt")
                .param("title", "TestProdukt")
                .param("cost", "100")
                .param("surety", "100")
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
    public void testPostProductAddSellingWrongTitle() throws Exception {
        UserEntity randomUser = RandomTestData.newRandomTestUser();
        randomUser.setUserId(randomID.nextLong());

        when(userService.findByUsername(randomUser.getUsername())).thenReturn(randomUser);

        mockMvc.perform(post("/addproductselling")
            .param("description","Beschreibung zum TestProdukt")
            .param("title", "T")
            .param("price", "100")
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
    public void testPostProductAddLendingWrongTitle() throws Exception {
        UserEntity randomUser = RandomTestData.newRandomTestUser();
        randomUser.setUserId(randomID.nextLong());

        when(userService.findByUsername(randomUser.getUsername())).thenReturn(randomUser);

        mockMvc.perform(post("/addproductlending")
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
    public void testPostProductAddSellingWrongPrice() throws Exception {
        UserEntity randomUser = RandomTestData.newRandomTestUser();
        randomUser.setUserId(randomID.nextLong());

        when(userService.findByUsername(randomUser.getUsername())).thenReturn(randomUser);

        mockMvc.perform(post("/addproductselling")
            .param("description","Beschreibung zum TestProdukt")
            .param("title", "TestProdukt")
            .param("price", "-1")
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
    public void testPostProductAddLendingWrongCost() throws Exception {
        UserEntity randomUser = RandomTestData.newRandomTestUser();
        randomUser.setUserId(randomID.nextLong());

        when(userService.findByUsername(randomUser.getUsername())).thenReturn(randomUser);

        mockMvc.perform(post("/addproductlending")
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
    public void testPostProductAddSellingWrongStreet() throws Exception {
        UserEntity randomUser = RandomTestData.newRandomTestUser();
        randomUser.setUserId(randomID.nextLong());

        when(userService.findByUsername(randomUser.getUsername())).thenReturn(randomUser);

        mockMvc.perform(post("/addproductselling")
            .param("description","Beschreibung zum TestProdukt")
            .param("title", "TestProdukt")
            .param("price", "100")
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
    public void testPostProductAddLendingWrongStreet() throws Exception {
        UserEntity randomUser = RandomTestData.newRandomTestUser();
        randomUser.setUserId(randomID.nextLong());

        when(userService.findByUsername(randomUser.getUsername())).thenReturn(randomUser);

        mockMvc.perform(post("/addproductlending")
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
    public void testPostValidProductEditSelling() throws Exception {
        UserEntity randomUser = RandomTestData.newRandomTestUser();
        randomUser.setUserId(randomID.nextLong());
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(randomUser, address);
        product.setStatus(Productstatus.forBuying);
        product.setId(randomID.nextLong());

        when(userService.findByUsername(randomUser.getUsername())).thenReturn(randomUser);
        when(productService.getById(product.getId())).thenReturn(product);

        mockMvc.perform(post("/editproduct/" + product.getId().toString())
            .param("description","Beschreibung zum TestProdukt")
            .param("title", "TestProdukt")
            .param("price", "100")
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
    public void testPostValidProductEditLending() throws Exception {
        UserEntity randomUser = RandomTestData.newRandomTestUser();
        randomUser.setUserId(randomID.nextLong());
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(randomUser, address);
        product.setStatus(Productstatus.forLending);
        product.setId(randomID.nextLong());

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



    // Controller-Tests mit SellService

    @Test
    public void testGoToBuyRequestIsOK() throws Exception {
        // Arrange
        String url = "/buyrequests/sendRequest?id=";

        UserEntity user = RandomTestData.newRandomTestUser();
        user.setUserId(randomID.nextLong());
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(user, address);
        product.setId(randomID.nextLong());

        when(userService.findByUsername(ArgumentMatchers.anyString())).thenReturn(user);
        when(productService.getById(ArgumentMatchers.anyLong())).thenReturn(product);
        Mockito
            .doNothing()
            .when(sellService)
            .buyProduct(ArgumentMatchers.any(UserEntity.class), ArgumentMatchers.any(ProductEntity.class));

        // Act & Assert
        mockMvc
            .perform(get(url + product.getId())
                .with(user(authenticatedUserService.loadUserByUsername(user.getUsername())))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Produkt kaufen")));
    }

    @Test
    public void testGoToBuyRequestNotFound() throws Exception {
        // Arrange
        String url = "/buyrequests/sendRequest?id=";

        UserEntity user = RandomTestData.newRandomTestUser();
        user.setUserId(randomID.nextLong());
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(user, address);
        product.setId(randomID.nextLong());

        when(userService.findByUsername(ArgumentMatchers.anyString())).thenReturn(user);

        // Act & Assert
        mockMvc
            .perform(get(url + product.getId())
                .with(user(authenticatedUserService.loadUserByUsername(user.getUsername())))
                .with(csrf()))
            .andExpect(status().is5xxServerError());
    }

}