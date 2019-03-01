package de.hhu.abschlussprojektverleihplattform.service;

import de.hhu.abschlussprojektverleihplattform.model.Timespan;
import de.hhu.abschlussprojektverleihplattform.model.*;
import de.hhu.abschlussprojektverleihplattform.repository.ILendingRepository;
import de.hhu.abschlussprojektverleihplattform.repository.ITransactionRepository;
import de.hhu.abschlussprojektverleihplattform.service.propay.interfaces.IPaymentService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class LendingService implements ILendingService {

    private final ILendingRepository lendingRepository;
    private final IPaymentService paymentService;
    private final ITransactionRepository transactionRepository;

    public LendingService(ILendingRepository lendingRepository,
                          IPaymentService paymentService,
                          ITransactionRepository transactionRepository) {
        this.lendingRepository = lendingRepository;
        this.paymentService = paymentService;
        this.transactionRepository = transactionRepository;
    }

    public List<Timespan> getAvailableTime(ProductEntity product) {
        List<LendingEntity> lendings = lendingRepository.getAllLendingsFromProduct(product);
        List<Timespan> list = new ArrayList<>();
        for (LendingEntity lend : lendings) {
            if (
                lend.getStatus() != Lendingstatus.done
                    && lend.getStatus() != Lendingstatus.denied
            ) {
                Timespan timespan = new Timespan(lend.getStart(), lend.getEnd());
                list.add(timespan);
            }
        }
        return list;
    }

    public LendingEntity requestLending(
            UserEntity actingUser,
            ProductEntity product,
            Timestamp start,
            Timestamp end
    ) throws Exception{
        if(!product.getStatus().equals(Productstatus.forLending)){
            throw new Exception("Dieses Produkt kann nur gekauft, nicht geliehen werden.");
        }
        if(start.equals(end)) {
            throw new Exception("Start und Ende der Ausleihe dürfen nicht identisch sein.");
        }
        if(start.after(end)) {
            throw new Exception(
                "Falls sie einen Bug suchen, hier ist keiner. "
                + "Das Ende muss nach dem Start sein, sie Genie!"
            );
        }
        if(start.before(getThisMorning())) {
            throw new Exception(
                "Sie können die Vergangenheit nicht ändern. "
                + "Sie müssen das Produkt nach dem aktuellen Zeitpunkt ausleihen."
            );
        }
        int totalMoney = product.getSurety()
            + product.getCost() * daysBetweenTwoTimestamps(start, end);
        Long userMoney = paymentService.usersCurrentBalance(actingUser.getUsername());
        if (userMoney < totalMoney) {
            throw new Exception("Die Kosten und die Kaution ergeben zusammen: "
                + totalMoney + "€, aber sie haben nur: " + userMoney + "€ auf ihrem Konto.");
        }
        LendingEntity lending = new LendingEntity(
            Lendingstatus.requested,
            start,
            end,
            actingUser,
            product,
            0L,
            0L
        );
        // TODO: check if 0L really is unused in ProPay
        lendingRepository.addLending(lending);
        return lending;
    }

    public void acceptLendingRequest(LendingEntity lending) throws Exception {
        if (!lending.getStatus().equals(Lendingstatus.requested)) {
            throw new Exception("Die Ausleihe musste den Status: " + Lendingstatus.requested
                    + " haben, aber hat den Status: " + lending.getStatus());
        }
        List<LendingEntity> lendings =
            lendingRepository.getAllLendingsFromProduct(lending.getProduct());
        Timestamp start = lending.getStart();
        Timestamp end = lending.getEnd();
        for (LendingEntity lend : lendings) {
            if(lend.getStatus() == Lendingstatus.requested
                || lend.getStatus() == Lendingstatus.denied) {
                continue;
            }
            Timestamp lend_start = lend.getStart();
            Timestamp lend_end = lend.getEnd();
            if (
                    (start.after(lend_start) && start.before(lend_end))
                            || (end.after(lend_start) && end.before(lend_end))
                            || (lend_start.after(start) && lend_start.before(end))
                            || start.equals(lend_start)
            ) {
                throw new Exception("Das Produkt ist innerhalb des Zeitraums bereits vergeben.");
            }
        }
        int totalMoney = lending.getProduct().getSurety()
                + lending.getProduct().getCost()
                * daysBetweenTwoTimestamps(lending.getStart(), lending.getEnd());
        Long userMoney = paymentService.usersCurrentBalance(lending.getBorrower().getUsername());
        if (userMoney < totalMoney) {
            throw new Exception(
                "Der Leihende hat momentan nicht genügend Geld für den Leihvorgang."
            );
        }
        Long costID = paymentService.reservateAmount(
            lending.getBorrower().getUsername(),
            lending.getProduct().getOwner().getUsername(),
            lending.getProduct().getCost()
                * daysBetweenTwoTimestamps(lending.getStart(), lending.getEnd())
        );
        Long suretyID;
        try {
            suretyID = paymentService.reservateAmount(
                    lending.getBorrower().getUsername(),
                    lending.getProduct().getOwner().getUsername(),
                    lending.getProduct().getSurety()
            );
        } catch (Exception e) {
            paymentService.returnReservatedMoney(lending.getBorrower().getUsername(), costID);
            throw e;
        }
        paymentService.tranferReservatedMoney(lending.getBorrower().getUsername(), costID);
        lending.setStatus(Lendingstatus.confirmt);
        lending.setCostReservationID(costID);
        lending.setSuretyReservationID(suretyID);
        lendingRepository.update(lending);

        TransactionEntity transaction = new TransactionEntity(lending.getBorrower(),
                lending.getProduct().getOwner(),
                lending.getProduct().getCost(),
                lending.getStart());
        transactionRepository.addTransaction(transaction);
    }

    public void denyLendingRequest(LendingEntity lending) throws Exception {
        if (!lending.getStatus().equals(Lendingstatus.requested)) {
            throw new Exception("Die Ausleihe musste den Status: " + Lendingstatus.requested
                    + " haben, aber hat den Status: " + lending.getStatus());
        }
        lending.setStatus(Lendingstatus.denied);
        lendingRepository.update(lending);
    }

    public void returnProduct(LendingEntity lending) throws Exception {
        if (!lending.getStatus().equals(Lendingstatus.confirmt)) {
            throw new Exception("Die Ausleihe musste den Status: " + Lendingstatus.confirmt
                    + " haben, aber hat den Status: " + lending.getStatus());
        }
        lending.setStatus(Lendingstatus.returned);
        lendingRepository.update(lending);
    }

    public void acceptReturnedProduct(LendingEntity lending) throws Exception {
        if (!lending.getStatus().equals(Lendingstatus.returned)) {
            throw new Exception("Die Ausleihe musste den Status: " + Lendingstatus.returned
                    + " haben, aber hat den Status: " + lending.getStatus());
        }
        paymentService.returnReservatedMoney(
            lending.getBorrower().getUsername(),
            lending.getSuretyReservationID()
        );
        lending.setStatus(Lendingstatus.done);
        lendingRepository.update(lending);
    }

    public void denyReturnedProduct(LendingEntity lending) throws Exception {
        if (!lending.getStatus().equals(Lendingstatus.returned)) {
            throw new Exception("Die Ausleihe musste den Status: " + Lendingstatus.returned
                    + " haben, aber hat den Status: " + lending.getStatus());
        }
        lending.setStatus(Lendingstatus.conflict);
        lendingRepository.update(lending);
    }

    public void ownerReceivesSuretyAfterConflict(LendingEntity lending) throws Exception {
        if (!lending.getStatus().equals(Lendingstatus.conflict)) {
            throw new Exception("Die Ausleihe musste den Status: " + Lendingstatus.conflict
                    + " haben, aber hat den Status: " + lending.getStatus());
        }
        paymentService.tranferReservatedMoney(
            lending.getBorrower().getUsername(),
            lending.getSuretyReservationID()
        );
        lending.setStatus(Lendingstatus.done);
        lendingRepository.update(lending);

        TransactionEntity transaction = new TransactionEntity(lending.getBorrower(),
                lending.getProduct().getOwner(),
                lending.getProduct().getSurety(),
                lending.getStart());
        transactionRepository.addTransaction(transaction);
    }

    public void borrowerReceivesSuretyAfterConflict(LendingEntity lending) throws Exception {
        if (!lending.getStatus().equals(Lendingstatus.conflict)) {
            throw new Exception("Die Ausleihe musste den Status: " + Lendingstatus.conflict
                    + " haben, aber hat den Status: " + lending.getStatus());
        }
        paymentService.returnReservatedMoney(
            lending.getBorrower().getUsername(),
            lending.getSuretyReservationID()
        );
        lending.setStatus(Lendingstatus.done);
        lendingRepository.update(lending);
    }
    
    public List<LendingEntity> getAllRequestsForUser(UserEntity user) {
        return lendingRepository.getAllLendingRequestsForProductOwner(user);
    }

    public List<LendingEntity> getAllLendingsFromUser(UserEntity user) {
        return lendingRepository.getAllLendingsFromUser(user);
    }

    public List<LendingEntity> getAllLendingsForUser(UserEntity user) {
        return lendingRepository.getAllLendingsForUser(user);
    }

    public List<LendingEntity> getReturnedLendingFromUser(UserEntity user) {
        return lendingRepository.getReturnedLendingFromUser(user);
    }

    public List<LendingEntity> getAllLendings() {
        return lendingRepository.getAllLendings();
    }

    public List<LendingEntity> getAllConflicts() {
        return lendingRepository.getAllConflicts();
    }

    public LendingEntity getLendingById(Long id) throws Exception {
        return lendingRepository.getLendingById(id);
    }

    private List<LendingEntity> filterByStatus(List<LendingEntity> lendings, Lendingstatus status) {
        return lendings
                .stream()
                .filter(
                        lendingEntity -> lendingEntity
                                .getStatus()
                                .equals(status)
                )
                .collect(Collectors.toList());
    }

    public List<LendingEntity> getAllRequestedLendings(List<LendingEntity> allLendings) {
        return filterByStatus(allLendings, Lendingstatus.requested);
    }

    public List<LendingEntity> getAllConfirmedLendings(List<LendingEntity> allLendings) {
        return filterByStatus(allLendings, Lendingstatus.confirmt);
    }

    public List<LendingEntity> getAllReturnedLendings(List<LendingEntity> allLendings) {
        return filterByStatus(allLendings, Lendingstatus.returned);
    }

    public List<LendingEntity> getAllConflictedLendings(List<LendingEntity> allLendings) {
        return filterByStatus(allLendings, Lendingstatus.conflict);
    }

    public List<LendingEntity> getAllCompletedLendings(List<LendingEntity> allLendings) {
        return allLendings
                .stream()
                .filter(
                    lendingEntity -> lendingEntity.getStatus().equals(Lendingstatus.done)
                            || lendingEntity.getStatus().equals(Lendingstatus.denied)
                )
                .collect(Collectors.toList());
    }

    public List<LendingEntity> getAllReminder(List<LendingEntity> allLendings) {
        return allLendings
            .stream()
            .filter(lendingEntity -> lendingEntity
                .getEnd()
                .before(Timestamp.valueOf(LocalDateTime.now())))
            .filter(lendingEntity -> lendingEntity
                .getStatus()
                .equals(Lendingstatus.confirmt))
            .collect(Collectors.toList());
    }

    int daysBetweenTwoTimestamps(Timestamp start, Timestamp end) {
        long differenceInMillis = end.getTime() - start.getTime();
        double differenceInDays = differenceInMillis / (1000.0 * 60 * 60 * 24);
        return (int) Math.ceil(differenceInDays);
    }

    Timestamp getThisMorning() {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        long millis = now.getTime();
        long millisPerHour = 1000*60*60;
        long millisPerDay = millisPerHour * 24;
        millis /= millisPerDay;
        millis *= millisPerDay;
        return new Timestamp(millis - millisPerHour);
    }
}
