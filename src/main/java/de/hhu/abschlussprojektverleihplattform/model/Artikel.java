package de.hhu.abschlussprojektverleihplattform.model;

import lombok.Data;

@Data
public class Artikel {

    private String beschreibung;
    private String titel;
    private int kaution;
    private int kosten;

    public Artikel() {

    }

    public Artikel(String beschreibung, String titel, int kaution, int kosten) {
        this.beschreibung = beschreibung;
        this.titel = titel;
        this.kaution = kaution;
        this.kosten = kosten;
    }
    
}
