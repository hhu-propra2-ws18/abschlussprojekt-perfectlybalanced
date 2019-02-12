package de.hhu.abschlussprojektverleihplattform.model;

import lombok.Data;

@Data
public class ProductEntity {

    private Long id;
    private String description;
    private String titel;
    private int surety;
    private int cost;

    public ProductEntity() {

    }

    public ProductEntity(String description, String titel, int surety, int cost) {
        this.description = description;
        this.titel = titel;
        this.surety = surety;
        this.cost = cost;
    }
    
}
