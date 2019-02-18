package de.hhu.abschlussprojektverleihplattform.service;

import de.hhu.abschlussprojektverleihplattform.repository.ILendingRepository;
import de.hhu.abschlussprojektverleihplattform.service.propay.IPaymentService;
import de.hhu.abschlussprojektverleihplattform.logic.TempZeitraumModel;
import de.hhu.abschlussprojektverleihplattform.model.*;

import java.sql.Timestamp;
import java.util.List;

//NOTE: Die meisten Methoden geben einen boolean-wert zurueck.
//      ist dieser false wurde die Operation nicht(erfolgreich) ausgefuehrt,
//      und auf der Website muss eine entsprechende Fehlermeldung angezeigt werden.

public class LendingService implements ILendingService {

    private ILendingRepository lending_repository;
    private IPaymentService payment_service;

    public LendingService(ILendingRepository lending_repository, IPaymentService payment_service) {
        this.lending_repository = lending_repository;
        this.payment_service = payment_service;
    }

    // Verfuegbaren Zeitraum pruefen
    public TempZeitraumModel getTime(ProductEntity product) {
        List<LendingEntity> lendings = lending_repository.getAllLendingsFromProduct(product);
        //TODO: Irgendwie in ein Format umwandeln, was die Viwes anzeigen koennen
        return new TempZeitraumModel();
    }

    // Anfrage einer Buchung eintragen
    public boolean requestLending(
        UserEntity actingUser,
        ProductEntity product,
        Timestamp start,
        Timestamp end
    ) {
        List<LendingEntity> lendings = lending_repository.getAllLendingsFromProduct(product);
        boolean timeIsOK = true;
        for (LendingEntity lend : lendings) {
            Timestamp lend_start = lend.getStart();
            Timestamp lend_end = lend.getEnd();
            if (
		(start.after(lend_start) && start.before(lend_end))
		    || (end.after(lend_start) && end.before(lend_end))
		    || (lend_start.after(start) && lend_start.before(end))
	    ) {
                timeIsOK = false;
            }
        }
        int totalCost = product.getCost() * daysBetween(start, end);
        int totalMoney = totalCost + product.getSurety();
        boolean moneyIsOK = payment_service.userHasAmount(actingUser, totalMoney);
        if (timeIsOK && moneyIsOK) {
            Long costID = payment_service.reservateAmount(
                actingUser,
                product.getOwner(),
                totalCost
            );
            Long suretyID = payment_service.reservateAmount(
                actingUser,
                product.getOwner(),
                product.getSurety()
            );
            if (costID > 0 && suretyID > 0) {
                LendingEntity lending 
                    = new LendingEntity(
                    Lendingstatus.requested,
                    start,
                    end,
                    actingUser,
                    product,
                    costID,
                    suretyID
                );
                lending_repository.addLending(lending);
                return true;
            } else {
                payment_service.returnReservatedMoney(actingUser.getUsername(), costID);
                payment_service.returnReservatedMoney(actingUser.getUsername(), suretyID);
            }
        }
        return false;
    }

    // Anfrage einer Buchung beantworten
    public boolean acceptLending(LendingEntity lending, boolean requestIsAccepted) {
        if (requestIsAccepted) {
            if (payment_service.tranferReservatedMoney(
                    lending.getBorrower().getUsername(),
                    lending.getCostReservationID()
                )
            ) {
                lending.setStatus(Lendingstatus.confirmt);
                lending_repository.update(lending);
                return true;
            }
            return false;
        } else {
            lending.setStatus(Lendingstatus.denied);
            lending_repository.update(lending);
            return true;
        }
    }

    // Artikel zurueckgeben
    public void returnProduct(LendingEntity lending) {
        lending.setStatus(Lendingstatus.returned);
        lending_repository.update(lending);
    }

    // Artikel zurueckgeben alternative
    public void returnProduct(UserEntity actingUser, ProductEntity product) {
        LendingEntity lending
        = lending_repository.getLendingByProductAndUser(product, actingUser);
        lending.setStatus(Lendingstatus.returned);
        lending_repository.update(lending);
    }

    // Angeben ob ein Artikel in gutem Zustand zurueckgegeben wurde
    public boolean checkReturnedProduct(LendingEntity lending, boolean isAcceptable) {
        if (isAcceptable) {
            if (payment_service.returnReservatedMoney(
                    lending.getBorrower().getUsername(),
                    lending.getSuretyReservationID()
                )
            ) {
                lending.setStatus(Lendingstatus.done);
                lending_repository.update(lending);
                return true;
            }
            return false;
        } else {
            lending.setStatus(Lendingstatus.conflict);
            lending_repository.update(lending);
            return true;
        }
    }

    // Angeben ob ein Artikel in gutem Zustand zurueckgegeben wurde Alternative
    public boolean checkReturnedProduct(
	UserEntity actingUser,
	ProductEntity product,
	boolean isAcceptable
    ) {
        LendingEntity lending 
	    = lending_service.getLendingByProductAndUser(product, actingUser);
        if (isAcceptable) {
            if (payment_service.returnReservatedMoney(
                    lending.getBorrower().getUsername(),
                    lending.getSuretyReservationID()
                    )
            ) {
                lending.setStatus(Lendingstatus.done);
                lending_repository.update(lending);
                return true;
            }
            return false;
        } else {
            lending.setStatus(Lendingstatus.conflict);
            lending_repository.update(lending);
            return true;
        }
    }

    // Konflikt vom Admin loesen
    public boolean resolveConflict(LendingEntity lending, boolean ownerRecivesSurety) {
        if (ownerRecivesSurety) {
            if (!payment_service.tranferReservatedMoney(
		lending.getBorrower().getUsername(),
		lending.getSuretyReservationID()
		)) {
                return false;
            }
        } else {
            if (!payment_service.returnReservatedMoney(
		lending.getBorrower().getUsername(),
		lending.getSuretyReservationID()
	        )
	    ) {
                return false;
            }
        }
        lending.setStatus(Lendingstatus.done);
        lending_repository.update(lending);
        return true;
    }

    // private Methode die die Differrenz in Tagen zwischen zwei Timestamps
    // berechnet, kann ggf ausgelagert werden
    // kann hier nicht als private makiert werden, da sie sonst
    // nich getestet werden kann
    protected int DaysBetween(Timestamp start, Timestamp end) {
        long differenceInMillis = end.getTime() - start.getTime();
        double differenceInDays = differenceInMillis / (1000.0 * 60 * 60 * 24);
        return (int) Math.ceil(differenceInDays);
    }
}
