package de.hhu.abschlussprojektverleihplattform.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

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
    @OneToMany
    private List<ProductEntity> products;

    public UserEntity() {

    }

    public UserEntity(String firstname, String lastname, String username, String password, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public UserEntity(Long id){
        this.firstname = null;
        this.lastname = null;
        this.username = null;
        this.password = null;
        this.email = null;
    }
}
