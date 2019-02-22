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

import java.util.List;

import static org.junit.Assert.assertEquals;
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
    private ProductEntity product1;

    @Before
    public void saveSetupEntities() {
        userRepository.saveUser(user1);

        product1 = RandomTestData.newRandomTestProduct(user1, address1);
        productRepository.saveProduct(product1);

    }

    @Test
    public void test_saving_sets_id(){
        LendingEntity lendingEntity
                = RandomTestData.newRandomLendingStausDone(user1, product1);
        lendingRepository.addLending(lendingEntity);

        Assert.assertEquals(
            lendingEntity.getId(),
            lendingRepository.getLendingById(
                lendingEntity.getId()
            ).getId()
        );
    }

    @Test
    public void saveOneLendingToDatabase() {
        LendingEntity lendingEntity
            = RandomTestData.newRandomLendingStausDone(user1, product1);
        lendingRepository.addLending(lendingEntity);

        LendingEntity loadedLending
            = lendingRepository.getLendingsByProductAndBorrower(product1, user1).get(0);


        System.out.println("pre loading " + lendingEntity.getStatus());
        System.out.println("after loading " + loadedLending.getStatus());

        lendingEntity.setId(loadedLending.getId());
        assertEquals(loadedLending, lendingEntity);
    }

    @Test
    public void test_get_all_lendings_from_product() {
        LendingEntity lendingEntity
            = RandomTestData.newRandomLendingStausDone(user1, product1);
        lendingRepository.addLending(lendingEntity);

        List<LendingEntity> lendings
            = lendingRepository.getAllLendingsFromProduct(product1);
        assertEquals(1, lendings.size());
    }

    @Test
    public void test_getLendingByProductAndUser() {
        LendingEntity lendingEntity
            = RandomTestData.newRandomLendingStausDone(user1, product1);

        lendingRepository.addLending(lendingEntity);
        LendingEntity loadedLending
            = lendingRepository.getLendingsByProductAndBorrower(product1, user1).get(0);

        assertEquals(loadedLending, lendingEntity);

    }

    @Test
    public void test_getReturnedLendingFromUser() {
        LendingEntity returnedLendingEntity
            = RandomTestData.newRandomLendingStatusConflict(user1, product1);
        returnedLendingEntity.setStatus(Lendingstatus.returned);
        lendingRepository.addLending(returnedLendingEntity);

        List<LendingEntity> returnedLendings
            = lendingRepository.getAllLendingsFromUser(user1);


        Assert.assertEquals(1, returnedLendings.size());
    }

    @Test
    public void test_getAllConflicts() {
        LendingEntity lendingEntity
            = RandomTestData.newRandomLendingStatusConflict(user1, product1);

        List<LendingEntity> conflictLendingsBefore = lendingRepository.getAllConflicts();
        int sizeBefore = conflictLendingsBefore.size();
        lendingRepository.addLending(lendingEntity);
        List<LendingEntity> conflictLendings = lendingRepository.getAllConflicts();

        assertEquals(sizeBefore+1, conflictLendings.size());
        assertTrue(conflictLendings.contains(lendingEntity));
    }

    @Test
    public void lendingStatusTest() {
        UserEntity user = RandomTestData.newRandomTestUser();
        userRepository.saveUser(user);

        AddressEntity address = RandomTestData.newRandomTestAddress();

        ProductEntity productEntity = RandomTestData.newRandomTestProduct(user, address);
        productRepository.saveProduct(productEntity);


        LendingEntity lendingEntity
            = RandomTestData.newRandomLendingStausDone(user, productEntity);
        lendingRepository.addLending(lendingEntity);
        LendingEntity testLending
            = lendingRepository.getLendingsByProductAndBorrower(productEntity, user).get(0);


    }

    @Test
    public void getAllReturnedLendingsFromOwner() {
        UserEntity owner = RandomTestData.newRandomTestUser();
        UserEntity borrower = RandomTestData.newRandomTestUser();
        userRepository.saveUser(owner);
        userRepository.saveUser(borrower);

        AddressEntity address = RandomTestData.newRandomTestAddress();

        ProductEntity productEntity = RandomTestData.newRandomTestProduct(owner, address);
        productRepository.saveProduct(productEntity);

        LendingEntity lendingEntity
            = RandomTestData.newRandomLendingStausDone(borrower, productEntity);
        lendingEntity.setStatus(Lendingstatus.returned);
        lendingRepository.addLending(lendingEntity);
        LendingEntity testLending
            = lendingRepository.getLendingsByProductAndBorrower(productEntity, borrower).get(0);

        List<LendingEntity> allReturnedLendingsFromOwner
            = lendingRepository.getReturnedLendingFromUser(owner);

        Assert.assertEquals(1, allReturnedLendingsFromOwner.size());
    }

    @Test
    public void getAllRquestsForUser() {
        UserEntity owner = RandomTestData.newRandomTestUser();
        UserEntity borrower = RandomTestData.newRandomTestUser();
        userRepository.saveUser(owner);
        userRepository.saveUser(borrower);
        AddressEntity address = RandomTestData.newRandomTestAddress();

        ProductEntity productEntity = RandomTestData.newRandomTestProduct(owner, address);
        productRepository.saveProduct(productEntity);

        LendingEntity lendingEntity
            = RandomTestData.newRandomLendingStausDone(borrower, productEntity);
        lendingEntity.setStatus(Lendingstatus.requested);
        lendingRepository.addLending(lendingEntity);
        LendingEntity testLending
            = lendingRepository.getLendingsByProductAndBorrower(productEntity, borrower).get(0);

        List<LendingEntity> allReturnedLendingsFromOwner
            = lendingRepository.getAllRequestsForUser(owner);

        Assert.assertEquals(2, allReturnedLendingsFromOwner.size());
    }

    @Test
    public void updateLendingStatus() {
        UserEntity owner2 = RandomTestData.newRandomTestUser();
        UserEntity borrower2 = RandomTestData.newRandomTestUser();
        userRepository.saveUser(owner2);
        userRepository.saveUser(borrower2);

        AddressEntity address = RandomTestData.newRandomTestAddress();

        ProductEntity productEntity = RandomTestData.newRandomTestProduct(owner2, address);
        productRepository.saveProduct(productEntity);

        LendingEntity lendingEntity
            = RandomTestData.newRandomLendingStausDone(borrower2, productEntity);
        lendingRepository.addLending(lendingEntity);
        LendingEntity testLending
            = lendingRepository.getLendingsByProductAndBorrower(productEntity, borrower2).get(0);
        testLending.setStatus(Lendingstatus.denied);
        lendingRepository.update(testLending);
        testLending
            = lendingRepository.getLendingsByProductAndBorrower(productEntity, borrower2).get(0);
        assertEquals(Lendingstatus.denied, testLending.getStatus());
    }

    @Test
    public void getLendingById() {
        UserEntity owner3 = RandomTestData.newRandomTestUser();
        UserEntity borrower3 = RandomTestData.newRandomTestUser();
        userRepository.saveUser(owner3);
        userRepository.saveUser(borrower3);

        AddressEntity address = RandomTestData.newRandomTestAddress();

        ProductEntity productEntity = RandomTestData.newRandomTestProduct(owner3, address);
        productRepository.saveProduct(productEntity);

        LendingEntity lendingEntity
            = RandomTestData.newRandomLendingStausDone(borrower3, productEntity);
        lendingRepository.addLending(lendingEntity);
        LendingEntity testLending
            = lendingRepository.getLendingsByProductAndBorrower(productEntity, borrower3).get(0);
        LendingEntity testLendingLoadedById = lendingRepository.getLendingById(testLending.getId());

        Assert.assertEquals(testLending, testLendingLoadedById);
    }
}
