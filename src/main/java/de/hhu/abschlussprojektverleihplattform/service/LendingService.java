package de.hhu.abschlussprojektverleihplattform.service;

import de.hhu.abschlussprojektverleihplattform.logic.Timespan;
import de.hhu.abschlussprojektverleihplattform.repository.ILendingRepository;
import de.hhu.abschlussprojektverleihplattform.service.propay.IPaymentService;
import de.hhu.abschlussprojektverleihplattform.model.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

//NOTE: Die meisten Methoden geben einen boolean-wert zurueck.
//      ist dieser false wurde die Operation nicht(erfolgreich) ausgefuehrt,
//      und auf der Website muss eine entsprechende Fehlermeldung angezeigt werden.

@Service
public class LendingService implements ILendingService {

    private ILendingRepository lending_repository;
    private IPaymentService payment_service;

    public LendingService(ILendingRepository lending_repository, IPaymentService payment_service) {
        this.lending_repository = lending_repository;
        this.payment_service = payment_service;
    }

    // Verfuegbaren Zeitraum pruefen
    public List<Timespan> getTime(ProductEntity product) {
        List<LendingEntity> lendings = lending_repository.getAllLendingsFromProduct(product);
        List<Timespan> list = new ArrayList<Timespan>();
        for (LendingEntity lend : lendings) {
            if (lend.getStatus() != Lendingstatus.done && lend.getStatus() != Lendingstatus.denied){
                Timespan timespan = new Timespan(lend.getStart(), lend.getEnd());
                list.add(timespan);
            }
        }
        return list;
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
        int totalMoney = product.getSurety() + product.getCost() * daysBetween(start, end);
        boolean moneyIsOK = payment_service.userHasAmount(actingUser, totalMoney);
        if (timeIsOK && moneyIsOK) {
            LendingEntity lending = new LendingEntity(
                Lendingstatus.requested,
                start,
                end,
                actingUser,
                product,
                0L,
                0L
            );
            // TODO: check if 0L realy is unused in ProPay
            lending_repository.addLending(lending);
            return true;
        }
        return false;
    }

    // Anfrage einer Buchung beantworten
    public boolean acceptLendingRequest(LendingEntity lending) {
        Long costID = payment_service.reservateAmount(
            lending.getBorrower(),
            lending.getProduct().getOwner(),
            lending.getProduct().getCost()
                * daysBetween(lending.getStart(), lending.getEnd())
        );
        Long suretyID = payment_service.reservateAmount(
            lending.getBorrower(),
            lending.getProduct().getOwner(),
            lending.getProduct().getSurety()
        );
        if (costID > 0 && suretyID > 0) {
            if (payment_service.tranferReservatedMoney(
                lending.getBorrower().getUsername(),
                costID
            )
            ) {
                lending.setStatus(Lendingstatus.confirmt);
                lending.setCostReservationID(costID);
                lending.setSuretyReservationID(suretyID);
                lending_repository.update(lending);
                return true;
            }
        }
        payment_service.returnReservatedMoney(lending.getBorrower().getUsername(), costID);
        payment_service.returnReservatedMoney(lending.getBorrower().getUsername(), suretyID);
        return false;
    }

    // Anfrage einer Buchung beantworten
    public void denyLendingRequest(LendingEntity lending) {
        lending.setStatus(Lendingstatus.denied);
        lending_repository.update(lending);
    }

    // Artikel zurueckgeben
    public void returnProduct(LendingEntity lending) {
        lending.setStatus(Lendingstatus.returned);
        lending_repository.update(lending);
    }

    // Angeben dass ein Artikel in gutem Zustand zurueckgegeben wurde
    public boolean acceptReturnedProduct(LendingEntity lending) {
        lending.setStatus(Lendingstatus.done);
        lending_repository.update(lending);
        return true;
    }

    // Angeben dass ein Artikel in schlechtem Zustand zurueckgegeben wurde
    public void denyRetunedProduct(LendingEntity lending) {
        lending.setStatus(Lendingstatus.conflict);
        lending_repository.update(lending);
    }

    // Konflikt vom Admin loesen
    public boolean ownerRecivesSurety(LendingEntity lending) {
        if (payment_service.tranferReservatedMoney(
            lending.getBorrower().getUsername(),
            lending.getSuretyReservationID()
        )) {
            lending.setStatus(Lendingstatus.done);
            lending_repository.update(lending);
            return true;
        }
        return false;
    }

    // Konflikt vom Admin loesen
    public boolean borrowerRecivesSurety(LendingEntity lending) {
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
    }

    // Methoden um die Daten fuer die Views anzuzeigen

    // return all Lendings, that are owned by the user and have the status requested
    public List<LendingEntity> getAllRequestsForUser(UserEntity user) {
        return lending_repository.getAllLendingRequestsForProductOwner(user);
    }

    // return all Lendings, that are owned by the user
    public List<LendingEntity> getAllLendingsFromUser(UserEntity user) {
        return lending_repository.getAllLendingsFromUser(user);
    }

    // return all Lendings, that are borrowed by the user
    public List<LendingEntity> getAllLendingsForUser(UserEntity user) {
        return lending_repository.getAllLendingsForUser(user);
    }

    // return all Lendings, that are owned by the user and have the status returned
    public List<LendingEntity> getReturnedLendingFromUser(UserEntity user) {
        return lending_repository.getReturnedLendingFromUser(user);
    }

    public List<LendingEntity> getAllLendings() {
        return lending_repository.getAllLendings();
    }

    // return all Lendings, that have the status conflict
    public List<LendingEntity> getAllConflicts() {
        return lending_repository.getAllConflicts();
    }

    public LendingEntity getLendingById(Long id) {
        return lending_repository.getLendingById(id);
    }

    public List<LendingEntity> getAllRequestedLendings(List<LendingEntity> allLendings) {
        List<LendingEntity> sortedList = new ArrayList<>();
        for (LendingEntity lend : allLendings) {
            if (lend.getStatus().equals(Lendingstatus.requested)) {
                sortedList.add(lend);
            }
        }
        return sortedList;
    }

    public List<LendingEntity> getAllConfirmedLendings(List<LendingEntity> allLendings) {
        List<LendingEntity> sortedList = new ArrayList<>();
        for (LendingEntity lend : allLendings) {
            if (lend.getStatus().equals(Lendingstatus.confirmt)) {
                sortedList.add(lend);
            }
        }
        return sortedList;
    }

    public List<LendingEntity> getAllReturnedLendings(List<LendingEntity> allLendings) {
        List<LendingEntity> sortedList = new ArrayList<>();
        for (LendingEntity lend : allLendings) {
            if (lend.getStatus().equals(Lendingstatus.returned)) {
                sortedList.add(lend);
            }
        }
        return sortedList;
    }

    public List<LendingEntity> getAllConflictedLendings(List<LendingEntity> allLendings) {
        List<LendingEntity> sortedList = new ArrayList<>();
        for (LendingEntity lend : allLendings) {
            if (lend.getStatus().equals(Lendingstatus.conflict)) {
                sortedList.add(lend);
            }
        }
        return sortedList;
    }

    public List<LendingEntity> getAllCompletedLendings(List<LendingEntity> allLendings) {
        List<LendingEntity> sortedList = new ArrayList<>();
        for (LendingEntity lend : allLendings) {
            if (lend.getStatus().equals(Lendingstatus.done)
                    || lend.getStatus().equals(Lendingstatus.denied)) {
                sortedList.add(lend);
            }
        }
        return sortedList;
    }

    // private Methode die die Differrenz in Tagen zwischen zwei Timestamps
    // berechnet, kann ggf ausgelagert werden
    // kann hier nicht als private makiert werden, da sie sonst
    // nich getestet werden kann
    protected int daysBetween(Timestamp start, Timestamp end) {
        long differenceInMillis = end.getTime() - start.getTime();
        double differenceInDays = differenceInMillis / (1000.0 * 60 * 60 * 24);
        return (int) Math.ceil(differenceInDays);
    }

    // private Methodes for the Develop-Mode

    private LendingEntity createExampleLending1(
            Lendingstatus status,
            UserEntity owner, UserEntity borrower
    ) {
        Timestamp start = new Timestamp(1549368000000L); //3d+2h difference
        Timestamp end = new Timestamp(1549634400000L);
        ProductEntity product = createExampleProduct1(owner);
        Long costReervationID = 1L;
        Long suretyReservationID = 2L;
        return new LendingEntity(
            status,
            start,
            end,
            borrower,
            product,
            costReervationID,
            suretyReservationID
        );
    }

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
}
