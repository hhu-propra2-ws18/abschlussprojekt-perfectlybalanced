package de.hhu.abschlussprojektverleihplattform.logic;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;

public interface Payment_Service {
    boolean UserHasAmount(UserEntity User, int amount);

    // kann ggf auch void sein, haengt davon ab ob eine bestaetigung notwendig ist oder nicht
    boolean reservateAmount(UserEntity user, int amount);

    void tranferReservatedMoney(UserEntity payingUser, UserEntity recivingUser, int amount);

    void returnReservatedMoney(UserEntity userEntity, int amount);


    // Da ich die genaue funktionsweise des reservierens nicht kenne, kann sich hier noch einiges aendern
}
