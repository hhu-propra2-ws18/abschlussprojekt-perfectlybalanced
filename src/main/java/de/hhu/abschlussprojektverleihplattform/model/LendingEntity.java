package de.hhu.abschlussprojektverleihplattform.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
public class LendingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Lendingstatus status;
    private Timestamp start;
    private Timestamp end;
    @OneToOne
    private UserEntity borrower;
    @OneToOne
    private ProductEntity product;
    private Long costReservationID;
    private Long suretyReservationID;

    public LendingEntity() {
        
    }

    public LendingEntity(Lendingstatus status, Timestamp start, Timestamp end, UserEntity borrower, ProductEntity product, Long costReservationID, Long suretyReservationID) {
        this.status = status;
        this.start = start;
        this.end = end;
        this.borrower = borrower;
        this.product = product;
        this.costReservationID = costReservationID;
        this.suretyReservationID = suretyReservationID;
    }

}

