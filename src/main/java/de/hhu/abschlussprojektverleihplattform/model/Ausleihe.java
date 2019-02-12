package de.hhu.abschlussprojektverleihplattform.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Ausleihe {

    Ausleihstatus status;
    Timestamp start;
    Timestamp ende;
    Benutzer leihender;
    Artikel artikel;

    public Ausleihe() {
        
    }

    public Ausleihe(Ausleihstatus status, Timestamp start, Timestamp ende, Benutzer leihender, Artikel artikel) {
        this.status = status;
        this.start = start;
        this.ende = ende;
        this.leihender = leihender;
        this.artikel = artikel;
    }

}

