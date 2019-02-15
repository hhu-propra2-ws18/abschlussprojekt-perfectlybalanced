package de.hhu.abschlussprojektverleihplattform.repository;

import de.hhu.abschlussprojektverleihplattform.model.*;
import de.hhu.abschlussprojektverleihplattform.utils.RandomTestData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class LendingRepositoryTest {

    @Autowired
    private LendingRepository lendingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;


    @Test
    public void saveOneLendingToDatabase(){
        UserEntity user1 = RandomTestData.newRandomTestUser();
        AddressEntity address1 = RandomTestData.newRandomTestAddress();
        userRepository.saveUser(user1);
        UserEntity loadedUser1 = userRepository.getUserByFirstname(user1.getFirstname());
        ProductEntity product1 = RandomTestData.newRandomTestProduct(loadedUser1, address1);
        productRepository.saveProduct(product1);
        ProductEntity loadedProduct1 = productRepository.getProductByTitlel(product1.getTitle());
        LendingEntity lendingEntity = RandomTestData.newRandomLendingStausDone(loadedUser1, loadedProduct1);
        lendingRepository.saveLending(lendingEntity);
        List<LendingEntity> allLendings = lendingRepository.getAllLendings();
        System.out.println(allLendings.toString());
    }

}