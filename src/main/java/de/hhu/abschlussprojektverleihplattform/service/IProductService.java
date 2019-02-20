package de.hhu.abschlussprojektverleihplattform.service;

import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;

import java.util.List;

public interface IProductService {
    void addProduct(ProductEntity product);

    void editProduct(ProductEntity product);

    List<ProductEntity> showAll();

    ProductEntity getById(Long productId);

    ProductEntity getByTitle(String productTitle);

}
