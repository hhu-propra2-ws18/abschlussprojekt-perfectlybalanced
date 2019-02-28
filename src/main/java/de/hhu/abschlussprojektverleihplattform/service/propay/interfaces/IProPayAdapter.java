package de.hhu.abschlussprojektverleihplattform.service.propay.interfaces;

import de.hhu.abschlussprojektverleihplattform.service.propay.exceptions.ProPayAccountNotExistException;
import de.hhu.abschlussprojektverleihplattform.service.propay.model.Account;
import de.hhu.abschlussprojektverleihplattform.service.propay.model.Reservation;
import org.springframework.web.client.HttpClientErrorException;

public interface IProPayAdapter {

    //this represents the ProPay API,
    //not the API which is needed by the rest of the application

    //Accounts
    Account getAccount(String username)
            throws Exception;

    Account createAccountIfNotAlreadyExistsAndIncreaseBalanceBy(
            String username,
            long amount
    )
            throws Exception;

    //Payments
    void makePayment(
            String sourceUsername,
            String destinationUsername,
            long amount
    )
            throws Exception;

    //Reservations
    Reservation makeReservation(
            String sourceUsername,
            String destinationUsername,
            long amount
    )
            throws Exception;

    Account releaseReservation(
            String sourceUsername,
            long reservationId
    )
            throws Exception;

    Account punishReservation(
            String sourceUsername,
            long reservationId
    )
            throws Exception;
}
