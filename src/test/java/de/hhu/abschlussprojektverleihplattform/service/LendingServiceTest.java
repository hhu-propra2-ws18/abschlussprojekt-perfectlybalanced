package de.hhu.abschlussprojektverleihplattform.service;

import de.hhu.abschlussprojektverleihplattform.TestDummys.LendingServiceDummy;
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
        Timestamp start1 = new Timestamp(1000l);
        Timestamp end1 = new Timestamp(2000l);
        LendingEntity timeBlocker = new LendingEntity(Lendingstatus.confirmt, start1, end1, actingUser, product, 0l, 0l);
        LendingServiceDummy lending_service = new LendingServiceDummy();
        lending_service.addLending(timeBlocker);
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_service, payment_service);
        Timestamp start2 = new Timestamp(1700l);
        Timestamp end2 = new Timestamp(3000l);

        boolean result = logic.RequestLending(actingUser, product, start2, end2);

        Assert.assertEquals(false, result);
    }

    @Test
    public void TimeIsBlocked2() {
        // end is in reservated Time
        UserEntity actingUser = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        Timestamp start1 = new Timestamp(1000l);
        Timestamp end1 = new Timestamp(2000l);
        LendingEntity timeBlocker = new LendingEntity(Lendingstatus.confirmt, start1, end1, actingUser, product, 0l, 0l);
        LendingServiceDummy lending_service = new LendingServiceDummy();
        lending_service.addLending(timeBlocker);
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_service, payment_service);
        Timestamp start2 = new Timestamp(800l);
        Timestamp end2 = new Timestamp(1500l);

        boolean result = logic.RequestLending(actingUser, product, start2, end2);

        Assert.assertEquals(false, result);
    }

    @Test
    public void TimeIsBlocked3() {
        // both are in reservated Time
        UserEntity actingUser = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        Timestamp start1 = new Timestamp(1000l);
        Timestamp end1 = new Timestamp(2000l);
        LendingEntity timeBlocker = new LendingEntity(Lendingstatus.confirmt, start1, end1, actingUser, product, 0l, 0l);
        LendingServiceDummy lending_service = new LendingServiceDummy();
        lending_service.addLending(timeBlocker);
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_service, payment_service);
        Timestamp start2 = new Timestamp(1800l);
        Timestamp end2 = new Timestamp(1900l);

        boolean result = logic.RequestLending(actingUser, product, start2, end2);

        Assert.assertEquals(false, result);
    }

    @Test
    public void TimeIsBlocked4() {
        // reservated Time within requested Time
        UserEntity actingUser = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        Timestamp start1 = new Timestamp(1000l);
        Timestamp end1 = new Timestamp(2000l);
        LendingEntity timeBlocker = new LendingEntity(Lendingstatus.confirmt, start1, end1, actingUser, product, 0l, 0l);
        LendingServiceDummy lending_service = new LendingServiceDummy();
        lending_service.addLending(timeBlocker);
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_service, payment_service);
        Timestamp start2 = new Timestamp(800l);
        Timestamp end2 = new Timestamp(3500l);

        boolean result = logic.RequestLending(actingUser, product, start2, end2);

        Assert.assertEquals(false, result);
    }

    @Test
    public void UserHasNotEnoughMoney() {
        // reservated Time within requested Time
        UserEntity actingUser = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        LendingServiceDummy lending_service = new LendingServiceDummy();
        PaymentServiceDummy payment_service = new PaymentServiceDummy(false, true, true, true);
        LendingService logic = new LendingService(lending_service, payment_service);
        Timestamp start = new Timestamp(800l);
        Timestamp end = new Timestamp(3500l);

        boolean result = logic.RequestLending(actingUser, product, start, end);

        Assert.assertEquals(false, result);
    }

    @Test
    public void ReservationsFail() {
        // reservated Time within requested Time
        UserEntity actingUser = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        LendingServiceDummy lending_service = new LendingServiceDummy();
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, false, true, true);
        LendingService logic = new LendingService(lending_service, payment_service);
        Timestamp start = new Timestamp(800l);
        Timestamp end = new Timestamp(3500l);

        boolean result = logic.RequestLending(actingUser, product, start, end);

        Assert.assertEquals(false, result);
    }

    @Test
    public void ReservationSuccess() {
        // reservated Time within requested Time
        UserEntity actingUser = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        LendingServiceDummy lending_service = new LendingServiceDummy();
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_service, payment_service);
        Timestamp start = new Timestamp(800l);
        Timestamp end = new Timestamp(3500l);

        boolean result = logic.RequestLending(actingUser, product, start, end);

        Assert.assertEquals(true, result);
        LendingEntity created_lending = lending_service.getFirst();
        Assert.assertEquals(Lendingstatus.requested, created_lending.getStatus());
        Assert.assertEquals(true, created_lending.getStart().equals(start));
        Assert.assertEquals(true, created_lending.getEnd().equals(end));
        Assert.assertEquals(actingUser.getUsername(), created_lending.getBorrower().getUsername());
        Assert.assertEquals(product.getTitle(), created_lending.getProduct().getTitle());
        ReservationDummy cost = payment_service.findReservation(created_lending.getCostReservationID());
        ReservationDummy surety = payment_service.findReservation(created_lending.getSuretyReservationID());
        Assert.assertEquals(actingUser.getUsername(), cost.getFrom().getUsername());
        Assert.assertEquals(product.getOwner().getUsername(), cost.getTo().getUsername());
        Assert.assertEquals(product.getCost(), cost.getAmount());
        Assert.assertEquals(PaymentStatus.reservated, cost.getStatus());
        Assert.assertEquals(actingUser.getUsername(), surety.getFrom().getUsername());
        Assert.assertEquals(product.getOwner().getUsername(), surety.getTo().getUsername());
        Assert.assertEquals(product.getSurety(), surety.getAmount());
        Assert.assertEquals(PaymentStatus.reservated, surety.getStatus());
    }

    // Tests for AcceptLending

    @Test
    public void RequestGetsDenied() {
        Timestamp start = new Timestamp(300l);
        Timestamp end = new Timestamp(500l);
        UserEntity borower = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        LendingEntity lending = new LendingEntity(Lendingstatus.requested, start, end, borower, product, 0l, 0l);
        LendingServiceDummy lending_service = new LendingServiceDummy();
        lending_service.setLendingToUpdate(lending);
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_service, payment_service);

        boolean result = logic.AcceptLending(lending, false);

        Assert.assertEquals(true, result);
        Assert.assertEquals(true, lending_service.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.denied, lending.getStatus());
    }

    @Test
    public void PaymentFails() {
        Timestamp start = new Timestamp(300l);
        Timestamp end = new Timestamp(500l);
        UserEntity borower = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        LendingEntity lending = new LendingEntity(Lendingstatus.requested, start, end, borower, product, 0l, 0l);
        LendingServiceDummy lending_service = new LendingServiceDummy();
        lending_service.setLendingToUpdate(lending);
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, false, true);
        LendingService logic = new LendingService(lending_service, payment_service);

        boolean result = logic.AcceptLending(lending, true);

        Assert.assertEquals(false, result);
        Assert.assertEquals(false, lending_service.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.requested, lending.getStatus());
    }

    @Test
    public void RequestGetsAccepted() {
        Timestamp start = new Timestamp(300l);
        Timestamp end = new Timestamp(500l);
        UserEntity borower = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        LendingEntity lending = new LendingEntity(Lendingstatus.requested, start, end, borower, product, 0l, 0l);
        LendingServiceDummy lending_service = new LendingServiceDummy();
        lending_service.setLendingToUpdate(lending);
        PaymentServiceDummy payment_service = new PaymentServiceDummy(true, true, true, true);
        LendingService logic = new LendingService(lending_service, payment_service);

        boolean result = logic.AcceptLending(lending, true);

        Assert.assertEquals(true, result);
        Assert.assertEquals(true, lending_service.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.confirmt, lending.getStatus());
    }

    // Tests for ReturnProduct

    @Test
    public void ProductGetsReturned() {
        Timestamp start = new Timestamp(300l);
        Timestamp end = new Timestamp(500l);
        UserEntity borower = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        LendingEntity lending = new LendingEntity(Lendingstatus.requested, start, end, borower, product, 0l, 0l);
        LendingServiceDummy lending_service = new LendingServiceDummy();
        lending_service.setLendingToUpdate(lending);
        LendingService logic = new LendingService(lending_service, null);

        logic.ReturnProduct(lending);

        Assert.assertEquals(true, lending_service.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.returned, lending.getStatus());
    }

    @Test
    public void ProductGetsReturnedAlternative() {
        Timestamp start = new Timestamp(300l);
        Timestamp end = new Timestamp(500l);
        UserEntity borower = createExampleUser1();
        UserEntity owner = createExampleUser2();
        ProductEntity product = createExampleProduct1(owner);
        LendingEntity lending = new LendingEntity(Lendingstatus.requested, start, end, borower, product, 0l, 0l);
        LendingServiceDummy lending_service = new LendingServiceDummy();
        lending_service.setLendingToUpdate(lending);
        lending_service.setLendingByProductAndUser(lending);
        LendingService logic = new LendingService(lending_service, null);

        logic.ReturnProduct(borower, product);

        Assert.assertEquals(true, lending_service.hasBeenUpdated());
        Assert.assertEquals(Lendingstatus.returned, lending.getStatus());
    }

    // Tests for CheckReturnedProduct


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

    private UserEntity createExampleUser3() {
        String firstname = "Dieter";
        String lastname = "Schulz";
        String username = "VollCoolerName";
        String password = "123456";
        String email = "Frank.Meier@Example.com";
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

    private ProductEntity createExampleProduct3(UserEntity owner) {
        String description = "Ein richtig fetter Kohle-Grill";
        String title = "Grill";
        int surety = 3000;
        int cost = 800;
        String street = "Lilienpfad";
        int housenumber = 6;
        int postcode = 13579;
        String city = "Niederhausen";
        AddressEntity a = new AddressEntity(street, housenumber, postcode, city);
        return new ProductEntity(description, title, surety, cost, a, owner);
    }

}
