package de.hhu.abschlussprojektverleihplattform.utils;

import com.mysql.cj.xdevapi.AddResult;
import de.hhu.abschlussprojektverleihplattform.model.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.sql.Timestamp;
import java.util.Random;

public class RandomTestData {

    public static UserEntity newRandomTestUser() {
        return new UserEntity(RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10));
    }

    public static AddressEntity newRandomTestAddress() {
        Random random = new Random();
        return new AddressEntity(RandomStringUtils.randomAlphabetic(10),
                random.nextInt(),
                random.nextInt(10000) + 9999,
                RandomStringUtils.randomAlphabetic(10));
    }

    public static ProductEntity newRandomTestProduct(UserEntity owner, AddressEntity address) {
        Random random = new Random();
        return new ProductEntity(RandomStringUtils.randomAlphabetic(255),
                RandomStringUtils.randomAlphabetic(50),
                random.nextInt(),
                random.nextInt(),
                address,
                owner);
    }

    public static LendingEntity newRandomLendingStausDone(UserEntity borrower, ProductEntity product) {
        return new LendingEntity(Lendingstatus.done,
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis() + 86400000),
                borrower,
                product,
                0L,
                0L);
    }

}
