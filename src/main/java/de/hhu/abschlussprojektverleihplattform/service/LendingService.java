package de.hhu.abschlussprojektverleihplattform.service;

import de.hhu.abschlussprojektverleihplattform.logic.Timespan;
import de.hhu.abschlussprojektverleihplattform.repository.ILendingRepository;
import de.hhu.abschlussprojektverleihplattform.service.propay.IPaymentService;
import de.hhu.abschlussprojektverleihplattform.model.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class LendingService implements ILendingService {

    private ILendingRepository lendingRepository;
    private IPaymentService paymentService;

    public LendingService(ILendingRepository lendingRepository, IPaymentService paymentService) {
        this.lendingRepository = lendingRepository;
        this.paymentService = paymentService;
    }

    public List<Timespan> getAvailableTime(ProductEntity product) {
        List<LendingEntity> lendings = lendingRepository.getAllLendingsFromProduct(product);
        List<Timespan> list = new ArrayList<Timespan>();
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
    ) throws Exception {
        List<LendingEntity> lendings = lendingRepository.getAllLendingsFromProduct(product);
        for (LendingEntity lend : lendings) {
            Timestamp lend_start = lend.getStart();
            Timestamp lend_end = lend.getEnd();
            if (
                (start.after(lend_start) && start.before(lend_end))
                    || (end.after(lend_start) && end.before(lend_end))
                    || (lend_start.after(start) && lend_start.before(end))
                    || start.equals(lend_start)
            ) {
                throw new Exception("The Product is not available in the selected time.");
            }
        }
        int totalMoney = product.getSurety()
            + product.getCost() * daysBetweenTwoTimestamps(start, end);
        Long userMoney = paymentService.usersCurrentBalance(actingUser.getUsername());
        if (userMoney < totalMoney) {
            throw new Exception("The cost and the surety sum up to: "
                + totalMoney + "€, but you only have: " + userMoney + "€.");
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
            throw new Exception("The Lending has the Status: " + lending.getStatus()
                + " but it needs to be: " + Lendingstatus.requested);
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
    }

    public void denyLendingRequest(LendingEntity lending) throws Exception {
        if (!lending.getStatus().equals(Lendingstatus.requested)) {
            throw new Exception("The Lending has the Status: " + lending.getStatus()
                + " but it needs to be: " + Lendingstatus.requested);
        }
        lending.setStatus(Lendingstatus.denied);
        lendingRepository.update(lending);
    }

    public void returnProduct(LendingEntity lending) throws Exception {
        if (!lending.getStatus().equals(Lendingstatus.confirmt)) {
            throw new Exception("The Lending has the Status: " + lending.getStatus()
                + " but it needs to be: " + Lendingstatus.confirmt);
        }
        lending.setStatus(Lendingstatus.returned);
        lendingRepository.update(lending);
    }

    public void acceptReturnedProduct(LendingEntity lending) throws Exception {
        if (!lending.getStatus().equals(Lendingstatus.returned)) {
            throw new Exception("The Lending has the Status: " + lending.getStatus()
                + " but it needs to be: " + Lendingstatus.returned);
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
            throw new Exception("The Lending has the Status: " + lending.getStatus()
                + " but it needs to be: " + Lendingstatus.returned);
        }
        lending.setStatus(Lendingstatus.conflict);
        lendingRepository.update(lending);
    }

    public void ownerReceivesSuretyAfterConflict(LendingEntity lending) throws Exception {
        if (!lending.getStatus().equals(Lendingstatus.conflict)) {
            throw new Exception("The Lending has the Status: " + lending.getStatus()
                    + " but it needs to be: " + Lendingstatus.conflict);
        }
        paymentService.tranferReservatedMoney(
            lending.getBorrower().getUsername(),
            lending.getSuretyReservationID()
        );
        lending.setStatus(Lendingstatus.done);
        lendingRepository.update(lending);
    }

    public void borrowerReceivesSuretyAfterConflict(LendingEntity lending) throws Exception {
        if (!lending.getStatus().equals(Lendingstatus.conflict)) {
            throw new Exception("The Lending has the Status: " + lending.getStatus()
                    + " but it needs to be: " + Lendingstatus.conflict);
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

    protected int daysBetweenTwoTimestamps(Timestamp start, Timestamp end) {
        long differenceInMillis = end.getTime() - start.getTime();
        double differenceInDays = differenceInMillis / (1000.0 * 60 * 60 * 24);
        return (int) Math.ceil(differenceInDays);
    }
}
