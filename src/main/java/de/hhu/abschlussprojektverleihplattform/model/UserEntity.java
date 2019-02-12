package de.hhu.abschlussprojektverleihplattform.model;

import lombok.Data;

@Data
public class UserEntity {

    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private String email;

    public UserEntity() {

    }

    public UserEntity(String firstname, String lastname, String username, String password, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
