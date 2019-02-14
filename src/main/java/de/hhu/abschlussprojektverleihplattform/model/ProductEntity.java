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
    private String title;
    private int surety;
    private int cost;

    @Embedded
    private AddressEntity location;

    @OneToOne
    @Embedded
    private UserEntity owner;

    public ProductEntity() {

    }

    public ProductEntity(String description, String title, int surety, int cost, AddressEntity location, UserEntity owner) {
        this.description = description;
        this.title = title;
        this.surety = surety;
        this.cost = cost;
        this.location = location;
        this.owner = owner;
    }
    
}
