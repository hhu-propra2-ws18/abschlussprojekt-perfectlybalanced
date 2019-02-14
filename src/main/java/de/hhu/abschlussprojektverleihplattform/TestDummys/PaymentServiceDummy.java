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
        this.returnsAreSuccessfull = ReturnsAreSuccessfull;
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
        payments.add(reservation);
        id++;
        return reservation.getId();
    }

    @Override
    public boolean tranferReservatedMoney(String username, Long id) {
        return transfersAreSuccessfull;
    }

    @Override
    public boolean returnReservatedMoney(String username, Long id) {
        return returnsAreSuccessfull;
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
