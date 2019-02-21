package de.hhu.abschlussprojektverleihplattform.logic;

import lombok.Data;

import javax.persistence.Entity;
import java.sql.Date;
import java.sql.Timestamp;

@Data
//@Entity
public class Zeitspanne {

 //   private Date start;
 //   private Date end;
    private Timestamp start, end;

    public Zeitspanne(Timestamp start, Timestamp end) {
        //this.start = new Date(start.getTime());
        //this.end = new Date(end.getTime());
        this.start = start;
        this.end = end;
    }

}
