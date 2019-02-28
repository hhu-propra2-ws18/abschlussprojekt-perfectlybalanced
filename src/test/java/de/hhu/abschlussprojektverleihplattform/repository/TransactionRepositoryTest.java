package de.hhu.abschlussprojektverleihplattform.repository;

import de.hhu.abschlussprojektverleihplattform.model.TransactionEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.utils.RandomTestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionRepositoryTest {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    private UserEntity user1 = RandomTestData.newRandomTestUser();
    private UserEntity user2 = RandomTestData.newRandomTestUser();
    private UserEntity user3 = RandomTestData.newRandomTestUser();

    @Before
    public void saveSetupEntities(){
        userRepository.saveUser(user1);
        userRepository.saveUser(user2);
        userRepository.saveUser(user3);
    }

    @Test
    public void addTransaction() {
        TransactionEntity transactionEntity
            = RandomTestData.newRandomTestTransaction(user1,user2);

        transactionRepository.addTransaction(transactionEntity);

        Assert.assertTrue(
            transactionRepository.getAllTransactionsFromUser(
            user1.getUserId()).contains(transactionEntity)
        );
    }

    @Test
    public void findById() {
        TransactionEntity transactionEntity
            = RandomTestData.newRandomTestTransaction(user1,user2);

        transactionRepository.addTransaction(transactionEntity);

        Assert.assertEquals(transactionEntity.getId(),
            transactionRepository.findById(transactionEntity.getId()
            ).getId()
        );
    }

    @Test
    public void getAllTransactionsFromUser() {
        TransactionEntity transactionEntity1
            = RandomTestData.newRandomTestTransaction(user1,user2);
        TransactionEntity transactionEntity2
            = RandomTestData.newRandomTestTransaction(user3,user1);

        transactionRepository.addTransaction(transactionEntity1);
        transactionRepository.addTransaction(transactionEntity2);

        Assert.assertEquals(2,
            transactionRepository.getAllTransactionsFromUser(user1.getUserId()).size()
        );
    }
}