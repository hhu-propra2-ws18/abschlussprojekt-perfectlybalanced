package de.hhu.abschlussprojektverleihplattform.logic;

import de.hhu.abschlussprojektverleihplattform.model.*;

import java.sql.Timestamp;
import java.util.List;

//NOTE: Die meisten Methoden geben einen boolean-wert zurueck.
//      ist dieser false wurde die Operation nicht(erfolgreich) ausgefuehrt,
//      und auf der Website muss eine entsprechende Fehlermeldung angezeigt werden.

public class Logic {

    private ILending lending_service;
    private IPayment payment_service;
    private IProduct product_service;
    private IUser user_service;

    // werden momentan nicht gebraucht
    //private IAddress address_service;
    //private IAdmin admin_service;

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
        //TODO: Irgendwie in ein Format umwandeln, was die Viwes anzeigen koennen
        return new TempZeitraumModel();
    }

    // Anfrage einer Buchung eintragen
    public boolean RequestLending(UserEntity actingUser, ProductEntity product, Timestamp start, Timestamp end) {
        List<LendingEntity> lendings = lending_service.getAllLendingsFromProduct(product);
        boolean TimeIsOK = true;
        for (LendingEntity lend : lendings) {
            Timestamp lend_start = lend.getStart();
            Timestamp lend_end = lend.getEnd();
            if ((start.after(lend_start) && start.before(lend_end)) || (end.after(lend_start) && end.before(lend_end))) {
                TimeIsOK = false;
            }
        }
        int totalcost = product.getCost() + product.getSurety();
        boolean MoneyIsOK = payment_service.UserHasAmount(actingUser, totalcost);
        if (TimeIsOK && MoneyIsOK) {
            LendingEntity lending = new LendingEntity(Lendingstatus.requested, start, end, actingUser, product);
            lending_service.addLending(lending);
            if (payment_service.reservateAmount(actingUser, totalcost)) {
                return true;
            }
        }
        return false;
    }

    // Anfrage einer Buchung beantworten
    public boolean AcceptLending(UserEntity actingUser, LendingEntity lending) {
        lending.setStatus(Lendingstatus.confirmt);
        lending_service.update(lending);
        UserEntity productowner = lending.getProduct().getOwner();
        int cost = lending.getProduct().getCost();
        return payment_service.tranferReservatedMoney(actingUser, productowner, cost);
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
    public boolean CheckReturnedProduct(LendingEntity lending, boolean isAcceptable) {
        if (isAcceptable) {
            lending.setStatus(Lendingstatus.done);
            lending_service.update(lending);
            UserEntity customer = lending.getBorrower();
            int surety = lending.getProduct().getSurety();
            return payment_service.returnReservatedMoney(customer, surety);
        } else {
            lending.setStatus(Lendingstatus.conflict);
            return true;
        }
    }

    // Angeben ob ein Artikel in gutem Zustand zurueckgegeben wurde Alternative
    public boolean CheckReturnedProduct(UserEntity actingUser, ProductEntity product, boolean isAcceptable) {
        LendingEntity lending = lending_service.getLendingByProductAndUser(product, actingUser);
        if (isAcceptable) {
            lending.setStatus(Lendingstatus.done);
            lending_service.update(lending);
            UserEntity customer = lending.getBorrower();
            int surety = lending.getProduct().getSurety();
            return payment_service.returnReservatedMoney(customer, surety);
        } else {
            lending.setStatus(Lendingstatus.conflict);
            return true;
        }
    }

    // Konflikt vom Admin loesen
    public boolean ResolveConflict(LendingEntity lending, boolean OwnerRecivesSurety) {
        if (OwnerRecivesSurety) {
            UserEntity customer = lending.getBorrower();
            UserEntity owner = lending.getProduct().getOwner();
            int surety = lending.getProduct().getSurety();
            if(!payment_service.tranferReservatedMoney(customer, owner, surety)) {
                return false;
            }
        } else {
            UserEntity customer = lending.getBorrower();
            int surety = lending.getProduct().getSurety();
            if(!payment_service.reservateAmount(customer, surety)) {
                return false;
            }
        }
        lending.setStatus(Lendingstatus.done);
        lending_service.update(lending);
        return true;
    }

    //// Abfragen:

    // Alle Produkte
    public List<ProductEntity> GetAllProducts() {
        return product_service.getAllProducts();
    }

    // Alle eingehenden Anfragen
    public List<LendingEntity> GetRequestForUser(UserEntity actingUser) {
        return lending_service.getAllRequestsForUser(actingUser);
    }

    // Alle geliehenen Produkte
    public List<LendingEntity> GetLendingForUser(UserEntity actingUser) {
        return lending_service.getAllLendingsForUser(actingUser);
    }

    // Alle verliehenden Produkte
    public List<LendingEntity> GetLedingsFromUser(UserEntity actingUser) {
        return lending_service.getAllLendingsFromUser(actingUser);
    }

    // Alle zurueckgegebene Produkte
    public List<LendingEntity> GetReturnedLendings(UserEntity activeUser) {
        return lending_service.getReturnedLendingFromUser(activeUser);
    }

    // Alle Konflikte
    public List<LendingEntity> GetAllConflicts() {
        return lending_service.getAllConflicts();
    }

}
