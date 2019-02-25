package de.hhu.abschlussprojektverleihplattform.logic;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;
import java.sql.Timestamp;

@Data
public class Timespan {

    @SuppressFBWarnings(justification="generated code")
    private Timestamp start;
    @SuppressFBWarnings(justification="generated code")
    private Timestamp end;

    public Timespan(Timestamp start, Timestamp end) {
        this.start = start;
        this.end = end;
    }

}
