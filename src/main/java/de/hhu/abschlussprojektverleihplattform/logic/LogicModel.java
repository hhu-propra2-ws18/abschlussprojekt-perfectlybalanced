package de.hhu.abschlussprojektverleihplattform.logic;

import de.hhu.abschlussprojektverleihplattform.model.*;

import java.sql.Timestamp;
import java.util.List;

public class LogicModel {

    private IAddress IAddress_;
    private IAdmin IAdmin_;
    private ILending ILending_;
    private IPayment IPayment_;
    private IProduct IProduct_;
    private IUser IUser_;

    public void AddUser(String firstname, String lastname, String username, String password, String email) {
        UserEntity u = new UserEntity(firstname, lastname, username, password, email);
        IUser_.addUser(u);
    }

    public void AddProduct(UserEntity actingUser, String description, String titel, int surety, int cost, String street, int housenumber, int postcode, String city) {
        AddressEntity location = new AddressEntity(street, housenumber, postcode, city);
        ProductEntity p = new ProductEntity(description, titel, surety, cost, location, actingUser);
        IProduct_.addProduct(p);
    }

    //// Operationen:

    // Verfuegbaren Zeitraum pruefen
    public TempZeitraumModel getTime(ProductEntity product) {
        List<LendingEntity> lendings = ILending_.getAllLendingsFromProduct(product);
        // Irgendwie in ein Format umwandeln, was die Viwes anzeigen koennen
        return  new TempZeitraumModel();
    }

    // Anfrage einer Buchung eintragen
    public boolean RequestLending(UserEntity actingUser, ProductEntity product, Timestamp start, Timestamp end) {
        List<LendingEntity> lendings = ILending_.getAllLendingsFromProduct(product);
        boolean ok = false; // Produkt ist im anegegeben Zeitraum verfuegbar
        int totalcost = product.getCost() + product.getSurety();
        boolean ok2 = IPayment_.UserHasAmount(actingUser, totalcost);
        if(ok && ok2) {
            LendingEntity lending = new LendingEntity(Lendingstatus.requested, start, end, actingUser, product);
            ILending_.addLending(lending);
            boolean ok3 = IPayment_.reservateAmount(actingUser, totalcost);
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
        ILending_.update(lending);
        UserEntity productowner = lending.getProduct().getOwner();
        int cost = lending.getProduct().getCost();
        IPayment_.tranferReservatedMoney(actingUser, productowner, cost);
    }

    // Artikel zurueckgeben
    public void ReturnProduct(LendingEntity lending) {
        lending.setStatus(Lendingstatus.returned);
        ILending_.update(lending);
    }

    // Artikel zurueckgeben alternative
    public void ReturnProduct(UserEntity actingUser, ProductEntity product) {
        LendingEntity lending = ILending_.getLendingByProductAndUser(product, actingUser);
        lending.setStatus(Lendingstatus.returned);
        ILending_.update(lending);
    }

    // Angeben ob ein Artikel in gutem Zustand zurueckgegeben wurde
    public void CheckReturnedProduct(LendingEntity lending, boolean isAcceptable) {
        if(isAcceptable) {
            lending.setStatus(Lendingstatus.done);
            ILending_.update(lending);
            UserEntity customer = lending.getBorrower();
            int surety = lending.getProduct().getSurety();
            IPayment_.returnReservatedMoney(customer, surety);
        } else {
            lending.setStatus(Lendingstatus.conflict);
        }
    }

    // Angeben ob ein Artikel in gutem Zustand zurueckgegeben wurde Alternative
    public void CheckReturnedProduct(UserEntity actingUser, ProductEntity product, boolean isAcceptable) {
        LendingEntity lending = ILending_.getLendingByProductAndUser(product, actingUser);
        if(isAcceptable) {
            lending.setStatus(Lendingstatus.done);
            ILending_.update(lending);
            UserEntity customer = lending.getBorrower();
            int surety = lending.getProduct().getSurety();
            IPayment_.returnReservatedMoney(customer, surety);
        } else {
            lending.setStatus(Lendingstatus.conflict);
        }
    }

    // Konflikt vom Admin loesen
    public void ResolveConflict(LendingEntity lending, boolean OwnerRecivesSurety) {
        if(OwnerRecivesSurety) {
            UserEntity customer = lending.getBorrower();
            UserEntity owner = lending.getProduct().getOwner();
            int surety = lending.getProduct().getSurety();
            IPayment_.tranferReservatedMoney(customer, owner, surety);
        } else {
            UserEntity customer = lending.getBorrower();
            int surety = lending.getProduct().getSurety();
            IPayment_.reservateAmount(customer, surety);
        }
    }

    //// Abfragen:

    // Alle Produkte
    public List<ProductEntity> GetAllProducts() {
        return IProduct_.getAllProducts();
    }

    // Alle eingehenden Anfragen
    public List<LendingEntity> GetRequestForUser(UserEntity actingUser) {
        return ILending_.getAllRequestsForUser(actingUser);
    }

    // Alle geliehenen Produkte
    public List<LendingEntity> GetLendingForUser(UserEntity actingUser) {
        return ILending_.getAllLendingsForUser(actingUser);
    }

    // Alle verliehenden Produkte
    public List<LendingEntity> GetLedingsFromUser(UserEntity actingUser) {
        return ILending_.getAllLendingsFromUser(actingUser);
    }

    // Alle zurueckgegebene Produkte
    public List<LendingEntity> GetReturnedLendings(UserEntity activeUser) {
        return ILending_.getReturnedLendingFromUser(activeUser);
    }

    // Alle Konflikte
    public List<LendingEntity> GetAllConflicts() {
        return ILending_.getAllConflicts();
    }

    // Detais zu Produkte/Abfragen/Konflikten/...
}
