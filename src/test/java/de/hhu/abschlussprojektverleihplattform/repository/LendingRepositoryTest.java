package de.hhu.abschlussprojektverleihplattform.repository;

import de.hhu.abschlussprojektverleihplattform.model.*;
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

    @Test
    public void saveOneLendingToDatabase(){
        UserEntity user = userRepository.getUserByFirstname("Max");
        AddressEntity address = new AddressEntity("street", 1, 47809, "city");
        ProductEntity product = new ProductEntity("description", "title", 100, 10, address, user);
        Timestamp start = new Timestamp(System.currentTimeMillis());
        LendingEntity testLending = new LendingEntity(Lendingstatus.done, start, start, user, product, 0L, 0L);
        lendingRepository.saveLending(testLending);
        System.out.println(user.toString());

        List<LendingEntity> lendingEntities=lendingRepository.getAllLendings();
        System.out.println(lendingEntities.toString());
    }

}