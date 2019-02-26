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
    public void testSavingSetsId(){
        UserEntity user2 = RandomTestData.newRandomTestUser();
        AddressEntity address2 = RandomTestData.newRandomTestAddress();
        userRepository.saveUser(user2);

        ProductEntity product = RandomTestData.newRandomTestProduct(user2, address2);

        productRepository.saveProduct(product);

        //should fail if id is not set
        productRepository.getProductById(product.getId());
    }

    @Test
    public void testSavingWithPriceAndStatus(){
        UserEntity owner = RandomTestData.newRandomTestUser();
        userRepository.saveUser(owner);
        AddressEntity productAddress = RandomTestData.newRandomTestAddress();
        ProductEntity testProduct = RandomTestData.newRandomTestPoductWithPrice(
            owner,
            productAddress,
            1000
        );
        productRepository.saveProduct(testProduct);
        ProductEntity product = productRepository.getProductById(testProduct.getId());
        Assert.assertEquals(testProduct, product);
    }

    @Test
    public void testUpdatingAProductWithPrice(){
        UserEntity owner = RandomTestData.newRandomTestUser();
        userRepository.saveUser(owner);
        AddressEntity addressEntity = RandomTestData.newRandomTestAddress();
        ProductEntity testProduct = RandomTestData.newRandomTestPoductWithPrice(
            owner,
            addressEntity,
            1000
        );
        productRepository.saveProduct(testProduct);
        testProduct.setPrice(500);
        productRepository.editProduct(testProduct);
        ProductEntity loadedProduct = productRepository.getProductById(testProduct.getId());
        Assert.assertEquals(testProduct, loadedProduct);
    }

    @Test
    public void getAllProductsWithPrice(){
        boolean productExsists = false;
        UserEntity owner = RandomTestData.newRandomTestUser();
        userRepository.saveUser(owner);
        AddressEntity addressEntity = RandomTestData.newRandomTestAddress();
        ProductEntity testProduct = RandomTestData.newRandomTestPoductWithPrice(
            owner,
            addressEntity,
            1000
        );
        productRepository.saveProduct(testProduct);
        List<ProductEntity> allProducts = productRepository.getAllProducts();
        ProductEntity loadedProduct = new ProductEntity();

        for (ProductEntity product: allProducts){
            if (product.getId() == testProduct.getId()){
                productExsists = true;
                loadedProduct = product;
            }
        }

        Assert.assertTrue(productExsists);
        Assert.assertEquals(testProduct, loadedProduct);
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

        AddressEntity address = RandomTestData.newRandomTestAddress();

        ProductEntity productEntity = RandomTestData.newRandomTestProduct(user, address);
        productRepository.saveProduct(productEntity);

        List<ProductEntity> allProductsFromUser
                = productRepository.getAllProductsFromUser(user);

        Assert.assertEquals(1, allProductsFromUser.size());
    }
}

