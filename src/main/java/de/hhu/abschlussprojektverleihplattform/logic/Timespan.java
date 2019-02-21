package de.hhu.abschlussprojektverleihplattform.logic;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class Timespan {

    private Timestamp start, end;

    public Timespan(Timestamp start, Timestamp end) {
        this.start = start;
        this.end = end;
    }

}
