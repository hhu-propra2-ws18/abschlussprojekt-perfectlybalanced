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

    //the loaded versions
    private UserEntity loadedUser1;
    private ProductEntity loadedProduct1;

    @Before
    public void saveSetupEntities() {
        userRepository.saveUser(user1);
        loadedUser1 = userRepository.getUserByFirstname(user1.getFirstname());
        ProductEntity product1
                = RandomTestData.newRandomTestProduct(loadedUser1, address1);
        productRepository.saveProduct(product1);
        loadedProduct1
                = productRepository.getProductByTitlel(product1.getTitle());
    }

    @Test
    public void saveOneLendingToDatabase() {
        LendingEntity lendingEntity
                = RandomTestData.newRandomLendingStausDone(loadedUser1, loadedProduct1);
        lendingRepository.addLending(lendingEntity);

        LendingEntity loadedLending
                = lendingRepository.getLendingByProductAndUser(loadedProduct1, loadedUser1);


        System.out.println("pre loading " + lendingEntity.getStatus());
        System.out.println("after loading " + loadedLending.getStatus());

        lendingEntity.setId(loadedLending.getId());
        assertEquals(loadedLending, lendingEntity);
    }

    @Test
    public void test_get_all_lendings_from_product() {
        LendingEntity lendingEntity
                = RandomTestData.newRandomLendingStausDone(loadedUser1, loadedProduct1);
        lendingRepository.addLending(lendingEntity);

        List<LendingEntity> lendings
                = lendingRepository.getAllLendingsFromProduct(loadedProduct1);
        assertEquals(1, lendings.size());
    }

    @Test
    public void test_getLendingByProductAndUser() {
        LendingEntity lendingEntity
                = RandomTestData.newRandomLendingStausDone(loadedUser1, loadedProduct1);

        lendingRepository.addLending(lendingEntity);
        LendingEntity loadedLending
                = lendingRepository.getLendingByProductAndUser(loadedProduct1, loadedUser1);

        lendingEntity.setId(loadedLending.getId());
        assertEquals(loadedLending, lendingEntity);

    }

    @Test
    public void test_getReturnedLendingFromUser() {
        LendingEntity returnedLendingEntity
                = RandomTestData.newRandomLendingStatusConflict(loadedUser1,loadedProduct1);
        returnedLendingEntity.setStatus(Lendingstatus.returned);
        lendingRepository.addLending(returnedLendingEntity);

        List<LendingEntity> returnedLendings
                = lendingRepository.getAllLendingsFromUser(loadedUser1);


        Assert.assertEquals(1, returnedLendings.size());
    }

    @Test
    public void test_getAllConflicts() {
        LendingEntity lendingEntity
                = RandomTestData.newRandomLendingStatusConflict(loadedUser1, loadedProduct1);

        lendingRepository.addLending(lendingEntity);

        List<LendingEntity> conflictLendings = lendingRepository.getAllConflicts();

        assertEquals(1, conflictLendings.size());
        assertEquals(loadedProduct1, conflictLendings.get(0).getProduct());
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

        List<LendingEntity> allReturnedLendingsFromOwner
                = lendingRepository.getAllRequestsForUser(testOwner1);

        Assert.assertEquals(1, allReturnedLendingsFromOwner.size());
    }
}
