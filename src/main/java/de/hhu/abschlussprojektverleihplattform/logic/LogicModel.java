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
        if(ok) {
            LendingEntity lending = new LendingEntity(Lendingstatus.requested, start, end, actingUser, product);
            lending_service.addLending(lending);
            return true;
        } else {
            return false;
        }
    }

    // Anfrage einer Buchung beantworten
    public void AcceptLending(UserEntity actingUser, LendingEntity lending) {
        lending.setLendingstatus(Lendingstatus.confirmt);
        lending_service.Update(lending);
    }

    // Artikel zurueckgeben

    // Angeben ob ein Artikel in gutem Zustand zurueckgegeben wurde

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
