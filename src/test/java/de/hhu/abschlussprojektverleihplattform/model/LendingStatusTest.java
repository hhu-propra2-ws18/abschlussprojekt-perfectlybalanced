package de.hhu.abschlussprojektverleihplattform.model;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

public class LendingStatusTest {

    @Test
    public void test_lending_status(){

        Lendingstatus status1 = Lendingstatus.valueOf("denied");

        Assert.assertEquals(5, status1.getValue());

        Assert.assertEquals("denied",status1.name());

        Assert.assertEquals(5,Lendingstatus.getLemdingStatusValueFrom(status1));
    }
}
