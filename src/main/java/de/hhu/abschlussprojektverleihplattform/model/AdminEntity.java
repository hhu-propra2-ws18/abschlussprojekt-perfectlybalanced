package de.hhu.abschlussprojektverleihplattform.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class AdminEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
