package de.hhu.abschlussprojektverleihplattform.testdummys;

import de.hhu.abschlussprojektverleihplattform.service.propay.IPaymentService;

import java.util.ArrayList;

public class PaymentServiceDummy implements IPaymentService {

    private ArrayList<ReservationDummy> payments;
    private Long id;

    private String lastCalledUsername;
    private Long lastCalledId;
    private boolean lastWasTransfer;

    private Long userCurrentBalance;
    private Exception userCurrentBalanceFailed;
    private boolean userCurrentBalanceThrowsException;

    private Exception reservationFailed;
    private boolean reservationThrowsException;

    private Exception returnFailed;
    private boolean returnThrowsException;

    private Exception transferFailed;
    private boolean transferThrowsException;

    public PaymentServiceDummy() {
        payments = new ArrayList<ReservationDummy>();
        id = 1L;
        lastCalledUsername = "";
        lastCalledId = 0L;
        lastWasTransfer = false;
    }

    public void configurateUsersCurrentBalance(Long userCurrentBalance, Exception userCurrentBalanceFailed, boolean userCurrentBalanceThrowsException) {
        this.userCurrentBalance = userCurrentBalance;
        this.userCurrentBalanceFailed = userCurrentBalanceFailed;
        this.userCurrentBalanceThrowsException = userCurrentBalanceThrowsException;
    }

    @Override
    public Long usersCurrentBalance(String username) throws Exception {
        if(userCurrentBalanceThrowsException) {
            throw userCurrentBalanceFailed;
        } else {
            return userCurrentBalance;
        }
    }

    public void configureReservateAmount(Exception reservationFailed, boolean reservationThrowsException) {
        this.reservationFailed = reservationFailed;
        this.reservationThrowsException = reservationThrowsException;
    }

    @Override
    public Long reservateAmount(String payingUser, String recivingUser, int amount) throws Exception {
        if (reservationThrowsException) {
            throw reservationFailed;
        }
        ReservationDummy reservation = new ReservationDummy(payingUser, recivingUser, amount, id);
        payments.add(reservation);
        id++;
        return reservation.getId();
    }

    public void configureTransfer(Exception transferFailed, boolean transferThrowsException) {
        this.transferFailed = transferFailed;
        this.transferThrowsException = transferThrowsException;
    }

    @Override
    public void tranferReservatedMoney(String username, Long id) throws Exception {
        lastCalledId = id;
        lastCalledUsername = username;
        lastWasTransfer = true;
        if(transferThrowsException) {
            throw transferFailed;
        }
    }

    public  void configureReturn(Exception returnFailed, boolean returnThrowsException) {
        this.returnFailed = returnFailed;
        this.returnThrowsException = returnThrowsException;

    }

    @Override
    public void returnReservatedMoney(String username, Long id) throws Exception {
        lastCalledId = id;
        lastCalledUsername = username;
        lastWasTransfer = false;
        if(returnThrowsException) {
            throw returnFailed;
        }
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
            if (reservation.getId().equals(id)) {
                return reservation;
            }
        }
        return null;
    }
}
