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

}
