package de.hhu.abschlussprojektverleihplattform.repository;

import de.hhu.abschlussprojektverleihplattform.model.AddressEntity;
import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.utils.RandomTestData;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    public void test_saving_sets_id(){
        UserEntity user2 = RandomTestData.newRandomTestUser();
        AddressEntity address2 = RandomTestData.newRandomTestAddress();
        userRepository.saveUser(user2);

        ProductEntity product = RandomTestData.newRandomTestProduct(user2, address2);

        productRepository.saveProduct(product);

        //should fail if id is not set
        productRepository.getProductById(product.getId());
    }

    @Test
    public void getAllProducsFromDatabase() {
        boolean savedProductsExists = false;
        UserEntity user1 = RandomTestData.newRandomTestUser();
        AddressEntity address1 = RandomTestData.newRandomTestAddress();
        userRepository.saveUser(user1);

        ProductEntity product = RandomTestData.newRandomTestProduct(user1, address1);
        productRepository.saveProduct(product);
        List<ProductEntity> allProducts = productRepository.getAllProducts();

        for (ProductEntity productElement : allProducts) {
            if (productElement.getTitle().equals(product.getTitle())
		&& productElement.getDescription().equals(product.getDescription())
                && productElement.getSurety() == product.getSurety()
                && productElement.getCost() == product.getCost()
                && productElement.getLocation().getStreet()
		    .equals(product.getLocation().getStreet())
	    ) {
                savedProductsExists = true;
	    }
        }

        Assert.assertTrue(savedProductsExists);
    }

    @Test
    public void getAllAvailableProducts(){
        boolean savedProductsExists = false;
        AddressEntity address3 = RandomTestData.newRandomTestAddress();
        UserEntity user3 = RandomTestData.newRandomTestUser();
        userRepository.saveUser(user3);

        ProductEntity product = RandomTestData.newRandomTestProduct(user3, address3);
        productRepository.saveProduct(product);
        List<ProductEntity> availableProducts = productRepository.getAvailableProducts();

        for (ProductEntity productElement : availableProducts) {
            if (productElement.getTitle().equals(product.getTitle())
                    && productElement.getDescription().equals(product.getDescription())
                    && productElement.getSurety() == product.getSurety()
                    && productElement.getCost() == product.getCost()
                    && productElement.getLocation().getStreet()
                    .equals(product.getLocation().getStreet())
            ) {
                savedProductsExists = true;
            }
        }

        Assert.assertTrue(savedProductsExists);

    }

    @Test
    public void editProduct(){
        AddressEntity address4 = RandomTestData.newRandomTestAddress();
        UserEntity user4 = RandomTestData.newRandomTestUser();
        userRepository.saveUser(user4);

        ProductEntity product = RandomTestData.newRandomTestProduct(user4, address4);
        productRepository.saveProduct(product);

        product.setSurety(10);
        product.setCost(30);
        product.setTitle("Schraubendreher");
        productRepository.editProduct(product);
        List<ProductEntity> allProducts = productRepository.getAllProducts();
        Assert.assertTrue(allProducts.contains(product));
    }
  
    @Test
    public void getAllProductsFromOneUser(){
        UserEntity user = RandomTestData.newRandomTestUser();
        userRepository.saveUser(user);
        UserEntity testUser = userRepository.getUserByFirstname(user.getFirstname());

        AddressEntity address = RandomTestData.newRandomTestAddress();

        ProductEntity productEntity = RandomTestData.newRandomTestProduct(testUser, address);
        productRepository.saveProduct(productEntity);

        List<ProductEntity> allProductsFromUser
                = productRepository.getAllProductsFromUser(testUser);

        Assert.assertEquals(1, allProductsFromUser.size());
    }
}

