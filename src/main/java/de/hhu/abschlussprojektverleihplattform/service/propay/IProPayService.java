package de.hhu.abschlussprojektverleihplattform.service.propay;

import de.hhu.abschlussprojektverleihplattform.service.propay.model.Account;
import de.hhu.abschlussprojektverleihplattform.service.propay.model.Reservation;

public interface IProPayService {

    public boolean createAccountIfNotExists(String username) throws Exception;

    public long getBalance(String username) throws Exception;

    public boolean accountExists(String username);

    public boolean makePayment(String sourceAccount, String targetAccount, long amount);

    public boolean changeUserBalanceBy(String username,long delta);

    public Reservation makeReservationFromSourceUserToTargetUser(String userSource, String userTarget,long amount) throws Exception;

    public Account getAccount(String username) throws Exception;

    public void returnReservedAmount(String username, Long reservationId) throws Exception;
}
