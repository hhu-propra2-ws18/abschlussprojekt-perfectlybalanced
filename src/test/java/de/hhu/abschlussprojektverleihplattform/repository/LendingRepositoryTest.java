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

         product1
            = RandomTestData.newRandomTestProduct(user1, address1);
        productRepository.saveProduct(product1);

    }

    @Test
    public void test_saving_sets_id(){
        LendingEntity lendingEntity
                = RandomTestData.newRandomLendingStausDone(user1, product1);
        lendingRepository.addLending(lendingEntity);

        Assert.assertEquals(lendingEntity.getId(),lendingRepository.getLendingById(lendingEntity.getId()).getId());
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

        lendingRepository.addLending(lendingEntity);

        List<LendingEntity> conflictLendings = lendingRepository.getAllConflicts();

        assertEquals(1, conflictLendings.size());
        assertEquals(product1, conflictLendings.get(0).getProduct());
    }

    @Test
    public void lendingStatusTest() {
        UserEntity user = RandomTestData.newRandomTestUser();
        userRepository.saveUser(user);
        UserEntity testUser = userRepository.getUserByFirstname(user.getFirstname());

        AddressEntity address = RandomTestData.newRandomTestAddress();

        ProductEntity productEntity = RandomTestData.newRandomTestProduct(testUser, address);
        productRepository.saveProduct(productEntity);
        ProductEntity testProduct = productRepository.getProductByTitlel(productEntity.getTitle());

        LendingEntity lendingEntity
            = RandomTestData.newRandomLendingStausDone(testUser, testProduct);
        lendingRepository.addLending(lendingEntity);
        LendingEntity testLending
            = lendingRepository.getLendingsByProductAndBorrower(testProduct, testUser).get(0);


    }

    @Test
    public void getAllReturnedLendingsFromOwner() {
        UserEntity owner = RandomTestData.newRandomTestUser();
        UserEntity borrower = RandomTestData.newRandomTestUser();
        userRepository.saveUser(owner);
        userRepository.saveUser(borrower);
        UserEntity testOwner = userRepository.getUserByFirstname(owner.getFirstname());
        UserEntity testBorrower = userRepository.getUserByFirstname(borrower.getFirstname());

        AddressEntity address = RandomTestData.newRandomTestAddress();

        ProductEntity productEntity = RandomTestData.newRandomTestProduct(testOwner, address);
        productRepository.saveProduct(productEntity);
        ProductEntity testProduct = productRepository.getProductByTitlel(productEntity.getTitle());

        LendingEntity lendingEntity
            = RandomTestData.newRandomLendingStausDone(testBorrower, testProduct);
        lendingEntity.setStatus(Lendingstatus.returned);
        lendingRepository.addLending(lendingEntity);
        LendingEntity testLending
            = lendingRepository.getLendingsByProductAndBorrower(testProduct, testBorrower).get(0);

        List<LendingEntity> allReturnedLendingsFromOwner
            = lendingRepository.getReturnedLendingFromUser(testOwner);

        Assert.assertEquals(1, allReturnedLendingsFromOwner.size());
    }

    @Test
    public void getAllRquestsForUser() {
        UserEntity owner = RandomTestData.newRandomTestUser();
        UserEntity borrower = RandomTestData.newRandomTestUser();
        userRepository.saveUser(owner);
        userRepository.saveUser(borrower);
        UserEntity testOwner1 = userRepository.getUserByFirstname(owner.getFirstname());
        UserEntity testBorrower1 = userRepository.getUserByFirstname(borrower.getFirstname());

        AddressEntity address = RandomTestData.newRandomTestAddress();

        ProductEntity productEntity = RandomTestData.newRandomTestProduct(testOwner1, address);
        productRepository.saveProduct(productEntity);
        ProductEntity testProduct = productRepository.getProductByTitlel(productEntity.getTitle());

        LendingEntity lendingEntity
            = RandomTestData.newRandomLendingStausDone(testBorrower1, testProduct);
        lendingEntity.setStatus(Lendingstatus.requested);
        lendingRepository.addLending(lendingEntity);
        LendingEntity testLending
            = lendingRepository.getLendingsByProductAndBorrower(testProduct, testBorrower1).get(0);

        List<LendingEntity> allReturnedLendingsFromOwner
            = lendingRepository.getAllRequestsForUser(testOwner1);

        Assert.assertEquals(2, allReturnedLendingsFromOwner.size());
    }

    @Test
    public void updateLendingStatus() {
        UserEntity owner2 = RandomTestData.newRandomTestUser();
        UserEntity borrower2 = RandomTestData.newRandomTestUser();
        userRepository.saveUser(owner2);
        userRepository.saveUser(borrower2);
        UserEntity testOwner2 = userRepository.getUserByFirstname(owner2.getFirstname());
        UserEntity testBorrower2 = userRepository.getUserByFirstname(borrower2.getFirstname());

        AddressEntity address = RandomTestData.newRandomTestAddress();

        ProductEntity productEntity = RandomTestData.newRandomTestProduct(testOwner2, address);
        productRepository.saveProduct(productEntity);
        ProductEntity testProduct = productRepository.getProductByTitlel(productEntity.getTitle());

        LendingEntity lendingEntity
            = RandomTestData.newRandomLendingStausDone(testBorrower2, testProduct);
        lendingRepository.addLending(lendingEntity);
        LendingEntity testLending
            = lendingRepository.getLendingsByProductAndBorrower(testProduct, testBorrower2).get(0);
        testLending.setStatus(Lendingstatus.denied);
        lendingRepository.update(testLending);
        testLending = lendingRepository.getLendingsByProductAndBorrower(testProduct, testBorrower2).get(0);
        assertEquals(Lendingstatus.denied, testLending.getStatus());
    }

    @Test
    public void getLendingById() {
        UserEntity owner3 = RandomTestData.newRandomTestUser();
        UserEntity borrower3 = RandomTestData.newRandomTestUser();
        userRepository.saveUser(owner3);
        userRepository.saveUser(borrower3);
        UserEntity testOwner3 = userRepository.getUserByFirstname(owner3.getFirstname());
        UserEntity testBorrower3 = userRepository.getUserByFirstname(borrower3.getFirstname());

        AddressEntity address = RandomTestData.newRandomTestAddress();

        ProductEntity productEntity = RandomTestData.newRandomTestProduct(testOwner3, address);
        productRepository.saveProduct(productEntity);
        ProductEntity testProduct = productRepository.getProductByTitlel(productEntity.getTitle());

        LendingEntity lendingEntity
            = RandomTestData.newRandomLendingStausDone(testBorrower3, testProduct);
        lendingRepository.addLending(lendingEntity);
        LendingEntity testLending
            = lendingRepository.getLendingsByProductAndBorrower(testProduct, testBorrower3).get(0);
        LendingEntity testLendingLoadedById = lendingRepository.getLendingById(testLending.getId());

        Assert.assertEquals(testLending, testLendingLoadedById);
    }
}
