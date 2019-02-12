package de.hhu.abschlussprojektverleihplattform.model;

import lombok.Data;

@Data
public class Benutzer {

    Long id;
    String vorname;
    String nachname;
    String benutzername;
    String passwort;
    String mail;

    public Benutzer() {

    }

    public Benutzer(String vorname, String nachname, String benutzername, String passwort, String mail) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.benutzername = benutzername;
        this.passwort = passwort;
        this.mail = mail;
    }
}
