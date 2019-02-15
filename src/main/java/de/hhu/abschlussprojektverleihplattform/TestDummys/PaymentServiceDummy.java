package de.hhu.abschlussprojektverleihplattform.TestDummys;

import de.hhu.abschlussprojektverleihplattform.service.propay.IPaymentService;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;

import java.util.ArrayList;

public class PaymentServiceDummy implements IPaymentService {

    private ArrayList<ReservationDummy> payments;
    private Long id;

    private String lastCalledUsername;
    private Long lastCalledId;
    private boolean lastWasTransfer;

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
        lastCalledUsername = "";
        lastCalledId = 0l;
        lastWasTransfer = false;
    }

    @Override
    public boolean userHasAmount(UserEntity User, int amount) {
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
        lastCalledId = id;
        lastCalledUsername = username;
        lastWasTransfer = true;
        return transfersAreSuccessfull;
    }

    @Override
    public boolean returnReservatedMoney(String username, Long id) {
        lastCalledId = id;
        lastCalledUsername = username;
        lastWasTransfer = false;
        return returnsAreSuccessfull;
    }

    public Long getLastId() {
        return lastCalledId;
    }

    public String getLastUsername() {
        return lastCalledUsername;
    }

    public boolean getLastWasTransfer() {
        return lastWasTransfer;
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
