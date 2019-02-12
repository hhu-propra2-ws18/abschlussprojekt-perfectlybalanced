package de.hhu.abschlussprojektverleihplattform.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class LendingEntity {

    private Long id;
    private Lendingstatus status;
    private Timestamp start;
    private Timestamp end;
    private UserEntity borrower;
    private ProductEntity product;

    public LendingEntity() {
        
    }

    public LendingEntity(Lendingstatus status, Timestamp start, Timestamp end, UserEntity borrower, ProductEntity product) {
        this.status = status;
        this.start = start;
        this.end = end;
        this.borrower = borrower;
        this.product = product;
    }

}

