package de.hhu.abschlussprojektverleihplattform.logic;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;

public interface IPayment {
    boolean userHasAmount(UserEntity User, int amount);

    // gibt die ID zurueck, fals der wert groesser als null ist, sonst ist die reservierung fehlgeschlagen
    Long reservateAmount(UserEntity payingUser, UserEntity recivingUser, int amount);

    boolean tranferReservatedMoney(String username,Long id);

    boolean returnReservatedMoney(String username,Long id);
}
