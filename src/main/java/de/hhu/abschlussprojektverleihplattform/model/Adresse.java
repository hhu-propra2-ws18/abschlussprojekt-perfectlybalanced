package de.hhu.abschlussprojektverleihplattform.model;

import lombok.Data;

@Data
public class Adresse {

    private Long id;
    private String strasse;
    private int hausnummer;
    private int plz;
    private String ort;


    public Adresse() {

    }

    public Adresse(String strasse, int hausnummer, int plz, String ort) {
        this.strasse = strasse;
        this.hausnummer = hausnummer;
        this.plz = plz;
        this.ort = ort;
    }
}
