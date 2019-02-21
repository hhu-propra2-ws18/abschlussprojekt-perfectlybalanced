package de.hhu.abschlussprojektverleihplattform.logic;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class Zeitspanne {

    private Timestamp start, end;

    public Zeitspanne(Timestamp start, Timestamp end) {
        this.start = start;
        this.end = end;
    }

}
