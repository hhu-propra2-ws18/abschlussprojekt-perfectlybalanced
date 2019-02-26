package de.hhu.abschlussprojektverleihplattform.service;

import de.hhu.abschlussprojektverleihplattform.model.AddressEntity;
import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.Productstatus;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.testdummys.PaymentServiceDummy;
import de.hhu.abschlussprojektverleihplattform.testdummys.ProductRepositoryDummy;
import de.hhu.abschlussprojektverleihplattform.testdummys.ReservationDummy;
import de.hhu.abschlussprojektverleihplattform.utils.RandomTestData;
import org.junit.Assert;
import org.junit.Test;

public class SellServiceTest {

    @Test
    public void productIsForLending() {
        UserEntity owner = RandomTestData.newRandomTestUser();
        UserEntity actingUser = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        product.setStatus(Productstatus.forLending);
        ProductRepositoryDummy productRepository = new ProductRepositoryDummy();
        productRepository.setProductToUpdate(product);
        SellService logic = new SellService(productRepository, null);

        Exception result = new Exception("0");
        try {
            logic.buyProduct(actingUser, product);
        } catch (Exception e) {
            result = e;
        }

        Assert.assertFalse(productRepository.getHasBeenUpdated());
        Assert.assertEquals("This Product can only be lend, not bought.", result.getMessage());
    }

    @Test
    public void productIsAllreadySold() {
        UserEntity owner = RandomTestData.newRandomTestUser();
        UserEntity actingUser = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        product.setStatus(Productstatus.sold);
        ProductRepositoryDummy productRepository = new ProductRepositoryDummy();
        productRepository.setProductToUpdate(product);
        SellService logic = new SellService(productRepository, null);

        Exception result = new Exception("0");
        try {
            logic.buyProduct(actingUser, product);
        } catch (Exception e) {
            result = e;
        }

        Assert.assertFalse(productRepository.getHasBeenUpdated());
        Assert.assertEquals("This Product already has been sold.", result.getMessage());
    }

    @Test
    public void userHasNotENoughMoney() {
        UserEntity owner = RandomTestData.newRandomTestUser();
        UserEntity actingUser = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestPoductWithPrice(owner, address, 40);
        ProductRepositoryDummy productRepository = new ProductRepositoryDummy();
        productRepository.setProductToUpdate(product);
        PaymentServiceDummy paymentService = new PaymentServiceDummy();
        paymentService.configurateUsersCurrentBalance(30L, null, false);
        SellService logic = new SellService(productRepository, paymentService);

        Exception result = new Exception("0");
        try {
            logic.buyProduct(actingUser, product);
        } catch (Exception e) {
            result = e;
        }

        Assert.assertFalse(productRepository.getHasBeenUpdated());
        Assert.assertEquals("The cost and the surety sum up to: "
                + 40 + "€, but you only have: " + 30 + "€.", result.getMessage());
    }

    @Test
    public void userHasMonayFails() {
        UserEntity owner = RandomTestData.newRandomTestUser();
        UserEntity actingUser = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestPoductWithPrice(owner, address, 40);
        ProductRepositoryDummy productRepository = new ProductRepositoryDummy();
        productRepository.setProductToUpdate(product);
        PaymentServiceDummy paymentService = new PaymentServiceDummy();
        Exception fail = new Exception("TestFail");
        paymentService.configurateUsersCurrentBalance(30L, fail, true);
        SellService logic = new SellService(productRepository, paymentService);

        Exception result = new Exception("0");
        try {
            logic.buyProduct(actingUser, product);
        } catch (Exception e) {
            result = e;
        }

        Assert.assertFalse(productRepository.getHasBeenUpdated());
        Assert.assertEquals(fail, result);
    }

    @Test
    public void ReservationFails() {
        UserEntity owner = RandomTestData.newRandomTestUser();
        UserEntity actingUser = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestPoductWithPrice(owner, address, 40);
        ProductRepositoryDummy productRepository = new ProductRepositoryDummy();
        productRepository.setProductToUpdate(product);
        PaymentServiceDummy paymentService = new PaymentServiceDummy();
        paymentService.configurateUsersCurrentBalance(50L, null, false);
        Exception fail = new Exception("TestFail");
        paymentService.configureReservateAmount(fail, true);
        SellService logic = new SellService(productRepository, paymentService);

        Exception result = new Exception("0");
        try {
            logic.buyProduct(actingUser, product);
        } catch (Exception e) {
            result = e;
        }

        Assert.assertFalse(productRepository.getHasBeenUpdated());
        Assert.assertEquals(fail, result);
    }

    @Test
    public void TransferFails() {
        UserEntity owner = RandomTestData.newRandomTestUser();
        UserEntity actingUser = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestPoductWithPrice(owner, address, 40);
        ProductRepositoryDummy productRepository = new ProductRepositoryDummy();
        productRepository.setProductToUpdate(product);
        PaymentServiceDummy paymentService = new PaymentServiceDummy();
        paymentService.configurateUsersCurrentBalance(50L, null, false);
        paymentService.configureReservateAmount(null, false);
        Exception fail = new Exception("TestFail");
        paymentService.configureTransfer(fail, true);
        SellService logic = new SellService(productRepository, paymentService);

        Exception result = new Exception("0");
        try {
            logic.buyProduct(actingUser, product);
        } catch (Exception e) {
            result = e;
        }

        Assert.assertFalse(productRepository.getHasBeenUpdated());
        Assert.assertEquals(fail, result);
        ReservationDummy reservation = paymentService.findReservation(1L);
        Assert.assertEquals(40, reservation.getAmount());
        Assert.assertEquals(actingUser.getUsername(), reservation.getFromUsername());
        Assert.assertEquals(owner.getUsername(), reservation.getToUsername());
        Assert.assertTrue(paymentService.getLastWasTransfer());
        Assert.assertEquals(actingUser.getUsername(), paymentService.getLastUsername());
        Assert.assertEquals(1L, (long) paymentService.getLastId());
    }

    @Test
    public void BuySuccessfull() {
        UserEntity owner = RandomTestData.newRandomTestUser();
        UserEntity actingUser = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestPoductWithPrice(owner, address, 45);
        ProductRepositoryDummy productRepository = new ProductRepositoryDummy();
        productRepository.setProductToUpdate(product);
        PaymentServiceDummy paymentService = new PaymentServiceDummy();
        paymentService.configurateUsersCurrentBalance(50L, null, false);
        paymentService.configureReservateAmount(null, false);
        paymentService.configureTransfer(null, false);
        SellService logic = new SellService(productRepository, paymentService);

        try {
            logic.buyProduct(actingUser, product);
        } catch (Exception e) {
            Assert.assertTrue(false);
        }

        Assert.assertTrue(productRepository.getHasBeenUpdated());
        Assert.assertEquals(Productstatus.sold, product.getStatus());
        ReservationDummy reservation = paymentService.findReservation(1L);
        Assert.assertEquals(45, reservation.getAmount());
        Assert.assertEquals(actingUser.getUsername(), reservation.getFromUsername());
        Assert.assertEquals(owner.getUsername(), reservation.getToUsername());
        Assert.assertTrue(paymentService.getLastWasTransfer());
        Assert.assertEquals(actingUser.getUsername(), paymentService.getLastUsername());
        Assert.assertEquals(1L, (long) paymentService.getLastId());
    }
}
