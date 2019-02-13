package de.hhu.abschlussprojektverleihplattform.service.propay;

import org.junit.Assert;
import org.junit.Assert.*;
import org.junit.Test;


public class ProPayServiceTest {

    @Test
    public void testnewuserhaszerobalance(){

        Assert.assertEquals(0,ProPayService.getInstance().checkbalance("alice"));
    }

    @Test
    public void test_can_create_account(){
        Assert.fail();
    }

    @Test
    public void test_increaseuser_has_positive_balance(){
        ProPayService.getInstance().
        Assert.fail();
    }
}
