package de.hhu.abschlussprojektverleihplattform.service;

import de.hhu.abschlussprojektverleihplattform.testdummys.LendingRepositoryDummy;
import de.hhu.abschlussprojektverleihplattform.testdummys.PaymentServiceDummy;
import de.hhu.abschlussprojektverleihplattform.testdummys.ReservationDummy;
import de.hhu.abschlussprojektverleihplattform.model.*;
import de.hhu.abschlussprojektverleihplattform.utils.RandomTestData;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;

//NOTE:
//The Dates used here are from May 2020.
//Should the Tests be run after that Time some will fail, since the Dates will then be in the Past.
//Should the Application still be needed after that Time, they have to be replaced with later Dates.

public class LendingServiceTest {

    // Tests for requestLending

    @Test
    public void productHasWrongStatus1() {
        UserEntity actingUser = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        product.setStatus(Productstatus.forBuying);
        Timestamp start1 = new Timestamp(1000L);
        Timestamp end1 = new Timestamp(2000L);
        LendingEntity timeBlocker = new LendingEntity(
            Lendingstatus.confirmt,
            start1,
            end1,
            actingUser,
            product,
            0L,
            0L
        );
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        LendingService logic = new LendingService(null, null);
        Timestamp start2 = new Timestamp(1700L);
        Timestamp end2 = new Timestamp(3000L);

        Exception result = new Exception("0");
        try {
            logic.requestLending(actingUser, product, start2, end2);
        } catch (Exception e) {
            result = e;
        }

        Assert.assertEquals(
            "This Product can only be bought, not lend.",
            result.getMessage()
        );
    }

    @Test
    public void productHasWrongStatus2() {
        UserEntity actingUser = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        product.setStatus(Productstatus.sold);
        Timestamp start1 = new Timestamp(1000L);
        Timestamp end1 = new Timestamp(2000L);
        LendingEntity timeBlocker = new LendingEntity(
            Lendingstatus.confirmt,
            start1,
            end1,
            actingUser,
            product,
            0L,
            0L
        );
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        LendingService logic = new LendingService(null, null);
        Timestamp start2 = new Timestamp(1700L);
        Timestamp end2 = new Timestamp(3000L);

        Exception result = new Exception("0");
        try {
            logic.requestLending(actingUser, product, start2, end2);
        } catch (Exception e) {
            result = e;
        }

        Assert.assertEquals(
            "This Product can only be bought, not lend.",
            result.getMessage()
        );
    }

    @Test
    public void instantTime() {
        UserEntity actingUser = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        LendingService logic = new LendingService(null, null);
        Timestamp start = new Timestamp(4000L);
        Timestamp end = new Timestamp(4000L);

        Exception result = new Exception("0");
        try {
            logic.requestLending(actingUser, product, start, end);
        } catch (Exception e) {
            result = e;
        }

        int totalMoney = product.getSurety() + product.getCost();
        Assert.assertEquals(
            "You can't lend a product for an instant",
            result.getMessage()
        );
    }

    @Test
    public void startAfterEnd() {
        UserEntity actingUser = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        LendingService logic = new LendingService(null, null);
        Timestamp start = new Timestamp(4000L);
        Timestamp end = new Timestamp(3000L);

        Exception result = new Exception("0");
        try {
            logic.requestLending(actingUser, product, start, end);
        } catch (Exception e) {
            result = e;
        }

        int totalMoney = product.getSurety() + product.getCost();
        Assert.assertEquals(
            "If you are searching for a Bug, there is non here. "
            + "The end-date must be after the start-date, you genius!",
            result.getMessage()
        );
    }

    @Test
    public void startIsInThePast() {
        UserEntity actingUser = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        LendingService logic = new LendingService(null, null);
        Long currentMilis = Timestamp.valueOf(LocalDateTime.now()).getTime();
        Timestamp start = new Timestamp(currentMilis - 300);
        Timestamp end = new Timestamp(currentMilis + 3000000);

        Exception result = new Exception("0");
        try {
            logic.requestLending(actingUser, product, start, end);
        } catch (Exception e) {
            result = e;
        }

        int totalMoney = product.getSurety() + product.getCost();
        Assert.assertEquals(
            "You can't change the Past. "
            + "You have to borrow the product after the current time.",
            result.getMessage()
        );
    }

    @Test
    public void userHasNotEnoughMoney() {
        UserEntity actingUser = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        PaymentServiceDummy payment_service = new PaymentServiceDummy();
        payment_service.configurateUsersCurrentBalance(0L, null, false);
        LendingService logic = new LendingService(null, payment_service);
        Timestamp start = new Timestamp(1589187600000L);
        Timestamp end = new Timestamp(1589549400000L);

        Exception result = new Exception("0");
        try {
            logic.requestLending(actingUser, product, start, end);
        } catch (Exception e) {
            result = e;
        }

        int totalMoney = product.getSurety() + product.getCost() * 5;
        Assert.assertEquals(
            "The cost and the surety sum up to: "
            + totalMoney + "€, but you only have: " + 0L + "€.",
            result.getMessage()
        );
    }

    @Test
    public void userBalanceCheckFails() {
        UserEntity actingUser = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        PaymentServiceDummy payment_service = new PaymentServiceDummy();
        Exception exception = new Exception("TestFail");
        payment_service.configurateUsersCurrentBalance(0L, exception, true);
        LendingService logic = new LendingService(null, payment_service);
        Timestamp start = new Timestamp(1589187600000L);
        Timestamp end = new Timestamp(1589549400000L);

        Exception result = new Exception("0");
        try {
            logic.requestLending(actingUser, product, start, end);
        } catch (Exception e) {
            result = e;
        }
        
        Assert.assertEquals(exception, result);
    }

    @Test
    public void reservationSuccess() {
        UserEntity actingUser = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        PaymentServiceDummy payment_service = new PaymentServiceDummy();
        payment_service.configurateUsersCurrentBalance(20000000L, null, false);
        LendingService logic = new LendingService(lending_repository, payment_service);
        Timestamp start = new Timestamp(1589187600000L);
        Timestamp end = new Timestamp(1589549400000L);

        try {
            logic.requestLending(actingUser, product, start, end);
        } catch (Exception e) {
            Assert.assertTrue(false);
        }

        LendingEntity created_lending = lending_repository.getFirst();
        Assert.assertEquals(Lendingstatus.requested, created_lending.getStatus());
        Assert.assertTrue(created_lending.getStart().equals(start));
        Assert.assertTrue(created_lending.getEnd().equals(end));
        Assert.assertEquals(actingUser.getUsername(), created_lending.getBorrower().getUsername());
        Assert.assertEquals(product.getTitle(), created_lending.getProduct().getTitle());
        Assert.assertEquals(0L, (long) created_lending.getCostReservationID());
        Assert.assertEquals(0L, (long) created_lending.getSuretyReservationID());
    }

    // Tests for acceptLendingRequest

    @Test
    public void wrongLendingStatusA() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borrower = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        LendingEntity lending = new LendingEntity(
            Lendingstatus.done,
            start,
            end,
            borrower,
            product,
            0L,
            0L
        );
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        LendingService logic = new LendingService(lending_repository, null);

        Exception result = new Exception("0");
        try {
            logic.acceptLendingRequest(lending);
        } catch (Exception e) {
            result = e;
        }

        Assert.assertEquals(
            "The Lending has the Status: " + Lendingstatus.done
            + " but it needs to be: " + Lendingstatus.requested,
            result.getMessage()
        );
        Assert.assertFalse(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.done, lending.getStatus());
    }

    @Test
    public void timeIsBlocked1() {
        // start is in reservated Time
        UserEntity borrower = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        Timestamp start1 = new Timestamp(1000L);
        Timestamp end1 = new Timestamp(2000L);
        Timestamp start2 = new Timestamp(1700L);
        Timestamp end2 = new Timestamp(3000L);
        LendingEntity lend = new LendingEntity(
            Lendingstatus.requested,
            start2,
            end2,
            borrower,
            product,
            0L,
            0L
        );
        LendingEntity timeBlocker = new LendingEntity(
            Lendingstatus.confirmt,
            start1,
            end1,
            borrower,
            product,
            0L,
            0L
        );
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.addLending(timeBlocker);
        PaymentServiceDummy paymentService = new PaymentServiceDummy();
        paymentService.configureReservateAmount(null, false);
        paymentService.configureTransfer(null, false);
        LendingService logic = new LendingService(lending_repository, null);


        Exception result = new Exception("0");
        try {
            logic.acceptLendingRequest(lend);
        } catch (Exception e) {
            result = e;
        }

        Assert.assertEquals(
            "The Product is not available in the selected time.",
            result.getMessage()
        );
    }

    @Test
    public void timeIsBlocked2() {
        // end is in reservated Time
        UserEntity borrower = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        Timestamp start1 = new Timestamp(1000L);
        Timestamp end1 = new Timestamp(2000L);
        Timestamp start2 = new Timestamp(800L);
        Timestamp end2 = new Timestamp(1500L);
        LendingEntity lend = new LendingEntity(
            Lendingstatus.requested,
            start2,
            end2,
            borrower,
            product,
            0L,
            0L
        );
        LendingEntity timeBlocker = new LendingEntity(
            Lendingstatus.confirmt,
            start1,
            end1,
            borrower,
            product,
            0L,
            0L
        );
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.addLending(timeBlocker);
        PaymentServiceDummy paymentService = new PaymentServiceDummy();
        paymentService.configureReservateAmount(null, false);
        paymentService.configureTransfer(null, false);
        LendingService logic = new LendingService(lending_repository, null);


        Exception result = new Exception("0");
        try {
            logic.acceptLendingRequest(lend);
        } catch (Exception e) {
            result = e;
        }

        Assert.assertEquals(
            "The Product is not available in the selected time.",
            result.getMessage()
        );
    }

    @Test
    public void timeIsBlocked3() {
        // both are in reservated Time
        UserEntity borrower = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        Timestamp start1 = new Timestamp(1000L);
        Timestamp end1 = new Timestamp(2000L);
        Timestamp start2 = new Timestamp(1700L);
        Timestamp end2 = new Timestamp(1900L);
        LendingEntity lend = new LendingEntity(
            Lendingstatus.requested,
            start2,
            end2,
            borrower,
            product,
            0L,
            0L
        );
        LendingEntity timeBlocker = new LendingEntity(
            Lendingstatus.confirmt,
            start1,
            end1,
            borrower,
            product,
            0L,
            0L
        );
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.addLending(timeBlocker);
        PaymentServiceDummy paymentService = new PaymentServiceDummy();
        paymentService.configureReservateAmount(null, false);
        paymentService.configureTransfer(null, false);
        LendingService logic = new LendingService(lending_repository, null);


        Exception result = new Exception("0");
        try {
            logic.acceptLendingRequest(lend);
        } catch (Exception e) {
            result = e;
        }

        Assert.assertEquals(
            "The Product is not available in the selected time.",
            result.getMessage()
        );
    }

    @Test
    public void timeIsBlocked4() {
        // reservated Time within requested Time
        UserEntity borrower = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        Timestamp start1 = new Timestamp(1000L);
        Timestamp end1 = new Timestamp(2000L);
        Timestamp start2 = new Timestamp(800L);
        Timestamp end2 = new Timestamp(3500L);
        LendingEntity lend = new LendingEntity(
            Lendingstatus.requested,
            start2,
            end2,
            borrower,
            product,
            0L,
            0L
        );
        LendingEntity timeBlocker = new LendingEntity(
            Lendingstatus.confirmt,
            start1,
            end1,
            borrower,
            product,
            0L,
            0L
        );
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.addLending(timeBlocker);
        PaymentServiceDummy paymentService = new PaymentServiceDummy();
        paymentService.configureReservateAmount(null, false);
        paymentService.configureTransfer(null, false);
        LendingService logic = new LendingService(lending_repository, null);


        Exception result = new Exception("0");
        try {
            logic.acceptLendingRequest(lend);
        } catch (Exception e) {
            result = e;
        }

        Assert.assertEquals(
            "The Product is not available in the selected time.",
            result.getMessage()
        );
    }

    @Test
    public void timeIsBlocked5() {
        // both times are equal
        UserEntity borrower = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        Timestamp start = new Timestamp(1000L);
        Timestamp end = new Timestamp(2000L);
        LendingEntity lend = new LendingEntity(
            Lendingstatus.requested,
            start,
            end,
            borrower,
            product,
            0L,
            0L
        );
        LendingEntity timeBlocker = new LendingEntity(
            Lendingstatus.confirmt,
            start,
            end,
            borrower,
            product,
            0L,
            0L
        );
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.addLending(timeBlocker);
        PaymentServiceDummy paymentService = new PaymentServiceDummy();
        paymentService.configureReservateAmount(null, false);
        paymentService.configureTransfer(null, false);
        LendingService logic = new LendingService(lending_repository, null);


        Exception result = new Exception("0");
        try {
            logic.acceptLendingRequest(lend);
        } catch (Exception e) {
            result = e;
        }

        Assert.assertEquals(
            "The Product is not available in the selected time.",
            result.getMessage()
        );
    }

    @Test
    public void moneyRequestFails() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borrower = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        LendingEntity lending = new LendingEntity(
                Lendingstatus.requested,
                start,
                end,
                borrower,
                product,
                0L,
                0L
        );
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        PaymentServiceDummy payment_service = new PaymentServiceDummy();
        Exception fail = new Exception("TestFail");
        payment_service.configurateUsersCurrentBalance(0L, fail, true);
        LendingService logic = new LendingService(lending_repository, payment_service);

        Exception result = new Exception("0");
        try {
            logic.acceptLendingRequest(lending);
        } catch (Exception e) {
            result = e;
        }

        Assert.assertEquals(fail, result);
        Assert.assertFalse(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.requested, lending.getStatus());
    }

    @Test
    public void borrowerHasNoMoney() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borrower = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        LendingEntity lending = new LendingEntity(
                Lendingstatus.requested,
                start,
                end,
                borrower,
                product,
                0L,
                0L
        );
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        PaymentServiceDummy payment_service = new PaymentServiceDummy();
        payment_service.configurateUsersCurrentBalance(0L, null, false);
        LendingService logic = new LendingService(lending_repository, payment_service);

        Exception result = new Exception("0");
        try {
            logic.acceptLendingRequest(lending);
        } catch (Exception e) {
            result = e;
        }

        Assert.assertEquals("The borrower currently hasn't enough money for the lending", result.getMessage());
        Assert.assertFalse(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.requested, lending.getStatus());
    }

    @Test
    public void reservation1Fails() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borrower = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        LendingEntity lending = new LendingEntity(
            Lendingstatus.requested,
            start,
            end,
            borrower,
            product,
            0L,
            0L
        );
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        PaymentServiceDummy payment_service = new PaymentServiceDummy();
        Exception fail = new Exception("TestFail");
        payment_service.configurateUsersCurrentBalance(Long.MAX_VALUE, null, false);
        payment_service.configureReservateAmount(fail, true);
        LendingService logic = new LendingService(lending_repository, payment_service);

        Exception result = new Exception("0");
        try {
            logic.acceptLendingRequest(lending);
        } catch (Exception e) {
            result = e;
        }

        Assert.assertEquals(fail, result);
        Assert.assertFalse(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.requested, lending.getStatus());
    }

    @Test
    public void reservation2Fails() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borrower = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        LendingEntity lending = new LendingEntity(
            Lendingstatus.requested,
            start,
            end,
            borrower,
            product,
            0L,
            0L
        );
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        PaymentServiceDummy payment_service = new PaymentServiceDummy();
        payment_service.configurateUsersCurrentBalance(Long.MAX_VALUE, null, false);
        Exception fail = new Exception("TestFail");
        payment_service.configureReservateAmount(null, false);
        payment_service.configureReservateAmountSecondOneFails(fail);
        LendingService logic = new LendingService(lending_repository, payment_service);

        Exception result = new Exception("0");
        try {
            logic.acceptLendingRequest(lending);
        } catch (Exception e) {
            result = e;
        }

        Assert.assertEquals(fail, result);
        Assert.assertFalse(payment_service.getLastWasTransfer());
        Assert.assertEquals(borrower.getUsername(), payment_service.getLastUsername());
        Assert.assertEquals(1L, (long) payment_service.getLastId());
        ReservationDummy reservation = payment_service.findReservation(1L);
        Assert.assertEquals(product.getCost(), reservation.getAmount());
        Assert.assertEquals(borrower.getUsername(), reservation.getFromUsername());
        Assert.assertEquals(owner.getUsername(), reservation.getToUsername());
    }

    @Test
    public void requestGetsAccepted1() {
        Timestamp start = new Timestamp(1521811800000L);
        Timestamp end = new Timestamp(1522326000000L);
        UserEntity borrower = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        LendingEntity lending = new LendingEntity(
            Lendingstatus.requested,
            start,
            end,
            borrower,
            product,
            0L,
            0L
        );
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        PaymentServiceDummy payment_service = new PaymentServiceDummy();
        payment_service.configurateUsersCurrentBalance(Long.MAX_VALUE, null, false);
        payment_service.configureReservateAmount(null, false);
        payment_service.configureTransfer(null, false);
        LendingService logic = new LendingService(lending_repository, payment_service);

        try {
            logic.acceptLendingRequest(lending);
        } catch (Exception e) {
            Assert.assertTrue(false);
        }

        Assert.assertTrue(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.confirmt, lending.getStatus());
        Assert.assertTrue(payment_service.getLastWasTransfer());
        Assert.assertEquals((long) lending.getCostReservationID(),
            (long) payment_service.getLastId());
        Assert.assertEquals(borrower.getUsername(), payment_service.getLastUsername());
        ReservationDummy cost
            = payment_service.findReservation(lending.getCostReservationID());
        ReservationDummy surety
            = payment_service.findReservation(lending.getSuretyReservationID());
        Assert.assertEquals(borrower.getUsername(), cost.getFromUsername());
        Assert.assertEquals(product.getOwner().getUsername(), cost.getToUsername());
        //Timedifference is between 5 and 6 Days
        Assert.assertEquals(product.getCost() * 6, cost.getAmount());
        Assert.assertEquals(borrower.getUsername(), surety.getFromUsername());
        Assert.assertEquals(product.getOwner().getUsername(), surety.getToUsername());
        Assert.assertEquals(product.getSurety(), surety.getAmount());
    }

    @Test
    public void requestGetsAccepted2() {
        Timestamp start = new Timestamp(1546804200000L);
        Timestamp end = new Timestamp(1547148600000L);
        UserEntity borrower = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        LendingEntity lending = new LendingEntity(
            Lendingstatus.requested,
            start,
            end,
            borrower,
            product,
            0L,
            0L
        );
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        PaymentServiceDummy payment_service = new PaymentServiceDummy();
        payment_service.configurateUsersCurrentBalance(Long.MAX_VALUE, null, false);
        payment_service.configureReservateAmount(null, false);
        payment_service.configureTransfer(null, false);
        LendingService logic = new LendingService(lending_repository, payment_service);

        try {
            logic.acceptLendingRequest(lending);
        } catch (Exception e) {
            Assert.assertTrue(false);
        }

        Assert.assertTrue(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.confirmt, lending.getStatus());
        Assert.assertTrue(payment_service.getLastWasTransfer());
        Assert.assertEquals((long) lending.getCostReservationID(),
            (long) payment_service.getLastId());
        Assert.assertEquals(borrower.getUsername(), payment_service.getLastUsername());
        ReservationDummy cost
            = payment_service.findReservation(lending.getCostReservationID());
        ReservationDummy surety
            = payment_service.findReservation(lending.getSuretyReservationID());
        Assert.assertEquals(borrower.getUsername(), cost.getFromUsername());
        Assert.assertEquals(product.getOwner().getUsername(), cost.getToUsername());
        //Timedifference is between 3 and 4 Days
        Assert.assertEquals(product.getCost() * 4, cost.getAmount());
        Assert.assertEquals(borrower.getUsername(), surety.getFromUsername());
        Assert.assertEquals(product.getOwner().getUsername(), surety.getToUsername());
        Assert.assertEquals(product.getSurety(), surety.getAmount());
    }

    // Tests for denyLendingRequest

    @Test
    public void wrongLendingStatusB() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borrower = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        LendingEntity lending = new LendingEntity(
            Lendingstatus.done,
            start,
            end,
            borrower,
            product,
            0L,
            0L
        );
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        LendingService logic = new LendingService(lending_repository, null);

        Exception result = new Exception("0");
        try {
            logic.denyLendingRequest(lending);
        } catch (Exception e) {
            result = e;
        }

        Assert.assertEquals(
            "The Lending has the Status: " + Lendingstatus.done
            + " but it needs to be: " + Lendingstatus.requested,
            result.getMessage()
        );
        Assert.assertFalse(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.done, lending.getStatus());
    }

    @Test
    public void requestGetsDenied() throws Exception {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borrower = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        LendingEntity lending = new LendingEntity(
            Lendingstatus.requested,
            start,
            end,
            borrower,
            product,
            0L,
            0L
        );
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        LendingService logic = new LendingService(lending_repository, null);

        logic.denyLendingRequest(lending);

        Assert.assertTrue(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.denied, lending.getStatus());
    }

    // Tests for returnProduct

    @Test
    public void wrongLendingStatusC() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borrower = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        LendingEntity lending = new LendingEntity(
            Lendingstatus.done,
            start,
            end,
            borrower,
            product,
            0L,
            0L
        );
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        LendingService logic = new LendingService(lending_repository, null);

        Exception result = new Exception("0");
        try {
            logic.returnProduct(lending);
        } catch (Exception e) {
            result = e;
        }

        Assert.assertEquals(
            "The Lending has the Status: " + Lendingstatus.done
            + " but it needs to be: " + Lendingstatus.confirmt,
            result.getMessage()
        );
        Assert.assertFalse(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.done, lending.getStatus());
    }

    @Test
    public void productGetsReturned() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borrower = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        LendingEntity lending = new LendingEntity(
            Lendingstatus.confirmt,
            start,
            end,
            borrower,
            product,
            0L,
            0L
        );
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        LendingService logic = new LendingService(lending_repository, null);

        try {
            logic.returnProduct(lending);
        } catch (Exception e) {
            Assert.assertTrue(false);
        }

        Assert.assertTrue(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.returned, lending.getStatus());
    }

    // Tests for acceptReturnedProduct

    @Test
    public void wrongLendingStatusD() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borrower = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        LendingEntity lending = new LendingEntity(
            Lendingstatus.done,
            start,
            end,
            borrower,
            product,
            0L,
            0L
        );
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        LendingService logic = new LendingService(lending_repository, null);

        Exception result = new Exception("0");
        try {
            logic.acceptReturnedProduct(lending);
        } catch (Exception e) {
            result = e;
        }

        Assert.assertEquals(
            "The Lending has the Status: " + Lendingstatus.done
            + " but it needs to be: " + Lendingstatus.returned,
            result.getMessage()
        );
        Assert.assertFalse(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.done, lending.getStatus());
    }

    @Test
    public void returnReservationFails() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borrower = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        LendingEntity lending = new LendingEntity(
            Lendingstatus.returned,
            start,
            end,
            borrower,
            product,
            0L,
            0L
        );
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        PaymentServiceDummy payment_service = new PaymentServiceDummy();
        Exception fail = new Exception("TestFail");
        payment_service.configureReturn(fail, true);
        LendingService logic = new LendingService(lending_repository, payment_service);

        Exception result = new Exception("0");
        try {
            logic.acceptReturnedProduct(lending);
        } catch (Exception e) {
            result = e;
        }

        Assert.assertFalse(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.returned, lending.getStatus());
        Assert.assertEquals(fail, result);
    }

    @Test
    public void productGetsReturnedInGoodCondition() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borrower = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        LendingEntity lending = new LendingEntity(
            Lendingstatus.returned,
            start,
            end,
            borrower,
            product,
            0L,
            20L
        );
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        PaymentServiceDummy payment_service = new PaymentServiceDummy();
        payment_service.configureReturn(null, false);
        LendingService logic = new LendingService(lending_repository, payment_service);

        try {
            logic.acceptReturnedProduct(lending);
        } catch (Exception e) {
            Assert.assertTrue(false);
        }

        Assert.assertTrue(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.done, lending.getStatus());
        Assert.assertFalse(payment_service.getLastWasTransfer());
        Assert.assertEquals(
            (long) lending.getSuretyReservationID(),
            (long) payment_service.getLastId()
        );
        Assert.assertEquals(borrower.getUsername(), payment_service.getLastUsername());
    }

    // Tests for denyReturnedProduct

    @Test
    public void wrongLendingStatusE() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borrower = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        LendingEntity lending = new LendingEntity(
            Lendingstatus.done,
            start,
            end,
            borrower,
            product,
            0L,
            0L
        );
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        LendingService logic = new LendingService(lending_repository, null);

        Exception result = new Exception("0");
        try {
            logic.denyReturnedProduct(lending);
        } catch (Exception e) {
            result = e;
        }

        Assert.assertEquals(
            "The Lending has the Status: " + Lendingstatus.done
            + " but it needs to be: " + Lendingstatus.returned,
            result.getMessage()
        );
        Assert.assertFalse(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.done, lending.getStatus());
    }

    @Test
    public void productGetsReturnedInBadCondition() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borrower = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        LendingEntity lending = new LendingEntity(
            Lendingstatus.returned,
            start,
            end,
            borrower,
            product,
            0L,
            0L
        );
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        LendingService logic = new LendingService(lending_repository, null);

        try {
            logic.denyReturnedProduct(lending);
        } catch (Exception e) {
            Assert.assertTrue(false);
        }

        Assert.assertTrue(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.conflict, lending.getStatus());
    }

    // Tests for ownerReceivesSuretyAfterConflict

    @Test
    public void wrongLendingStatusF() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borrower = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        LendingEntity lending = new LendingEntity(
            Lendingstatus.done,
            start,
            end,
            borrower,
            product,
            0L,
            0L
        );
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        LendingService logic = new LendingService(lending_repository, null);

        Exception result = new Exception("0");
        try {
            logic.ownerReceivesSuretyAfterConflict(lending);
        } catch (Exception e) {
            result = e;
        }

        Assert.assertEquals(
            "The Lending has the Status: " + Lendingstatus.done
            + " but it needs to be: " + Lendingstatus.conflict,
            result.getMessage()
        );
        Assert.assertFalse(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.done, lending.getStatus());
    }

    @Test
    public void ownerRecivesSuretyButTranferFails() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borrower = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        LendingEntity lending = new LendingEntity(
            Lendingstatus.conflict,
            start,
            end,
            borrower,
            product,
            0L,
            0L
        );
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        PaymentServiceDummy payment_service = new PaymentServiceDummy();
        Exception fail = new Exception("TestFail");
        payment_service.configureTransfer(fail, true);
        LendingService logic = new LendingService(lending_repository, payment_service);

        Exception result = new Exception("0");
        try {
            logic.ownerReceivesSuretyAfterConflict(lending);
        } catch (Exception e) {
            result = e;
        }

        Assert.assertEquals(fail, result);
        Assert.assertFalse(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.conflict, lending.getStatus());
    }

    @Test
    public void ownerRecivesSurety() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borrower = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        LendingEntity lending = new LendingEntity(
            Lendingstatus.conflict,
            start,
            end,
            borrower,
            product,
            0L,
            0L
        );
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        PaymentServiceDummy payment_service = new PaymentServiceDummy();
        payment_service.configureTransfer(null, false);
        LendingService logic = new LendingService(lending_repository, payment_service);

        try {
            logic.ownerReceivesSuretyAfterConflict(lending);
        } catch (Exception e) {
            Assert.assertTrue(false);
        }

        Assert.assertTrue(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.done, lending.getStatus());
        Assert.assertTrue(payment_service.getLastWasTransfer());
        Assert.assertEquals(
            (long) lending.getSuretyReservationID(),
            (long) payment_service.getLastId()
        );
        Assert.assertEquals(borrower.getUsername(), payment_service.getLastUsername());
    }

    // Tests for borrowerReceivesSuretyAfterConflict

    @Test
    public void wrongLendingStatusG() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borrower = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        LendingEntity lending = new LendingEntity(
            Lendingstatus.done,
            start,
            end,
            borrower,
            product,
            0L,
            0L
        );
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        LendingService logic = new LendingService(lending_repository, null);

        Exception result = new Exception("0");
        try {
            logic.borrowerReceivesSuretyAfterConflict(lending);
        } catch (Exception e) {
            result = e;
        }

        Assert.assertEquals(
            "The Lending has the Status: " + Lendingstatus.done
            + " but it needs to be: " + Lendingstatus.conflict,
            result.getMessage()
        );
        Assert.assertFalse(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.done, lending.getStatus());
    }

    @Test
    public void borrowerRecivesSuretyButTranferFails() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borrower = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        LendingEntity lending = new LendingEntity(
            Lendingstatus.conflict,
            start,
            end,
            borrower,
            product,
            0L,
            0L
        );
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        PaymentServiceDummy payment_service = new PaymentServiceDummy();
        Exception fail = new Exception("TestFail");
        payment_service.configureReturn(fail, true);
        LendingService logic = new LendingService(lending_repository, payment_service);

        Exception result = new Exception("0");
        try {
            logic.borrowerReceivesSuretyAfterConflict(lending);
        } catch (Exception e) {
            result = e;
        }

        Assert.assertEquals(fail, result);
        Assert.assertFalse(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.conflict, lending.getStatus());
    }

    @Test
    public void borrowerRecivesSurety() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borrower = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        LendingEntity lending = new LendingEntity(
            Lendingstatus.conflict,
            start,
            end,
            borrower,
            product,
            0L,
            0L
        );
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        PaymentServiceDummy payment_service = new PaymentServiceDummy();
        payment_service.configureTransfer(null, false);
        LendingService logic = new LendingService(lending_repository, payment_service);

        try {
            logic.borrowerReceivesSuretyAfterConflict(lending);
        } catch (Exception e) {
            Assert.assertTrue(false);
        }

        Assert.assertTrue(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.done, lending.getStatus());
        Assert.assertFalse(payment_service.getLastWasTransfer());
        Assert.assertEquals(
            (long) lending.getSuretyReservationID(),
            (long) payment_service.getLastId()
        );
        Assert.assertEquals(borrower.getUsername(), payment_service.getLastUsername());
    }

    // Tests for daysBetweenTwoTimestamps

    @Test
    public void differenceIs3() {
        Timestamp first = new Timestamp(1557266400000L);
        Timestamp second = new Timestamp(1557525600000L);
        LendingService logic = new LendingService(null, null);

        int result = logic.daysBetweenTwoTimestamps(first, second);

        Assert.assertEquals(3, result);
    }

    @Test
    public void differenceIs4() {
        Timestamp first = new Timestamp(1557266400000L);
        Timestamp second = new Timestamp(1557525600010L);
        LendingService logic = new LendingService(null, null);

        int result = logic.daysBetweenTwoTimestamps(first, second);

        Assert.assertEquals(4, result);
    }
}
