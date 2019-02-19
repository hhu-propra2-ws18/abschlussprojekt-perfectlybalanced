package de.hhu.abschlussprojektverleihplattform.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @NotBlank
    @Size(min=2, max=32, message = "{string.size}")
    private String firstname;
    @NotBlank
    @Size(min=2,max=32, message = "Nachname muss zwischen 2 und 32 Zeichen lang sein")
    private String lastname;
    @Column(unique = true)
    @NotBlank
    @Size(min=3, max=32, message = "Username muss zwischen 3 und 32 Zeichen lang sein")
    private String username;
    @NotBlank
    @Size(min=6, message = "Passwort muss mindestens 6 Zeichen lang sein")
    private String password;
    @NotEmpty
    @Email
    private String email;
    private Role role;

    public UserEntity() {

    }

    public UserEntity(
	String firstname,
	String lastname,
	String username,
	String password,
	String email,
	Role role
    ) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public UserEntity(
	String firstname,
	String lastname,
	String username,
	String password,
	String email
    ) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = Role.ROLE_USER;
    }

    public UserEntity(Long id){
        this.firstname = null;
        this.lastname = null;
        this.username = null;
        this.password = null;
        this.email = null;
    }
}

