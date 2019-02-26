package de.hhu.abschlussprojektverleihplattform.utils;

import de.hhu.abschlussprojektverleihplattform.model.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;

public class RandomTestData {

    public static UserEntity newRandomTestUser() {
        return new UserEntity(RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                Role.ROLE_USER);
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
                random.nextInt(1000),
                random.nextInt(1000),
                address,
                owner);
    }

    public static ProductEntity newRandomTestPoductWithPrice(
        UserEntity owner,
        AddressEntity address,
        int price
    ){
        return new ProductEntity(RandomStringUtils.randomAlphabetic(255),
                RandomStringUtils.randomAlphabetic(50),
                price,
                address,
                owner);
    }

    public static LendingEntity newRandomLendingStausDone(
		    UserEntity borrower, ProductEntity product
    ) {
        return new LendingEntity(Lendingstatus.done,
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis() + 86400000),
                borrower,
                product,
                0L,
                0L
	);
    }

    public static LendingEntity newRandomLendingStatusConflict(
        UserEntity borrower,
        ProductEntity productEntity
    ){
        LendingEntity result=newRandomLendingStausDone(borrower,productEntity);
        result.setStatus(Lendingstatus.conflict);
        return result;
    }

    public static Timestamp[] new2SuccessiveTimestamps(){

        Timestamp t1 = new Timestamp(new Date().getTime());
        Timestamp t2 = new Timestamp(t1.getTime()+300000000);

        return new Timestamp[]{t1,t2};
    }
}
