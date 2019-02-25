package de.hhu.abschlussprojektverleihplattform.service;

import de.hhu.abschlussprojektverleihplattform.testdummys.LendingRepositoryDummy;
import de.hhu.abschlussprojektverleihplattform.testdummys.PaymentServiceDummy;
import de.hhu.abschlussprojektverleihplattform.testdummys.PaymentStatus;
import de.hhu.abschlussprojektverleihplattform.testdummys.ReservationDummy;
import de.hhu.abschlussprojektverleihplattform.model.*;
import de.hhu.abschlussprojektverleihplattform.utils.RandomTestData;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Timestamp;

public class LendingServiceTest {
    
    // Tests for requestLending

    @Test
    public void timeIsBlocked1() {
        // start is in reservated Time
        UserEntity actingUser = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner,address);
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
        lending_repository.addLending(timeBlocker);
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_repository, payment_service);
        Timestamp start2 = new Timestamp(1700L);
        Timestamp end2 = new Timestamp(3000L);

        try{
            logic.requestLending(actingUser, product, start2, end2);
            Assert.fail();
        }catch (Exception e){
            System.out.println();
        }
    }

    @Test
    public void timeIsBlocked2() {
        // end is in reservated Time
        UserEntity actingUser = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
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
        lending_repository.addLending(timeBlocker);
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_repository, payment_service);
        Timestamp start2 = new Timestamp(800L);
        Timestamp end2 = new Timestamp(1500L);

        try{
            logic.requestLending(actingUser, product, start2, end2);
            Assert.fail();
        }catch (Exception e){
            System.out.println();
        }
    }

    @Test
    public void timeIsBlocked3() {
        // both are in reservated Time
        UserEntity actingUser = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
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
        lending_repository.addLending(timeBlocker);
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_repository, payment_service);
        Timestamp start2 = new Timestamp(1800L);
        Timestamp end2 = new Timestamp(1900L);

        try{
            logic.requestLending(actingUser, product, start2, end2);
            Assert.fail();
        }catch (Exception e){
            System.out.println();
        }
    }

    @Test
    public void timeIsBlocked4() {
        // reservated Time within requested Time
        UserEntity actingUser = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
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
        lending_repository.addLending(timeBlocker);
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_repository, payment_service);
        Timestamp start2 = new Timestamp(800L);
        Timestamp end2 = new Timestamp(3500L);

        try{
            logic.requestLending(actingUser, product, start2, end2);
            Assert.fail();
        }catch (Exception e){
            System.out.println();
        }
    }

    @Test
    public void userHasNotEnoughMoney() {
        // reservated Time within requested Time
        UserEntity actingUser = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        PaymentServiceDummy payment_service = new PaymentServiceDummy(false, true, true, true);
        LendingService logic = new LendingService(lending_repository, payment_service);
        Timestamp start = new Timestamp(800L);
        Timestamp end = new Timestamp(3500L);

        try{
            logic.requestLending(actingUser, product, start, end);
            Assert.fail();
        }catch (Exception e){
            System.out.println();
        }
    }

    @Test
    public void reservationSuccess1() throws Exception{
        // reservated Time within requested Time
        UserEntity actingUser = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_repository, payment_service);
        Timestamp start = new Timestamp(1557543600000L);
        Timestamp end = new Timestamp(1557900000000L);

        logic.requestLending(actingUser, product, start, end);

        LendingEntity created_lending = lending_repository.getFirst();
        Assert.assertEquals(Lendingstatus.requested, created_lending.getStatus());
        Assert.assertTrue(created_lending.getStart().equals(start));
        Assert.assertTrue(created_lending.getEnd().equals(end));
        Assert.assertEquals(actingUser.getUsername(), created_lending.getBorrower().getUsername());
        Assert.assertEquals(product.getTitle(), created_lending.getProduct().getTitle());
        Assert.assertEquals(0L, (long) created_lending.getCostReservationID());
        Assert.assertEquals(0L, (long) created_lending.getSuretyReservationID());
    }

    @Test
    public void reservationSuccess2() throws Exception{
        // reservated Time within requested Time
        UserEntity actingUser = RandomTestData.newRandomTestUser();
        UserEntity owner = RandomTestData.newRandomTestUser();
        AddressEntity address = RandomTestData.newRandomTestAddress();
        ProductEntity product = RandomTestData.newRandomTestProduct(owner, address);
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_repository, payment_service);
        Timestamp start = new Timestamp(1521811800000L);
        Timestamp end = new Timestamp(1522326000000L);

        logic.requestLending(actingUser, product, start, end);

        LendingEntity created_lending = lending_repository.getFirst();
        Assert.assertEquals(Lendingstatus.requested, created_lending.getStatus());
        Assert.assertTrue(created_lending.getStart().equals(start));
        Assert.assertTrue(created_lending.getEnd().equals(end));
        Assert.assertEquals(actingUser.getUsername(), created_lending.getBorrower().getUsername());
        Assert.assertEquals(product.getTitle(), created_lending.getProduct().getTitle());
        Assert.assertEquals(0L, (long) created_lending.getCostReservationID());
        Assert.assertEquals(0L, (long) created_lending.getSuretyReservationID());
    }

    // Test for decideLendingRequest

    @Test
    public void requestGetsDenied() throws Exception{
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
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_repository, payment_service);

        logic.denyLendingRequest(lending);

        Assert.assertTrue(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.denied, lending.getStatus());
    }

    // Tests for acceptLendingRequest

    @Test
    public void reservationFails() {
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
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, false, true, true);
        LendingService logic = new LendingService(lending_repository, payment_service);

        try{
            logic.acceptLendingRequest(lending);
            Assert.fail();
        }catch (Exception e){
            System.out.println();
        }

        Assert.assertFalse(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.requested, lending.getStatus());
    }

    @Test
    public void paymentFails() {
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
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, false, true);
        LendingService logic = new LendingService(lending_repository, payment_service);

        try {
            logic.acceptLendingRequest(lending);
            Assert.fail();
        }catch (Exception e){
            System.out.println(e.toString());
        }

        Assert.assertFalse(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.requested, lending.getStatus());
        ReservationDummy cost = payment_service.findReservation(1L);
        ReservationDummy surety = payment_service.findReservation(2L);
        Assert.assertEquals(borrower.getUsername(), cost.getFrom().getUsername());
        Assert.assertEquals(product.getOwner().getUsername(), cost.getTo().getUsername());
        //Timedifference is between 5 and 6 Days
        Assert.assertEquals(product.getCost() * 6, cost.getAmount());
        Assert.assertEquals(PaymentStatus.returned, cost.getStatus());
        Assert.assertEquals(borrower.getUsername(), surety.getFrom().getUsername());
        Assert.assertEquals(product.getOwner().getUsername(), surety.getTo().getUsername());
        Assert.assertEquals(product.getSurety(), surety.getAmount());
        Assert.assertEquals(PaymentStatus.returned, surety.getStatus());
    }

    @Test
    public void requestGetsAccepted1() throws Exception{
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
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_repository, payment_service);


        logic.acceptLendingRequest(lending);

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
        Assert.assertEquals(borrower.getUsername(), cost.getFrom().getUsername());
        Assert.assertEquals(product.getOwner().getUsername(), cost.getTo().getUsername());
        //Timedifference is between 5 and 6 Days
        Assert.assertEquals(product.getCost() * 6, cost.getAmount());
        Assert.assertEquals(PaymentStatus.payed, cost.getStatus());
        Assert.assertEquals(borrower.getUsername(), surety.getFrom().getUsername());
        Assert.assertEquals(product.getOwner().getUsername(), surety.getTo().getUsername());
        Assert.assertEquals(product.getSurety(), surety.getAmount());
        Assert.assertEquals(PaymentStatus.reservated, surety.getStatus());
    }

    @Test
    public void requestGetsAccepted2() throws Exception{
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
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_repository, payment_service);

        logic.acceptLendingRequest(lending);

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
        Assert.assertEquals(borrower.getUsername(), cost.getFrom().getUsername());
        Assert.assertEquals(product.getOwner().getUsername(), cost.getTo().getUsername());
        //Timedifference is between 3 and 4 Days
        Assert.assertEquals(product.getCost() * 4, cost.getAmount());
        Assert.assertEquals(PaymentStatus.payed, cost.getStatus());
        Assert.assertEquals(borrower.getUsername(), surety.getFrom().getUsername());
        Assert.assertEquals(product.getOwner().getUsername(), surety.getTo().getUsername());
        Assert.assertEquals(product.getSurety(), surety.getAmount());
        Assert.assertEquals(PaymentStatus.reservated, surety.getStatus());
    }

    // Test for returnProduct

    @Test
    public void productGetsReturned() throws Exception{
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

        logic.returnProduct(lending);

        Assert.assertTrue(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.returned, lending.getStatus());
    }

    // Test for denyReturnedProduct

    @Test
    public void productGetsReturnedInBadCondition() throws Exception{
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

        logic.denyReturnedProduct(lending);

        Assert.assertTrue(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.conflict, lending.getStatus());
    }

    // Tests for acceptReturnedProduct

    @Test
    public void productGetsReturnedInGoodCondition() throws Exception{
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
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_repository, payment_service);

        boolean result = logic.acceptReturnedProduct(lending);

        Assert.assertTrue(result);
        Assert.assertTrue(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.done, lending.getStatus());
        Assert.assertFalse(payment_service.getLastWasTransfer());
        Assert.assertEquals(20L, (long) payment_service.getLastId());
        Assert.assertEquals(borrower.getUsername(), payment_service.getLastUsername());
    }

    @Test
    public void returnReservationFails() throws Exception {
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
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, false);
        LendingService logic = new LendingService(lending_repository, payment_service);

        boolean result = logic.acceptReturnedProduct(lending);

        Assert.assertFalse(result);
        Assert.assertFalse(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.returned, lending.getStatus());
    }

    // Tests for ownerReceivesSuretyAfterConflict

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
            20L
        );
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_repository, payment_service);

        boolean result = logic.ownerReceivesSuretyAfterConflict(lending);

        Assert.assertTrue(result);
        Assert.assertTrue(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.done, lending.getStatus());
        Assert.assertTrue(payment_service.getLastWasTransfer());
        Assert.assertEquals(20L, (long) payment_service.getLastId());
        Assert.assertEquals(borrower.getUsername(), payment_service.getLastUsername());
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
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, false, true);
        LendingService logic = new LendingService(lending_repository, payment_service);

        boolean result = logic.ownerReceivesSuretyAfterConflict(lending);

        Assert.assertFalse(result);
        Assert.assertFalse(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.conflict, lending.getStatus());
    }

    // Tests for borrowerReceivesSuretyAfterConflict

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
            20L
        );
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_repository, payment_service);

        boolean result = logic.borrowerReceivesSuretyAfterConflict(lending);

        Assert.assertTrue(result);
        Assert.assertTrue(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.done, lending.getStatus());
        Assert.assertFalse(payment_service.getLastWasTransfer());
        Assert.assertEquals(20L, (long) payment_service.getLastId());
        Assert.assertEquals(borrower.getUsername(), payment_service.getLastUsername());
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
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, false);
        LendingService logic = new LendingService(lending_repository, payment_service);

        boolean result = logic.borrowerReceivesSuretyAfterConflict(lending);

        Assert.assertFalse(result);
        Assert.assertFalse(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.conflict, lending.getStatus());
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
