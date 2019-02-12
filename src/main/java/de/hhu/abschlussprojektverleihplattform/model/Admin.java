package de.hhu.abschlussprojektverleihplattform.model;

import lombok.Data;

@Data
public class Admin {

    String benutzername;
    String passwort;

    public Admin() {

    }

    public Admin(String benutzername, String passwort) {
        this.benutzername = benutzername;
        this.passwort = passwort;
    }
}
