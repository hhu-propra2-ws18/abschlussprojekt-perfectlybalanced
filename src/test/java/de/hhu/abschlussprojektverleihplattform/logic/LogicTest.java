package de.hhu.abschlussprojektverleihplattform.logic;

import de.hhu.abschlussprojektverleihplattform.TestDummys.DummyProductService;
import de.hhu.abschlussprojektverleihplattform.TestDummys.DummyUserService;
import de.hhu.abschlussprojektverleihplattform.model.AddressEntity;
import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class LogicTest {

    private String firstname1 = "Frank";
    private String lastname1 = "Meier";
    private String username1 = "DerTolleFrank";
    private String password1 = "123456";
    private String email1 = "Frank.Meier@Example.com";
    private String description1 = "Ein toller Rasemäher";
    private String title1 = "Rasemäher";
    private int surety1 = 200;
    private int cost1 = 20;
    private String street1 = "Tulpenweg";
    private int housenumber1 = 33;
    private int postcode1 = 12345;
    private String city1 = "Heidelberg";
    private String firstname2 = "Hans";
    private String lastname2 = "Müller";
    private String username2 = "Hanswurst";
    private String password2 = "654321";
    private String email2 = "Hans1993@Example.com";
    private String description2 = "Eine Heckenschere";
    private String title2 = "Heckenschere";
    private int surety2 = 60;
    private int cost2 = 5;
    private String street2 = "Talstrasse";
    private int housenumber2 = 44;
    private int postcode2 = 67890;
    private String city2 = "Bad Salzburg";
    private String firstname3 = "Dieter";
    private String lastname3 = "Schulz";
    private String username3 = "VollCoolerName";
    private String password3 = "qwertz";
    private String email3 = "D.Schulz@Example.com";
    private String description3 = "Ein richtig fetter Kohle-Grill";
    private String title3 = "Grill";
    private int surety3 = 3000;
    private int cost3 = 800;
    private String street3 = "Lilienpfad";
    private int housenumber3 = 6;
    private int postcode3 = 13579;
    private String city3 = "Niederhausen";

    @Test
    public void multipleUsersCanBeAdded() {

        DummyUserService DummyUser = new DummyUserService();
        Logic logic = new Logic(null, null, null, DummyUser);

        logic.AddUser(firstname1, lastname1, username1, password1, email1);
        logic.AddUser(firstname2, lastname2, username2, password2, email2);
        logic.AddUser(firstname3, lastname3, username3, password3, email3);

        ArrayList<UserEntity> users = DummyUser.getUsers();
        UserEntity user1 = users.get(0);
        UserEntity user2 = users.get(1);
        UserEntity user3 = users.get(2);
        Assert.assertEquals(user1.getFirstname(), firstname1);
        Assert.assertEquals(user1.getLastname(), lastname1);
        Assert.assertEquals(user1.getUsername(), username1);
        Assert.assertEquals(user1.getPassword(), password1);
        Assert.assertEquals(user1.getEmail(), email1);
        Assert.assertEquals(user2.getFirstname(), firstname2);
        Assert.assertEquals(user2.getLastname(), lastname2);
        Assert.assertEquals(user2.getUsername(), username2);
        Assert.assertEquals(user2.getPassword(), password2);
        Assert.assertEquals(user2.getEmail(), email2);
        Assert.assertEquals(user3.getFirstname(), firstname3);
        Assert.assertEquals(user3.getLastname(), lastname3);
        Assert.assertEquals(user3.getUsername(), username3);
        Assert.assertEquals(user3.getPassword(), password3);
        Assert.assertEquals(user3.getEmail(), email3);
    }

    @Test
    public void multipleProductsCanBeAdded() {
        AddressEntity location1 = new AddressEntity(street1, housenumber1, postcode1, city1);
        UserEntity user1 = new UserEntity(firstname1, lastname1, username1, password1, email1);
        AddressEntity location2 = new AddressEntity(street2, housenumber2, postcode2, city2);
        UserEntity user2 = new UserEntity(firstname2, lastname2, username2, password2, email2);
        AddressEntity location3 = new AddressEntity(street3, housenumber3, postcode3, city3);
        UserEntity user3 = new UserEntity(firstname3, lastname3, username3, password3, email3);
        DummyProductService DummyProduct = new DummyProductService();
        Logic logic = new Logic(null, null, DummyProduct, null);

        logic.AddProduct(user1, description1, title1, surety1, cost1, street1, housenumber1, postcode1, city1);
        logic.AddProduct(user2, description2, title2, surety2, cost2, street2, housenumber2, postcode2, city2);
        logic.AddProduct(user3, description3, title3, surety3, cost3, street3, housenumber3, postcode3, city3);

        ArrayList<ProductEntity> products = DummyProduct.getProducts();
        ProductEntity product1 = products.get(0);
        ProductEntity product2 = products.get(1);
        ProductEntity product3 = products.get(2);
        Assert.assertEquals(product1.getDescription(), description1);
        Assert.assertEquals(product1.getTitle(), title1);
        Assert.assertEquals(product1.getSurety(), surety1);
        Assert.assertEquals(product1.getCost(), cost1);
        Assert.assertEquals(product1.getOwner().getFirstname(), firstname1);
        Assert.assertEquals(product1.getOwner().getLastname(), lastname1);
        Assert.assertEquals(product1.getOwner().getUsername(), username1);
        Assert.assertEquals(product1.getOwner().getPassword(), password1);
        Assert.assertEquals(product1.getOwner().getEmail(), email1);
        Assert.assertEquals(product1.getLocation().getStreet(), street1);
        Assert.assertEquals(product1.getLocation().getHousenumber(), housenumber1);
        Assert.assertEquals(product1.getLocation().getPostcode(), postcode1);
        Assert.assertEquals(product1.getLocation().getCity(), city1);
        Assert.assertEquals(product2.getDescription(), description2);
        Assert.assertEquals(product2.getTitle(), title2);
        Assert.assertEquals(product2.getSurety(), surety2);
        Assert.assertEquals(product2.getCost(), cost2);
        Assert.assertEquals(product2.getOwner().getFirstname(), firstname2);
        Assert.assertEquals(product2.getOwner().getLastname(), lastname2);
        Assert.assertEquals(product2.getOwner().getUsername(), username2);
        Assert.assertEquals(product2.getOwner().getPassword(), password2);
        Assert.assertEquals(product2.getOwner().getEmail(), email2);
        Assert.assertEquals(product2.getLocation().getStreet(), street2);
        Assert.assertEquals(product2.getLocation().getHousenumber(), housenumber2);
        Assert.assertEquals(product2.getLocation().getPostcode(), postcode2);
        Assert.assertEquals(product2.getLocation().getCity(), city2);
        Assert.assertEquals(product3.getDescription(), description3);
        Assert.assertEquals(product3.getTitle(), title3);
        Assert.assertEquals(product3.getSurety(), surety3);
        Assert.assertEquals(product3.getCost(), cost3);
        Assert.assertEquals(product3.getOwner().getFirstname(), firstname3);
        Assert.assertEquals(product3.getOwner().getLastname(), lastname3);
        Assert.assertEquals(product3.getOwner().getUsername(), username3);
        Assert.assertEquals(product3.getOwner().getPassword(), password3);
        Assert.assertEquals(product3.getOwner().getEmail(), email3);
        Assert.assertEquals(product3.getLocation().getStreet(), street3);
        Assert.assertEquals(product3.getLocation().getHousenumber(), housenumber3);
        Assert.assertEquals(product3.getLocation().getPostcode(), postcode3);
        Assert.assertEquals(product3.getLocation().getCity(), city3);
    }
}
