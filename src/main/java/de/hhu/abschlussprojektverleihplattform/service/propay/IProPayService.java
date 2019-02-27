package de.hhu.abschlussprojektverleihplattform.service.propay;

import de.hhu.abschlussprojektverleihplattform.service.propay.exceptions.ProPayAccountNotExistException;
import de.hhu.abschlussprojektverleihplattform.service.propay.model.Account;
import de.hhu.abschlussprojektverleihplattform.service.propay.model.Reservation;

public interface IProPayService {

    boolean createAccountIfNotExists(String username) throws Exception;

    long getBalance(String username) throws Exception;

    Account accountExists(String username) throws ProPayAccountNotExistException;

    boolean makePayment(String sourceAccount, String targetAccount, long amount);

    void changeUserBalanceBy(String username, long delta) throws Exception;

    Reservation makeReservationFromSourceUserToTargetUser(
        String userSource, String userTarget, long amount
    ) throws Exception;

    Account getAccount(String username) throws Exception;

    void returnReservedAmount(String username, Long reservationId) throws Exception;

    void punishReservedAmount(String sourceUsername, Long reservationId) throws Exception;
}
