package de.hhu.abschlussprojektverleihplattform.model;

import lombok.Data;

@Data
public class ProductEntity {

    private Long id;
    private String description;
    private String titel;
    private int surety;
    private int cost;
    private AddressEntity location;
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
