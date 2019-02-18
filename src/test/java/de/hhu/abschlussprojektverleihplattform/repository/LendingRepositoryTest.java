package de.hhu.abschlussprojektverleihplattform.repository;

import de.hhu.abschlussprojektverleihplattform.model.*;
import de.hhu.abschlussprojektverleihplattform.utils.RandomTestData;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;


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

        LendingEntity loadedLending = lendingRepository.getLendingByProductAndUser(loadedProduct1,loadedUser1);
        //List<LendingEntity> allLendings = lendingRepository.getAllLendings();

        lendingEntity.setId(loadedLending.getId());
        assertEquals(loadedLending,lendingEntity);
    }

    @Test
    public void test_get_all_lendings_from_product(){

        UserEntity user1 = RandomTestData.newRandomTestUser();
        AddressEntity address1 = RandomTestData.newRandomTestAddress();
        userRepository.saveUser(user1);
        UserEntity loadedUser1 = userRepository.getUserByFirstname(user1.getFirstname());
        ProductEntity product1 = RandomTestData.newRandomTestProduct(loadedUser1, address1);
        productRepository.saveProduct(product1);

        ProductEntity loadedProduct1 = productRepository.getProductByTitlel(product1.getTitle());
        LendingEntity lendingEntity = RandomTestData.newRandomLendingStausDone(loadedUser1, loadedProduct1);

        lendingRepository.saveLending(lendingEntity);


        List<LendingEntity> lendings = lendingRepository.getAllLendingsFromProduct(loadedProduct1);
        assertTrue(lendings.size()==1);
    }

    @Test
    public void test_getLendingByProductAndUser(){
        UserEntity user1 = RandomTestData.newRandomTestUser();
        AddressEntity address1 = RandomTestData.newRandomTestAddress();
        userRepository.saveUser(user1);
        UserEntity loadedUser1 = userRepository.getUserByFirstname(user1.getFirstname());
        ProductEntity product1 = RandomTestData.newRandomTestProduct(loadedUser1, address1);
        productRepository.saveProduct(product1);
        ProductEntity loadedProduct1 = productRepository.getProductByTitlel(product1.getTitle());
        LendingEntity lendingEntity = RandomTestData.newRandomLendingStausDone(loadedUser1, loadedProduct1);

        lendingRepository.saveLending(lendingEntity);
        LendingEntity loadedLending = lendingRepository.getLendingByProductAndUser(loadedProduct1,loadedUser1);

        lendingEntity.setId(loadedLending.getId());
        assertEquals(loadedLending,lendingEntity);

    }

    /*
    java.lang.AssertionError: expected:
    <LendingEntity(id=5, status=denied, start=2019-02-18 11:39:44.632, end=2019-02-19 11:39:44.632, borrower=UserEntity(userId=7, firstname=zqdbyQtJLh, lastname=VyjkOXWuxu, username=qAzUCynjeI, password=kBaGIbpOdV, email=ZWBniEOCvT, role=null), product=ProductEntity(id=5, description=vGFaqXofJRwjFFQNWMpxqtiktsOUxSwDioFotgDpuwDiBRgFyBgExyBmMEciblNqItWAcqiwRZsEJLeIOHqgqPBqkdtJIfxUnERIZizYBrgDugoLuDjpdhCZTxKZXHeZCnJcqdhUhfquLuPIOYYiMDkcjZkJFTPfmqsFmNnGJopDCmiMuowEUXMLkdYQXdgoPuJoZMMWfCpVqBwiGyvuzVvVLEUbtkpdNNYknYiEpwkQzRUuGpoetMrICSTMWxL, title=niZaxbnRQbZiocecVuifHPzqMxYYkclvNwAybEotGFwODlySya, surety=1978099059, cost=191278473, location=AddressEntity(street=wPbtjSDbsx, housenumber=592833423, postcode=19812, city=ROAApsYLuR), owner=UserEntity(userId=7, firstname=zqdbyQtJLh, lastname=VyjkOXWuxu, username=qAzUCynjeI, password=kBaGIbpOdV, email=ZWBniEOCvT, role=null)), costReservationID=2, suretyReservationID=6)>
    but was:
    <LendingEntity(id=5, status=done, start=2019-02-18 11:39:44.632, end=2019-02-19 11:39:44.632, borrower=UserEntity(userId=7, firstname=zqdbyQtJLh, lastname=VyjkOXWuxu, username=qAzUCynjeI, password=kBaGIbpOdV, email=ZWBniEOCvT, role=null), product=ProductEntity(id=5, description=vGFaqXofJRwjFFQNWMpxqtiktsOUxSwDioFotgDpuwDiBRgFyBgExyBmMEciblNqItWAcqiwRZsEJLeIOHqgqPBqkdtJIfxUnERIZizYBrgDugoLuDjpdhCZTxKZXHeZCnJcqdhUhfquLuPIOYYiMDkcjZkJFTPfmqsFmNnGJopDCmiMuowEUXMLkdYQXdgoPuJoZMMWfCpVqBwiGyvuzVvVLEUbtkpdNNYknYiEpwkQzRUuGpoetMrICSTMWxL, title=niZaxbnRQbZiocecVuifHPzqMxYYkclvNwAybEotGFwODlySya, surety=1978099059, cost=191278473, location=AddressEntity(street=wPbtjSDbsx, housenumber=592833423, postcode=19812, city=ROAApsYLuR), owner=UserEntity(userId=7, firstname=zqdbyQtJLh, lastname=VyjkOXWuxu, username=qAzUCynjeI, password=kBaGIbpOdV, email=ZWBniEOCvT, role=null)), costReservationID=0, suretyReservationID=0)>
     */

}