package de.hhu.abschlussprojektverleihplattform.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
@Entity
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long senderID;
    private Long receiverID;
    private int amount;
    @SuppressFBWarnings(justification="generated code")
    private Timestamp date;

    public TransactionEntity(){

    }

    public TransactionEntity(
    Long sender,
    Long receiver,
    int cost,
    Timestamp date
    ){
        this.sender = sender;
        this.receiver = receiver;
        this.cost = cost;
        this.date = date;
    }
}
