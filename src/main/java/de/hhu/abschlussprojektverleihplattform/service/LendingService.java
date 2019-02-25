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

    public boolean requestLending(
            UserEntity actingUser,
            ProductEntity product,
            Timestamp start,
            Timestamp end
    ) {
        List<LendingEntity> lendings = lendingRepository.getAllLendingsFromProduct(product);
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
        int totalMoney = product.getSurety()
            + product.getCost() * daysBetweenTwoTimestamps(start, end);
        boolean moneyIsOK = paymentService.userHasAmount(actingUser, totalMoney);
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
            lendingRepository.addLending(lending);
            return true;
        } else {
            return false;
        }
    }

    public boolean acceptLendingRequest(LendingEntity lending) {
        Long costID = paymentService.reservateAmount(
            lending.getBorrower(),
            lending.getProduct().getOwner(),
            lending.getProduct().getCost()
                * daysBetweenTwoTimestamps(lending.getStart(), lending.getEnd())
        );
        Long suretyID = paymentService.reservateAmount(
            lending.getBorrower(),
            lending.getProduct().getOwner(),
            lending.getProduct().getSurety()
        );
        if (costID > 0 && suretyID > 0) {
            if (
                paymentService.tranferReservatedMoney(
                    lending.getBorrower().getUsername(),
                    costID
                )
            ) {
                lending.setStatus(Lendingstatus.confirmt);
                lending.setCostReservationID(costID);
                lending.setSuretyReservationID(suretyID);
                lendingRepository.update(lending);
                return true;
            }
        }
        paymentService.returnReservatedMoney(lending.getBorrower().getUsername(), costID);
        paymentService.returnReservatedMoney(lending.getBorrower().getUsername(), suretyID);
        return false;
    }

    public void denyLendingRequest(LendingEntity lending) {
        lending.setStatus(Lendingstatus.denied);
        lendingRepository.update(lending);
    }

    public void returnProduct(LendingEntity lending) {
        lending.setStatus(Lendingstatus.returned);
        lendingRepository.update(lending);
    }

    public boolean acceptReturnedProduct(LendingEntity lending) {
        if (paymentService.returnReservatedMoney(
                lending.getBorrower().getUsername(),
                lending.getSuretyReservationID()
            )
        ) {
            lending.setStatus(Lendingstatus.done);
            lendingRepository.update(lending);
            return true;
        }
        return false;
    }

    public void denyReturnedProduct(LendingEntity lending) {
        lending.setStatus(Lendingstatus.conflict);
        lendingRepository.update(lending);
    }

    public boolean ownerReceivesSuretyAfterConflict(LendingEntity lending) {
        if (
            paymentService.tranferReservatedMoney(
                lending.getBorrower().getUsername(),
                lending.getSuretyReservationID()
            )
        ) {
            lending.setStatus(Lendingstatus.done);
            lendingRepository.update(lending);
            return true;
        } else {
            return false;
        }
    }

    public boolean borrowerReceivesSuretyAfterConflict(LendingEntity lending) {
        if (
            paymentService.returnReservatedMoney(
                lending.getBorrower().getUsername(),
                lending.getSuretyReservationID()
            )
        ) {
            lending.setStatus(Lendingstatus.done);
            lendingRepository.update(lending);
            return true;
        } else {
            return false;
        }
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

    private List<LendingEntity> filterByStatus(List<LendingEntity> lendings, Lendingstatus status){
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
        return filterByStatus(allLendings,Lendingstatus.requested);
    }

    public List<LendingEntity> getAllConfirmedLendings(List<LendingEntity> allLendings) {
        return filterByStatus(allLendings,Lendingstatus.confirmt);
    }

    public List<LendingEntity> getAllReturnedLendings(List<LendingEntity> allLendings) {
        return filterByStatus(allLendings,Lendingstatus.returned);
    }

    public List<LendingEntity> getAllConflictedLendings(List<LendingEntity> allLendings) {
        return filterByStatus(allLendings,Lendingstatus.conflict);
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
