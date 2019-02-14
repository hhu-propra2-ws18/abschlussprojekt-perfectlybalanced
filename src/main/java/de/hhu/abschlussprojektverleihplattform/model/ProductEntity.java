package de.hhu.abschlussprojektverleihplattform.model;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public ProductEntity(
	String description,
	String title,
	int surety,
	int cost,
	AddressEntity location,
	UserEntity owner
    ) {
        this.description = description;
        this.title = title;
        this.surety = surety;
        this.cost = cost;
        this.location = location;
        this.owner = owner;
    }
    
}
