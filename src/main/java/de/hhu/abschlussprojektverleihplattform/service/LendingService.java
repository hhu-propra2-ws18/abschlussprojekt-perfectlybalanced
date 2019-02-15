package de.hhu.abschlussprojektverleihplattform.service;

import de.hhu.abschlussprojektverleihplattform.service.propay.IPaymentService;
import de.hhu.abschlussprojektverleihplattform.logic.TempZeitraumModel;
import de.hhu.abschlussprojektverleihplattform.model.*;

import java.sql.Timestamp;
import java.util.List;

//NOTE: Die meisten Methoden geben einen boolean-wert zurueck.
//      ist dieser false wurde die Operation nicht(erfolgreich) ausgefuehrt,
//      und auf der Website muss eine entsprechende Fehlermeldung angezeigt werden.

public class LendingService {

    private ILendingService lending_service;
    private IPaymentService payment_service;

    public LendingService(ILendingService lending_service, IPaymentService payment_service) {
        this.lending_service = lending_service;
        this.payment_service = payment_service;
    }

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
            if ((start.after(lend_start) && start.before(lend_end)) || (end.after(lend_start) && end.before(lend_end)) || (lend_start.after(start) && lend_start.before(end))) {
                TimeIsOK = false;
            }
        }
        int totalcost = product.getCost() + product.getSurety();
        boolean MoneyIsOK = payment_service.userHasAmount(actingUser, totalcost);
        if (TimeIsOK && MoneyIsOK) {
            Long costID = payment_service.reservateAmount(actingUser, product.getOwner(), product.getCost());
            Long suretyID = payment_service.reservateAmount(actingUser, product.getOwner(), product.getSurety());
            if (costID > 0 && suretyID > 0) {
                LendingEntity lending = new LendingEntity(Lendingstatus.requested, start, end, actingUser, product, costID, suretyID);
                lending_service.addLending(lending);
                return true;
            } else {
                payment_service.returnReservatedMoney(actingUser.getUsername(), costID);
                payment_service.returnReservatedMoney(actingUser.getUsername(), suretyID);
            }
        }
        return false;
    }

    // Anfrage einer Buchung beantworten
    public boolean AcceptLending(LendingEntity lending, boolean RequestIsAccepted) {
        if(RequestIsAccepted) {
            if(payment_service.tranferReservatedMoney(lending.getBorrower().getUsername(), lending.getCostReservationID())) {
                lending.setStatus(Lendingstatus.confirmt);
                lending_service.update(lending);
                return true;
            }
            return false;
        } else {
            lending.setStatus(Lendingstatus.denied);
            lending_service.update(lending);
            return true;
        }
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
            if(payment_service.returnReservatedMoney(lending.getBorrower().getUsername(), lending.getSuretyReservationID())) {
                lending.setStatus(Lendingstatus.done);
                lending_service.update(lending);
                return true;
            }
            return false;
        } else {
            lending.setStatus(Lendingstatus.conflict);
            lending_service.update(lending);
            return true;
        }
    }

    // Angeben ob ein Artikel in gutem Zustand zurueckgegeben wurde Alternative
    public boolean CheckReturnedProduct(UserEntity actingUser, ProductEntity product, boolean isAcceptable) {
        LendingEntity lending = lending_service.getLendingByProductAndUser(product, actingUser);
        if (isAcceptable) {
            if(payment_service.returnReservatedMoney(lending.getBorrower().getUsername(), lending.getSuretyReservationID())) {
                lending.setStatus(Lendingstatus.done);
                lending_service.update(lending);
                return true;
            }
            return false;
        } else {
            lending.setStatus(Lendingstatus.conflict);
            lending_service.update(lending);
            return true;
        }
    }

    // Konflikt vom Admin loesen
    public boolean ResolveConflict(LendingEntity lending, boolean OwnerRecivesSurety) {
        if (OwnerRecivesSurety) {
            if (!payment_service.tranferReservatedMoney(lending.getBorrower().getUsername(), lending.getSuretyReservationID())) {
                return false;
            }
        } else {
            if (!payment_service.returnReservatedMoney(lending.getBorrower().getUsername(), lending.getSuretyReservationID())) {
                return false;
            }
        }
        lending.setStatus(Lendingstatus.done);
        lending_service.update(lending);
        return true;
    }
}
