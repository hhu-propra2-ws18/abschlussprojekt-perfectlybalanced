package de.hhu.abschlussprojektverleihplattform.repository;

import de.hhu.abschlussprojektverleihplattform.model.*;
import de.hhu.abschlussprojektverleihplattform.utils.RandomTestData;
import org.junit.Assert;
import org.junit.Before;
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

    //so we do not have to do it in every test case

    //this is the user that borrows and also owns the product
    private UserEntity user1 = RandomTestData.newRandomTestUser();
    private AddressEntity address1 = RandomTestData.newRandomTestAddress();

    //the loaded versions
    UserEntity loadedUser1;
    ProductEntity loadedProduct1;

    @Before
    public void saveSetupEntities(){
        userRepository.saveUser(user1);
        loadedUser1 = userRepository.getUserByFirstname(user1.getFirstname());
        ProductEntity product1 = RandomTestData.newRandomTestProduct(loadedUser1, address1);
        productRepository.saveProduct(product1);
        loadedProduct1 = productRepository.getProductByTitlel(product1.getTitle());
    }

    @Test
    public void saveOneLendingToDatabase(){
        LendingEntity lendingEntity = RandomTestData.newRandomLendingStausDone(loadedUser1, loadedProduct1);
        lendingRepository.saveLending(lendingEntity);

        LendingEntity loadedLending = lendingRepository.getLendingByProductAndUser(loadedProduct1,loadedUser1);
        //List<LendingEntity> allLendings = lendingRepository.getAllLendings();

        System.out.println("pre loading "+lendingEntity.getStatus());
        System.out.println("after loading "+loadedLending.getStatus());

        lendingEntity.setId(loadedLending.getId());
        assertEquals(loadedLending,lendingEntity);
    }

    @Test
    public void test_get_all_lendings_from_product(){
        LendingEntity lendingEntity = RandomTestData.newRandomLendingStausDone(loadedUser1, loadedProduct1);
        lendingRepository.saveLending(lendingEntity);

        List<LendingEntity> lendings = lendingRepository.getAllLendingsFromProduct(loadedProduct1);
        assertEquals(1, lendings.size());
    }

    @Test
    public void test_getLendingByProductAndUser(){
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

    @Test
    public void test_getReturnedLendingFromUser(){
        LendingEntity returnedLendingEntity = RandomTestData.newRandomLendingStatusConflict(loadedUser1,loadedProduct1);
        returnedLendingEntity.setStatus(Lendingstatus.returned);
        lendingRepository.saveLending(returnedLendingEntity);

        List<LendingEntity> returnedlendings = lendingRepository.getAllLendingsFromUser(loadedUser1);


        
        assertEquals(1,returnedlendings.size());
    }

    @Test
    public void test_getAllConflicts(){
        LendingEntity lendingEntity = RandomTestData.newRandomLendingStatusConflict(loadedUser1, loadedProduct1);

        lendingRepository.saveLending(lendingEntity);
        LendingEntity loadedLending = lendingRepository.getLendingByProductAndUser(loadedProduct1,loadedUser1);

        List<LendingEntity> conflictLendings = lendingRepository.getAllConflicts();

        assertEquals(1,conflictLendings.size());
        assertEquals(loadedProduct1, conflictLendings.get(0).getProduct());
    }
}