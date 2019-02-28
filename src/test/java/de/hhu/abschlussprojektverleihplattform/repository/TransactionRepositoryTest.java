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



    private UserEntity sender = RandomTestData.newRandomTestUser();
    private UserEntity receiver = RandomTestData.newRandomTestUser();

    @Before
    public void saveSetupEntities(){
        userRepository.saveUser(sender);
        userRepository.saveUser(receiver);
    }
    @Test
    public void findById() {
        TransactionEntity transactionEntity
                = RandomTestData.newRandomTestTransaction(sender,receiver);
        transactionRepository.addTransaction(transactionEntity);

        System.out.println(transactionRepository.getAllTransactionsFromUser(sender.getUserId()));
        Assert.assertEquals(transactionEntity.getId(),
                transactionRepository.findById(transactionEntity.getId()
                ).getId()
        );
    }

    @Test
    public void getAllTransactionsFromUser() {
    }

    @Test
    public void addTransaction() {
    }
}