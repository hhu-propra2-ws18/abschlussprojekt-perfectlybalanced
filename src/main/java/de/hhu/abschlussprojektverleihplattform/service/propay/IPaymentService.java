package de.hhu.abschlussprojektverleihplattform.service.propay;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;

public interface IPaymentService {
    boolean userHasAmount(UserEntity user, int amount);

    // gibt die ID zurueck, fals der wert groesser als null ist,
    // sonst ist die reservierung fehlgeschlagen
    Long reservateAmount(String payingUser, String recivingUser, int amount) throws Exception;

    boolean tranferReservatedMoney(String username,Long id);

    boolean returnReservatedMoney(String username,Long id);

    Long usersCurrentBalance(String username);
}
