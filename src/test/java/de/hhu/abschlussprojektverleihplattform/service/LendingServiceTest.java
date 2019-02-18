package de.hhu.abschlussprojektverleihplattform.service;

import de.hhu.abschlussprojektverleihplattform.TestDummys.LendingRepositoryDummy;
import de.hhu.abschlussprojektverleihplattform.TestDummys.PaymentServiceDummy;
import de.hhu.abschlussprojektverleihplattform.TestDummys.PaymentStatus;
import de.hhu.abschlussprojektverleihplattform.TestDummys.ReservationDummy;
import de.hhu.abschlussprojektverleihplattform.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Timestamp;

public class LendingServiceTest {

    // Tests for RequestLending

    @Test
    public void TimeIsBlocked1() {
        // start is in reservated Time
        UserEntity actingUser = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        Timestamp start1 = new Timestamp(1000L);
        Timestamp end1 = new Timestamp(2000L);
        LendingEntity timeBlocker = new LendingEntity(Lendingstatus.confirmt, start1, end1, actingUser, product, 0L, 0L);
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.addLending(timeBlocker);
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_repository, payment_service);
        Timestamp start2 = new Timestamp(1700L);
        Timestamp end2 = new Timestamp(3000L);

        boolean result = logic.RequestLending(actingUser, product, start2, end2);

        Assert.assertFalse(result);
    }

    @Test
    public void TimeIsBlocked2() {
        // end is in reservated Time
        UserEntity actingUser = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        Timestamp start1 = new Timestamp(1000L);
        Timestamp end1 = new Timestamp(2000L);
        LendingEntity timeBlocker = new LendingEntity(Lendingstatus.confirmt, start1, end1, actingUser, product, 0L, 0L);
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.addLending(timeBlocker);
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_repository, payment_service);
        Timestamp start2 = new Timestamp(800L);
        Timestamp end2 = new Timestamp(1500L);

        boolean result = logic.RequestLending(actingUser, product, start2, end2);

        Assert.assertFalse(result);
    }

    @Test
    public void TimeIsBlocked3() {
        // both are in reservated Time
        UserEntity actingUser = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        Timestamp start1 = new Timestamp(1000L);
        Timestamp end1 = new Timestamp(2000L);
        LendingEntity timeBlocker = new LendingEntity(Lendingstatus.confirmt, start1, end1, actingUser, product, 0L, 0L);
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.addLending(timeBlocker);
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_repository, payment_service);
        Timestamp start2 = new Timestamp(1800L);
        Timestamp end2 = new Timestamp(1900L);

        boolean result = logic.RequestLending(actingUser, product, start2, end2);

        Assert.assertFalse(result);
    }

    @Test
    public void TimeIsBlocked4() {
        // reservated Time within requested Time
        UserEntity actingUser = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        Timestamp start1 = new Timestamp(1000L);
        Timestamp end1 = new Timestamp(2000L);
        LendingEntity timeBlocker = new LendingEntity(Lendingstatus.confirmt, start1, end1, actingUser, product, 0L, 0L);
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.addLending(timeBlocker);
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_repository, payment_service);
        Timestamp start2 = new Timestamp(800L);
        Timestamp end2 = new Timestamp(3500L);

        boolean result = logic.RequestLending(actingUser, product, start2, end2);

        Assert.assertFalse(result);
    }

    @Test
    public void UserHasNotEnoughMoney() {
        // reservated Time within requested Time
        UserEntity actingUser = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        PaymentServiceDummy payment_service = new PaymentServiceDummy(false, true, true, true);
        LendingService logic = new LendingService(lending_repository, payment_service);
        Timestamp start = new Timestamp(800L);
        Timestamp end = new Timestamp(3500L);

        boolean result = logic.RequestLending(actingUser, product, start, end);

        Assert.assertFalse(result);
    }

    @Test
    public void ReservationsFail() {
        // reservated Time within requested Time
        UserEntity actingUser = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, false, true, true);
        LendingService logic = new LendingService(lending_repository, payment_service);
        Timestamp start = new Timestamp(800L);
        Timestamp end = new Timestamp(3500L);

        boolean result = logic.RequestLending(actingUser, product, start, end);

        Assert.assertFalse(result);
    }

    @Test
    public void ReservationSuccess1() {
        // reservated Time within requested Time
        UserEntity actingUser = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_repository, payment_service);
        Timestamp start = new Timestamp(1557543600000L);
        Timestamp end = new Timestamp(1557900000000L);

        boolean result = logic.RequestLending(actingUser, product, start, end);

        Assert.assertTrue(result);
        LendingEntity created_lending = lending_repository.getFirst();
        Assert.assertEquals(Lendingstatus.requested, created_lending.getStatus());
        Assert.assertTrue(created_lending.getStart().equals(start));
        Assert.assertTrue(created_lending.getEnd().equals(end));
        Assert.assertEquals(actingUser.getUsername(), created_lending.getBorrower().getUsername());
        Assert.assertEquals(product.getTitle(), created_lending.getProduct().getTitle());
        ReservationDummy cost = payment_service.findReservation(created_lending.getCostReservationID());
        ReservationDummy surety = payment_service.findReservation(created_lending.getSuretyReservationID());
        Assert.assertEquals(actingUser.getUsername(), cost.getFrom().getUsername());
        Assert.assertEquals(product.getOwner().getUsername(), cost.getTo().getUsername());
        Assert.assertEquals(product.getCost() * 5, cost.getAmount());   //the Timedifference is between 4 and 5 Days
        Assert.assertEquals(PaymentStatus.reservated, cost.getStatus());
        Assert.assertEquals(actingUser.getUsername(), surety.getFrom().getUsername());
        Assert.assertEquals(product.getOwner().getUsername(), surety.getTo().getUsername());
        Assert.assertEquals(product.getSurety(), surety.getAmount());
        Assert.assertEquals(PaymentStatus.reservated, surety.getStatus());
    }

    @Test
    public void ReservationSuccess2() {
        // reservated Time within requested Time
        UserEntity actingUser = createExampleUser2();
        UserEntity owner = createExampleUser1();
        ProductEntity product = createExampleProduct2(owner);
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_repository, payment_service);
        Timestamp start = new Timestamp(1521811800000L);
        Timestamp end = new Timestamp(1522326000000L);

        boolean result = logic.RequestLending(actingUser, product, start, end);

        Assert.assertTrue(result);
        LendingEntity created_lending = lending_repository.getFirst();
        Assert.assertEquals(Lendingstatus.requested, created_lending.getStatus());
        Assert.assertTrue(created_lending.getStart().equals(start));
        Assert.assertTrue(created_lending.getEnd().equals(end));
        Assert.assertEquals(actingUser.getUsername(), created_lending.getBorrower().getUsername());
        Assert.assertEquals(product.getTitle(), created_lending.getProduct().getTitle());
        ReservationDummy cost = payment_service.findReservation(created_lending.getCostReservationID());
        ReservationDummy surety = payment_service.findReservation(created_lending.getSuretyReservationID());
        Assert.assertEquals(actingUser.getUsername(), cost.getFrom().getUsername());
        Assert.assertEquals(product.getOwner().getUsername(), cost.getTo().getUsername());
        Assert.assertEquals(product.getCost() * 6, cost.getAmount());   //the Timedifference is between 5 and 6 Days
        Assert.assertEquals(PaymentStatus.reservated, cost.getStatus());
        Assert.assertEquals(actingUser.getUsername(), surety.getFrom().getUsername());
        Assert.assertEquals(product.getOwner().getUsername(), surety.getTo().getUsername());
        Assert.assertEquals(product.getSurety(), surety.getAmount());
        Assert.assertEquals(PaymentStatus.reservated, surety.getStatus());
    }

    // Tests for AcceptLending

    @Test
    public void RequestGetsDenied() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borower = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        LendingEntity lending = new LendingEntity(Lendingstatus.requested, start, end, borower, product, 0L, 0L);
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_repository, payment_service);

        boolean result = logic.AcceptLending(lending, false);

        Assert.assertTrue(result);
        Assert.assertTrue(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.denied, lending.getStatus());
    }

    @Test
    public void PaymentFails() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borower = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        LendingEntity lending = new LendingEntity(Lendingstatus.requested, start, end, borower, product, 0L, 0L);
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, false, true);
        LendingService logic = new LendingService(lending_repository, payment_service);

        boolean result = logic.AcceptLending(lending, true);

        Assert.assertFalse(result);
        Assert.assertFalse(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.requested, lending.getStatus());
    }

    @Test
    public void RequestGetsAccepted() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borower = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        LendingEntity lending = new LendingEntity(Lendingstatus.requested, start, end, borower, product, 20L, 0L);
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_repository, payment_service);

        boolean result = logic.AcceptLending(lending, true);

        Assert.assertTrue(result);
        Assert.assertTrue(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.confirmt, lending.getStatus());
        Assert.assertTrue(payment_service.getLastWasTransfer());
        Assert.assertEquals(20L, (long) payment_service.getLastId());
        Assert.assertEquals(borower.getUsername(), payment_service.getLastUsername());
    }

    // Tests for ReturnProduct

    @Test
    public void ProductGetsReturned() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borower = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        LendingEntity lending = new LendingEntity(Lendingstatus.requested, start, end, borower, product, 0L, 0L);
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        LendingService logic = new LendingService(lending_repository, null);

        logic.ReturnProduct(lending);

        Assert.assertTrue(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.returned, lending.getStatus());
    }

    @Test
    public void ProductGetsReturnedAlternative() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borower = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        LendingEntity lending = new LendingEntity(Lendingstatus.requested, start, end, borower, product, 0L, 0L);
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        lending_repository.setLendingByProductAndUser(lending);
        LendingService logic = new LendingService(lending_repository, null);

        logic.ReturnProduct(borower, product);

        Assert.assertTrue(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.returned, lending.getStatus());
    }

    // Tests for CheckReturnedProduct

    @Test
    public void ProductGetsReturnedInBadCondition() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borower = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        LendingEntity lending = new LendingEntity(Lendingstatus.returned, start, end, borower, product, 0L, 0L);
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        LendingService logic = new LendingService(lending_repository, null);

        boolean result = logic.CheckReturnedProduct(lending, false);

        Assert.assertTrue(result);
        Assert.assertTrue(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.conflict, lending.getStatus());
    }

    @Test
    public void ProductGetsReturnedInGoodCondition() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borower = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        LendingEntity lending = new LendingEntity(Lendingstatus.returned, start, end, borower, product, 0L, 20L);
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_repository, payment_service);

        boolean result = logic.CheckReturnedProduct(lending, true);

        Assert.assertTrue(result);
        Assert.assertTrue(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.done, lending.getStatus());
        Assert.assertFalse(payment_service.getLastWasTransfer());
        Assert.assertEquals(20L, (long) payment_service.getLastId());
        Assert.assertEquals(borower.getUsername(), payment_service.getLastUsername());
    }

    @Test
    public void ReturnReservationFails() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borower = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        LendingEntity lending = new LendingEntity(Lendingstatus.returned, start, end, borower, product, 0L, 0L);
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, false);
        LendingService logic = new LendingService(lending_repository, payment_service);

        boolean result = logic.CheckReturnedProduct(lending, true);

        Assert.assertFalse(result);
        Assert.assertFalse(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.returned, lending.getStatus());
    }

    @Test
    public void ProductGetsReturnedInBadConditionAlternative() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borower = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        LendingEntity lending = new LendingEntity(Lendingstatus.returned, start, end, borower, product, 0L, 0L);
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        lending_repository.setLendingByProductAndUser(lending);
        LendingService logic = new LendingService(lending_repository, null);

        boolean result = logic.CheckReturnedProduct(owner, product, false);

        Assert.assertTrue(result);
        Assert.assertTrue(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.conflict, lending.getStatus());
    }

    @Test
    public void ProductGetsReturnedInGoodConditionAlternative() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borower = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        LendingEntity lending = new LendingEntity(Lendingstatus.returned, start, end, borower, product, 0L, 20L);
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        lending_repository.setLendingByProductAndUser(lending);
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_repository, payment_service);

        boolean result = logic.CheckReturnedProduct(owner, product, true);

        Assert.assertTrue(result);
        Assert.assertTrue(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.done, lending.getStatus());
        Assert.assertFalse(payment_service.getLastWasTransfer());
        Assert.assertEquals(20L, (long) payment_service.getLastId());
        Assert.assertEquals(borower.getUsername(), payment_service.getLastUsername());
    }

    @Test
    public void ReturnReservationFailsAlternative() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borower = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        LendingEntity lending = new LendingEntity(Lendingstatus.returned, start, end, borower, product, 0L, 0L);
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        lending_repository.setLendingByProductAndUser(lending);
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, false);
        LendingService logic = new LendingService(lending_repository, payment_service);

        boolean result = logic.CheckReturnedProduct(owner, product, true);

        Assert.assertFalse(result);
        Assert.assertFalse(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.returned, lending.getStatus());
    }

    // Tests for ResolveConflict

    @Test
    public void OwnerRecivesSurety() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borower = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        LendingEntity lending = new LendingEntity(Lendingstatus.conflict, start, end, borower, product, 0L, 20L);
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_repository, payment_service);

        boolean result = logic.ResolveConflict(lending, true);

        Assert.assertTrue(result);
        Assert.assertTrue(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.done, lending.getStatus());
        Assert.assertTrue(payment_service.getLastWasTransfer());
        Assert.assertEquals(20L, (long) payment_service.getLastId());
        Assert.assertEquals(borower.getUsername(), payment_service.getLastUsername());
    }

    @Test
    public void BorowerRecivesSurety() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borower = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        LendingEntity lending = new LendingEntity(Lendingstatus.conflict, start, end, borower, product, 0L, 20L);
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_repository, payment_service);

        boolean result = logic.ResolveConflict(lending, false);

        Assert.assertTrue(result);
        Assert.assertTrue(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.done, lending.getStatus());
        Assert.assertFalse(payment_service.getLastWasTransfer());
        Assert.assertEquals(20L, (long) payment_service.getLastId());
        Assert.assertEquals(borower.getUsername(), payment_service.getLastUsername());
    }

    @Test
    public void OwnerRecivesSuretyButTranferFails() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borower = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        LendingEntity lending = new LendingEntity(Lendingstatus.conflict, start, end, borower, product, 0L, 0L);
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, false, true);
        LendingService logic = new LendingService(lending_repository, payment_service);

        boolean result = logic.ResolveConflict(lending, true);

        Assert.assertFalse(result);
        Assert.assertFalse(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.conflict, lending.getStatus());
    }

    @Test
    public void BorowerRecivesSuretyButTranferFails() {
        Timestamp start = new Timestamp(300L);
        Timestamp end = new Timestamp(500L);
        UserEntity borower = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        LendingEntity lending = new LendingEntity(Lendingstatus.conflict, start, end, borower, product, 0L, 0L);
        LendingRepositoryDummy lending_repository = new LendingRepositoryDummy();
        lending_repository.setLendingToUpdate(lending);
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, false);
        LendingService logic = new LendingService(lending_repository, payment_service);

        boolean result = logic.ResolveConflict(lending, false);

        Assert.assertFalse(result);
        Assert.assertFalse(lending_repository.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.conflict, lending.getStatus());
    }

    // Tests for DaysBetween

    @Test
    public void DifferenceIs3() {
        Timestamp first = new Timestamp(1557266400000L);
        Timestamp second = new Timestamp(1557525600000L);
        LendingService logic = new LendingService(null, null);

        int result = logic.DaysBetween(first, second);

        Assert.assertEquals(3, result);
    }

    @Test
    public void DifferenceIs4() {
        Timestamp first = new Timestamp(1557266400000L);
        Timestamp second = new Timestamp(1557525600010L);
        LendingService logic = new LendingService(null, null);

        int result = logic.DaysBetween(first, second);

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
