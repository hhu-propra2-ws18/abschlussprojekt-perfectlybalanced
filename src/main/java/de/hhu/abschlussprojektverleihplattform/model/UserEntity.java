package de.hhu.abschlussprojektverleihplattform.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private String email;
    private Role role;

    public UserEntity() {

    }

    public UserEntity(String firstname, String lastname, String username, String password, String email, Role role) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public UserEntity(String firstname, String lastname, String username, String password, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = Role.user;
    }
}

