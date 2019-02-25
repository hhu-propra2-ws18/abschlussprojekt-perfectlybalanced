package de.hhu.abschlussprojektverleihplattform.logic;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class Timespan {

    private Timestamp start;
    private Timestamp end;

    public Timespan(Timestamp start, Timestamp end) {
        this.start = start;
        this.end = end;
    }

    public Timespan() {

    }

}
