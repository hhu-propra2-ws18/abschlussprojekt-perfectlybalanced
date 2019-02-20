package de.hhu.abschlussprojektverleihplattform.service;

import de.hhu.abschlussprojektverleihplattform.repository.ILendingRepository;
import de.hhu.abschlussprojektverleihplattform.service.propay.IPaymentService;
import de.hhu.abschlussprojektverleihplattform.logic.TempZeitraumModel;
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

    //For the development of the Controllers/Views
    private static boolean ReturnExampleLendings = true;

    private ILendingRepository lending_repository;
    private IPaymentService payment_service;

    public LendingService(ILendingRepository lending_repository, IPaymentService payment_service) {
        this.lending_repository = lending_repository;
        this.payment_service = payment_service;
    }

    // Verfuegbaren Zeitraum pruefen
    public TempZeitraumModel getTime(ProductEntity product) {
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
                = lending_repository.getLendingByProductAndUser(product, actingUser);
        if (isAcceptable) {
            if (
                    payment_service.returnReservatedMoney(
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

    // Methoden um die Daten fuer die Views anzuzeigen

    // return all Lendings, that are owned by the user and have the status requested
    public List<LendingEntity> getAllRequestsForUser(UserEntity user) {
        if (ReturnExampleLendings) {
            List<LendingEntity> list = new ArrayList<LendingEntity>();
            UserEntity borrower = createExampleUser1();
            list.add(createExampleLending1(Lendingstatus.requested, user, borrower));
            return list;
        }
        return lending_repository.getAllRequestsForUser(user);
    }

    // return all Lendings, that are owned by the user
    public List<LendingEntity> getAllLendingsFromUser(UserEntity user) {
        if (ReturnExampleLendings) {
            List<LendingEntity> list = new ArrayList<LendingEntity>();
            UserEntity borrower = createExampleUser1();
            list.add(createExampleLending1(Lendingstatus.confirmt, user, borrower));
            return list;
        }
        return lending_repository.getAllLendingsFromUser(user);
    }

    // return all Lendings, that are borrowed by the user
    public List<LendingEntity> getAllLendingsForUser(UserEntity user) {
        if (ReturnExampleLendings) {
            List<LendingEntity> list = new ArrayList<LendingEntity>();
            UserEntity owner = createExampleUser1();
            list.add(createExampleLending1(Lendingstatus.confirmt, owner, user));
            return list;
        }
        return lending_repository.getAllLendingsForUser(user);
    }

    // return all Lendings, that are owned by the user and have the status returned
    public List<LendingEntity> getReturnedLendingFromUser(UserEntity user) {
        if (ReturnExampleLendings) {
            List<LendingEntity> list = new ArrayList<LendingEntity>();
            UserEntity borrower = createExampleUser1();
            list.add(createExampleLending1(Lendingstatus.returned, user, borrower));
            return list;
        }
        return lending_repository.getReturnedLendingFromUser(user);
    }

    // return all Lendings, that have the status conflict
    public List<LendingEntity> getAllConflicts() {
        if (ReturnExampleLendings) {
            List<LendingEntity> list = new ArrayList<LendingEntity>();
            UserEntity owner = createExampleUser1();
            UserEntity borrower = createExampleUser2();
            list.add(createExampleLending1(Lendingstatus.conflict, owner, borrower));
            return list;
        }
        return lending_repository.getAllConflicts();
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
