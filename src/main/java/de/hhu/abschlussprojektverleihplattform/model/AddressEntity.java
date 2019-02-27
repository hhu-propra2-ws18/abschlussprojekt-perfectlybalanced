package de.hhu.abschlussprojektverleihplattform.model;

import javax.persistence.*;
import javax.validation.constraints.*;

import lombok.Data;
import org.hibernate.validator.constraints.Range;


@Data
@Embeddable
public class AddressEntity {

    @NotBlank
    @Size(min=5, message = "Adresse {address.size.error}")
    private String street;
    @NotNull
    @Min(value=0, message = "{address.housenumber.error}")
    private int housenumber;
    @NotNull
    @Range(min= 10000, max=99999, message = "{address.postcode.error}")
    private int postcode;
    @NotBlank
    @Size(min=5, message = "Stadt {address.size.error}")
    private String city;


    public AddressEntity() {

    }

    public AddressEntity(String street, int housenumber, int postcode, String city) {
        this.street = street;
        this.housenumber = housenumber;
        this.postcode = postcode;
        this.city = city;
    }
}
