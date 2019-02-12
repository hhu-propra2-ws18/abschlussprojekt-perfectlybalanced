package de.hhu.abschlussprojektverleihplattform.model;

import lombok.Data;

@Data
public class AddressEntity {

    private Long id;
    private String street;
    private int housenumber;
    private int postcode;
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
