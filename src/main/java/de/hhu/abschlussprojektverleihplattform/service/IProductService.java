package de.hhu.abschlussprojektverleihplattform.service;

import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;

import java.util.List;

public interface IProductService {
    void addProduct(ProductEntity product);

    void editProduct(ProductEntity product);

    List<ProductEntity> showAll();

    ProductEntity getById(Long productId);

    List<ProductEntity> getAllProductsFromUser(UserEntity user);
}
