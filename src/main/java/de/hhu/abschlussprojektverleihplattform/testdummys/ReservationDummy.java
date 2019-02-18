package de.hhu.abschlussprojektverleihplattform.testdummys;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import lombok.Data;

@Data
public class ReservationDummy {

    private Long id;
    private int amount;
    private UserEntity from;
    private UserEntity to;
    private PaymentStatus status;

    public ReservationDummy(UserEntity payer, UserEntity reciver, int amount, Long id) {
        this.amount = amount;
        this.from = payer;
        this.to = reciver;
        this.status = PaymentStatus.reservated;
        this.id = id;
    }

}

