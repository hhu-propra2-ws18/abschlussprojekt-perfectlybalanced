package de.hhu.abschlussprojektverleihplattform.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class ProductEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String description;
    private String titel;
    private int surety;
    private int cost;
    @Embedded
//    @OneToOne
    private AddressEntity location;
    @OneToOne
    private UserEntity owner;

    public ProductEntity() {

    }

    public ProductEntity(String description, String titel, int surety, int cost, AddressEntity location, UserEntity owner) {
        this.description = description;
        this.titel = titel;
        this.surety = surety;
        this.cost = cost;
        this.location = location;
        this.owner = owner;
    }
    
}
