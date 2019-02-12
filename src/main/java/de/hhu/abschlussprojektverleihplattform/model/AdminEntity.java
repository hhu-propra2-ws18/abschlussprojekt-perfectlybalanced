package de.hhu.abschlussprojektverleihplattform.model;

import lombok.Data;

@Data
public class AdminEntity {

    private Long id;
    private String username;
    private String password;

    public AdminEntity() {

    }

    public AdminEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
