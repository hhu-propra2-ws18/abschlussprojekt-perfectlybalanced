package de.hhu.abschlussprojektverleihplattform.TestDummys;

import de.hhu.abschlussprojektverleihplattform.logic.IPayment;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.service.propay.model.Reservation;

import java.util.ArrayList;

public class PaymentServiceDummy implements IPayment {

    private ArrayList<ReservationDummy> payments;
    private Long id;
    private boolean usersHaveMoney;
    private boolean reservationsAreSuccessfull;
    private boolean transfersAreSuccessfull;
    private boolean returnsAreSuccessfull;

    public PaymentServiceDummy(boolean UsersHaveMoney, boolean ReservationsAreSuccessfull, boolean TransfersAreSuccessfull, boolean ReturnsAreSuccessfull) {
        payments = new ArrayList<>();
        id = 1l;
        this.usersHaveMoney = UsersHaveMoney;
        this.reservationsAreSuccessfull = ReservationsAreSuccessfull;
        this.transfersAreSuccessfull = TransfersAreSuccessfull;
        this.reservationsAreSuccessfull = ReservationsAreSuccessfull;
    }

    @Override
    public boolean UserHasAmount(UserEntity User, int amount) {
        return usersHaveMoney;
    }

    @Override
    public Long reservateAmount(UserEntity payingUser, UserEntity recivingUser, int amount) {
        if (!reservationsAreSuccessfull) {
            return 0l;
        }
        ReservationDummy reservation = new ReservationDummy(payingUser, recivingUser, amount, id);
        id++;
        return reservation.getId();
    }

    @Override
    public boolean tranferReservatedMoney(Long id) {
        if (!transfersAreSuccessfull) {
            return false;
        }
        ReservationDummy reservation = findReservation(id);
        if (reservation == null) {
            return false;
        }
        if (reservation.getStatus() == PaymentStatus.reservated) {
            reservation.setStatus(PaymentStatus.payed);
            return true;
        }
        return false;
    }

    @Override
    public boolean returnReservatedMoney(Long id) {
        if (!returnsAreSuccessfull) {
            return false;
        }
        ReservationDummy reservation = findReservation(id);
        if (reservation == null) {
            return false;
        }
        if (reservation.getStatus() == PaymentStatus.reservated) {
            reservation.setStatus(PaymentStatus.returned);
            return true;
        }
        return false;
    }

    public ReservationDummy findReservation(Long id) {
        for (ReservationDummy reservation : payments) {
            if (reservation.getId() == id) {
                return reservation;
            }
        }
        return null;
    }
}
