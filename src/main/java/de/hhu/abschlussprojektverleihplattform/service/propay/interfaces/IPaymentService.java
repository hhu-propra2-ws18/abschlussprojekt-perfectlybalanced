package de.hhu.abschlussprojektverleihplattform.service.propay.interfaces;

public interface IPaymentService {

    // gibt die ID zurueck, fals der wert groesser als null ist,
    // sonst ist die reservierung fehlgeschlagen
    Long reservateAmount(String payingUser, String recivingUser, int amount) throws Exception;

    void tranferReservatedMoney(String username,Long id) throws Exception;

    void returnReservatedMoney(String username,Long id) throws Exception;

    Long usersCurrentBalance(String username) throws Exception;
}
