package de.hhu.abschlussprojektverleihplattform.logic;

import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;

import java.util.List;

public interface Product_Service {
    void addProduct(ProductEntity product);

    List<ProductEntity> getAllProducts();

    // Hier koennten noch suchfunktionen usw hinzukommen, aber die sind ja optionale Aufgaben
}
