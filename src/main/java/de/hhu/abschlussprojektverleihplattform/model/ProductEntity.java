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
    private Productstatus status;
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
    @NotNull
    @Min(value=0, message = "Kosten {product.price.error}")
    private int price;

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
        this.price = 0;
        this.location = location;
        this.owner = owner;
        this.status = Productstatus.forLending;
    }

    public ProductEntity(
            Long id,
            String description,
            String title,
            int surety,
            int cost,
            AddressEntity location,
            UserEntity owner
    ) {
        this.id = id;
        this.description = description;
        this.title = title;
        this.surety = surety;
        this.cost = cost;
        this.price = 0;
        this.location = location;
        this.owner = owner;
        this.status = Productstatus.forLending;
    }

    public ProductEntity(
            String description,
            String title,
            int price,
            AddressEntity location,
            UserEntity owner
    ) {
        this.description = description;
        this.title = title;
        this.surety = 0;
        this.cost = 0;
        this.price = price;
        this.location = location;
        this.owner = owner;
        this.status = Productstatus.forBuying;
    }

    public ProductEntity(
            Long id,
            String description,
            String title,
            int price,
            AddressEntity location,
            UserEntity owner
    ) {
        this.id = id;
        this.description = description;
        this.title = title;
        this.surety = 0;
        this.cost = 0;
        this.price = price;
        this.location = location;
        this.owner = owner;
        this.status = Productstatus.forBuying;
    }
}
