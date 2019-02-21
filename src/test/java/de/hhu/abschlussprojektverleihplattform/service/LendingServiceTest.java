package de.hhu.abschlussprojektverleihplattform.service;

import de.hhu.abschlussprojektverleihplattform.testdummys.LendingRepositoryDummy;
import de.hhu.abschlussprojektverleihplattform.testdummys.PaymentServiceDummy;
import de.hhu.abschlussprojektverleihplattform.testdummys.PaymentStatus;
import de.hhu.abschlussprojektverleihplattform.testdummys.ReservationDummy;
import de.hhu.abschlussprojektverleihplattform.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;

public class LendingServiceTest {


    @Before
    public void unsetDevelopFlags() {
        LendingService.UseDummyProPay = false;
        LendingService.ReturnExampleLendings = false;
    }
    // Tests for requestLending

    @Test
    public void timeIsBlocked1() {
        // start is in reservated Time
        UserEntity actingUser = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
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

        boolean result = logic.requestLending(actingUser, product, start2, end2);

        Assert.assertFalse(result);
    }

    @Test
    public void timeIsBlocked2() {
        // end is in reservated Time
        UserEntity actingUser = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
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

        boolean result = logic.requestLending(actingUser, product, start2, end2);

        Assert.assertFalse(result);
    }

    @Test
    public void timeIsBlocked3() {
        // both are in reservated Time
        UserEntity actingUser = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
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

        boolean result = logic.requestLending(actingUser, product, start2, end2);

        Assert.assertFalse(result);
    }

    @Test
    public void timeIsBlocked4() {
        // reservated Time within requested Time
        UserEntity actingUser = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
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

        boolean result = logic.requestLending(actingUser, product, start2, end2);

        Assert.assertFalse(result);
    }

    @Test
    public void userHasNotEnoughMoney() {
        // reservated Time within requested Time
        UserEntity actingUser = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        PaymentServiceDummy payment_service = new PaymentServiceDummy(false, true, true, true);
        LendingService logic = new LendingService(lending_repository, payment_service);
        Timestamp start = new Timestamp(800L);
        Timestamp end = new Timestamp(3500L);

        boolean result = logic.requestLending(actingUser, product, start, end);

        Assert.assertFalse(result);
    }

    @Test
    public void reservationSuccess1() {
        // reservated Time within requested Time
        UserEntity actingUser = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_repository, payment_service);
        Timestamp start = new Timestamp(1557543600000L);
        Timestamp end = new Timestamp(1557900000000L);

        boolean result = logic.requestLending(actingUser, product, start, end);

        Assert.assertTrue(result);
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
    public void reservationSuccess2() {
        // reservated Time within requested Time
        UserEntity actingUser = createExampleUser2();
        UserEntity owner = createExampleUser1();
        ProductEntity product = createExampleProduct2(owner);
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_repository, payment_service);
        Timestamp start = new Timestamp(1521811800000L);
        Timestamp end = new Timestamp(1522326000000L);

        boolean result = logic.requestLending(actingUser, product, start, end);

        Assert.assertTrue(result);
        LendingEntity created_lending = lending_repository.getFirst();
        Assert.assertEquals(Lendingstatus.requested, created_lending.getStatus());
        Assert.assertTrue(created_lending.getStart().equals(start));
        Assert.assertTrue(created_lending.getEnd().equals(end));
        Assert.assertEquals(actingUser.getUsername(), created_lending.getBorrower().getUsername());
        Assert.assertEquals(product.getTitle(), created_lending.getProduct().getTitle());
        Assert.assertEquals(0L, (long) created_lending.getCostReservationID());
        Assert.assertEquals(0L, (long) created_lending.getSuretyReservationID());
    }

    // Tests for decideLendingRequest

    @Test
    public void requestGetsDenied() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borrower = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
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

        boolean result = logic.denyLendingRequest(lending);

        Assert.assertTrue(result);
        Assert.assertTrue(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.denied, lending.getStatus());
    }

    @Test
    public void reservationFails() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borrower = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
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

        boolean result = logic.acceptLendingRequest(lending);

        Assert.assertFalse(result);
        Assert.assertFalse(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.requested, lending.getStatus());
    }

    @Test
    public void paymentFails() {
        Timestamp start = new Timestamp(1521811800000L);
        Timestamp end = new Timestamp(1522326000000L);
        UserEntity borrower = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
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

        boolean result = logic.acceptLendingRequest(lending);

        Assert.assertFalse(result);
        Assert.assertFalse(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.requested, lending.getStatus());
        ReservationDummy cost
                = payment_service.findReservation(1L);
        ReservationDummy surety
                = payment_service.findReservation(2L);
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
    public void requestGetsAccepted1() {
        Timestamp start = new Timestamp(1521811800000L);
        Timestamp end = new Timestamp(1522326000000L);
        UserEntity borrower = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
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

        boolean result = logic.acceptLendingRequest(lending);

        Assert.assertTrue(result);
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
    public void requestGetsAccepted2() {
        Timestamp start = new Timestamp(1546804200000L);
        Timestamp end = new Timestamp(1547148600000L);
        UserEntity borrower = createExampleUser2();
        UserEntity owner = createExampleUser1();
        ProductEntity product = createExampleProduct2(owner);
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

        boolean result = logic.acceptLendingRequest(lending);

        Assert.assertTrue(result);
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

    // Tests for returnProduct

    @Test
    public void productGetsReturned() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borrower = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
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

        logic.returnProduct(lending);

        Assert.assertTrue(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.returned, lending.getStatus());
    }

    // Test for denyRetunedProduct

    @Test
    public void productGetsReturnedInBadCondition() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borrower = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
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

        boolean result = logic.denyRetunedProduct(lending);

        Assert.assertTrue(result);
        Assert.assertTrue(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.conflict, lending.getStatus());
    }

    // Tests for acceptReturnedProduct

    @Test
    public void productGetsReturnedInGoodCondition() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borrower = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
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
    public void returnReservationFails() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borrower = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
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

    // Tests for ownerRecivesSurety

    @Test
    public void ownerRecivesSurety() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borrower = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
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

        boolean result = logic.ownerRecivesSurety(lending);

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
        UserEntity borrower = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
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

        boolean result = logic.ownerRecivesSurety(lending);

        Assert.assertFalse(result);
        Assert.assertFalse(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.conflict, lending.getStatus());
    }

    // Tests for borrowerRecivesSurety

    @Test
    public void borrowerRecivesSurety() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borrower = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
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

        boolean result = logic.borrowerRecivesSurety(lending);

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
        UserEntity borrower = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
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

        boolean result = logic.borrowerRecivesSurety(lending);

        Assert.assertFalse(result);
        Assert.assertFalse(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.conflict, lending.getStatus());
    }

    // Tests for daysBetween

    @Test
    public void differenceIs3() {
        Timestamp first = new Timestamp(1557266400000L);
        Timestamp second = new Timestamp(1557525600000L);
        LendingService logic = new LendingService(null, null);

        int result = logic.daysBetween(first, second);

        Assert.assertEquals(3, result);
    }

    @Test
    public void differenceIs4() {
        Timestamp first = new Timestamp(1557266400000L);
        Timestamp second = new Timestamp(1557525600010L);
        LendingService logic = new LendingService(null, null);

        int result = logic.daysBetween(first, second);

        Assert.assertEquals(4, result);
    }


    // private Methoden um schnell an TestEntities zu kommen

    private UserEntity createExampleUser1() {
        String firstname = "Frank";
        String lastname = "Meier";
        String username = "DerTolleFrank";
        String password = "123456";
        String email = "Frank.Meier@Example.com";
        return new UserEntity(firstname, lastname, username, password, email);
    }

    private UserEntity createExampleUser2() {
        String firstname = "Hans";
        String lastname = "Müller";
        String username = "Hnaswurst";
        String password = "qwertz";
        String email = "D.Schulz@Example.com";
        return new UserEntity(firstname, lastname, username, password, email);
    }

    private ProductEntity createExampleProduct1(UserEntity owner) {
        String description = "Ein toller Rasemäher";
        String title = "Rasemäher";
        int surety = 200;
        int cost = 20;
        String street = "Tulpenweg";
        int housenumber = 33;
        int postcode = 12345;
        String city = "Heidelberg";
        AddressEntity a = new AddressEntity(street, housenumber, postcode, city);
        return new ProductEntity(description, title, surety, cost, a, owner);
    }

    private ProductEntity createExampleProduct2(UserEntity owner) {
        String description = "Eine Heckenschere";
        String title = "Heckenschere";
        int surety = 60;
        int cost = 5;
        String street = "Talstrasse";
        int housenumber = 44;
        int postcode = 67890;
        String city = "Bad Salzbug";
        AddressEntity a = new AddressEntity(street, housenumber, postcode, city);
        return new ProductEntity(description, title, surety, cost, a, owner);
    }
}
