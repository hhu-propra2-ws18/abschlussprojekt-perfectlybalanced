package de.hhu.abschlussprojektverleihplattform.model;

import javax.persistence.*;
import javax.validation.constraints.*;

import lombok.Data;

@Data
@Entity
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min=10, max=255, message = "Beschreibung {string.size.error}")
    private String description;
    @NotBlank
    @Size(min=5, max=50, message = "Titel {string.size.error}")
    private String title;
    @NotNull
    @Min(value=0, message = "Kaution {product.price.error}")
    private int surety;
    @NotNull
    @Min(value=0, message = "Kosten {product.price.error}")
    private int cost;

    @Embedded
    private AddressEntity location;

    @ManyToOne
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
