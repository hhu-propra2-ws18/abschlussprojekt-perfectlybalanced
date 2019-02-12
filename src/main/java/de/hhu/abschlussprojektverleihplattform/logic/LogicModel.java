package de.hhu.abschlussprojektverleihplattform.logic;

import de.hhu.abschlussprojektverleihplattform.model.AddressEntity;
import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;

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

    public void AddProduct(String description, String titel, int surety, int cost, AddressEntity location, UserEntity owner) {
        ProductEntity p = new ProductEntity(description, titel, surety, cost, location, owner)
        product_service.addProduct(p);
    }

    //// Operationen:

    // Verfuegbaren Zeitraum pruefen

    // Anfrage einer Buchung eintragen

    // Anfrage einer Buchung beantworten

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
