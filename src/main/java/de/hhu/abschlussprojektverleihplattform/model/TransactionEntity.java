package de.hhu.abschlussprojektverleihplattform.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private UserEntity sender;
    @OneToOne
    private UserEntity receiver;
    private int amount;
    @SuppressFBWarnings(justification="generated code")
    private Timestamp date;

    public TransactionEntity(){

    }

    public TransactionEntity(
        UserEntity sender,
        UserEntity receiver,
        int amount,
        Timestamp date
    ){
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.date = date;
    }
}
