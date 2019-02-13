package de.hhu.abschlussprojektverleihplattform.service.propay;

import org.junit.Assert;
import org.junit.Assert.*;
import org.junit.Test;


public class ProPayServiceTest {

    @Test
    public void testnewuserhaszerobalance(){

        Assert.assertEquals(0,ProPayService.getInstance().checkbalance("alice"));
    }
}
