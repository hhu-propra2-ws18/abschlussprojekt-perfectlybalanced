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
    @Size(min=2, max=32, message = "Vorname {string.size.error}")
    private String firstname;
    @NotBlank
    @Size(min=2,max=32, message = "Nachname {string.size.error}")
    private String lastname;
    @Column(unique = true)
    @NotBlank
    @Size(min=3, max=32, message = "Username {string.size.error}")
    private String username;
    @NotBlank
    @Size(min=6, message = "{user.password.error}")
    private String password;
    @NotEmpty
    @Email(message = "{email.error}")
    private String email;
    private Role role;

    public UserEntity() {

    }

/*    public UserEntity(
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
    }*/

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

    public UserEntity(Long userId,
                      String firstname,
                      String lastname,
                      String username,
                      String password,
                      String email,
                      Role role) {
        this.userId = userId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }
}

