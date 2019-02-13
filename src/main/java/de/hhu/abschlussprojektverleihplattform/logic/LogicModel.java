package de.hhu.abschlussprojektverleihplattform.logic;

import de.hhu.abschlussprojektverleihplattform.model.*;

import java.sql.Timestamp;
import java.util.List;

public class LogicModel {

    private Address_Service address_service;
    private Admin_Service admin_service;
    private Lending_Service lending_service;
    private Payment_Service payment_service;
    private Product_Service product_service;
    private User_Service user_service;

    public void AddUser(String firstname, String lastname, String username, String password, String email) {
        UserEntity u = new UserEntity(firstname, lastname, username, password, email);
        user_service.addUser(u);
    }

    public void AddProduct(UserEntity actingUser, String description, String titel, int surety, int cost, String street, int housenumber, int postcode, String city) {
        AddressEntity location = new AddressEntity(street, housenumber, postcode, city);
        ProductEntity p = new ProductEntity(description, titel, surety, cost, location, actingUser);
        product_service.addProduct(p);
    }

    //// Operationen:

    // Verfuegbaren Zeitraum pruefen
    public TempZeitraumModel getTime(ProductEntity product) {
        List<LendingEntity> lendings = lending_service.getAllLendingsFromProduct(product);
        // Irgendwie in ein Format umwandeln, was die Viwes anzeigen koennen
        return  new TempZeitraumModel();
    }

    // Anfrage einer Buchung eintragen
    public boolean RequestLending(UserEntity actingUser, ProductEntity product, Timestamp start, Timestamp end) {
        List<LendingEntity> lendings = lending_service.getAllLendingsFromProduct(product);
        boolean ok = false; // Produkt ist im anegegeben Zeitraum verfuegbar
        int totalcost = product.getCost() + product.getSurety();
        boolean ok2 = payment_service.UserHasAmount(actingUser, totalcost);
        if(ok && ok2) {
            LendingEntity lending = new LendingEntity(Lendingstatus.requested, start, end, actingUser, product);
            lending_service.addLending(lending);
            boolean ok3 =payment_service.reservateAmount(actingUser, totalcost);
            if(!ok3) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    // Anfrage einer Buchung beantworten
    public void AcceptLending(UserEntity actingUser, LendingEntity lending) {
        lending.setStatus(Lendingstatus.confirmt);
        lending_service.update(lending);
        UserEntity productowner = lending.getProduct().getOwner();
        int cost = lending.getProduct().getCost();
        payment_service.tranferReservatedMoney(actingUser, productowner, cost);
    }

    // Artikel zurueckgeben
    public void ReturnProduct(LendingEntity lending) {
        lending.setStatus(Lendingstatus.returned);
        lending_service.update(lending);
    }

    // Artikel zurueckgeben alternative
    public void ReturnProduct(UserEntity actingUser, ProductEntity product) {
        LendingEntity lending = lending_service.getLendingByProductAndUser(product, actingUser);
        lending.setStatus(Lendingstatus.returned);
        lending_service.update(lending);
    }

    // Angeben ob ein Artikel in gutem Zustand zurueckgegeben wurde
    public void CheckReturnedProduct(UserEntity actingUser, LendingEntity lending, boolean isAcceptable) {
        if(isAcceptable) {
            lending.setStatus(Lendingstatus.done);
            lending_service.update(lending);
            UserEntity customer = lending.getBorrower();
            int surety = lending.getProduct().getSurety();
            payment_service.returnReservatedMoney(customer, surety);
        } else {
            
        }
    }

    // Konflikt vom Admin loesen


    //// Abfragen:

    // Alle Produkte

    // Alle eingehenden Anfragen

    // Alle geliehenen Produkte

    // Alle verliehenden Produkte

    // Alle zurueckgegebene Pridukte

    // Alle Konflikte

    // Detais zu Produkte/Abfragen/Konflikten/...
}
