package de.hhu.abschlussprojektverleihplattform.repository;

import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;

import java.util.List;

public interface IProductRepository {

    List<ProductEntity> getAllProducts();
    void saveProduct(ProductEntity productEntity);
    ProductEntity getProductById(Long id);
    ProductEntity getProductByTitlel(String title);
    List<ProductEntity> getAllProductsFromUser(UserEntity user);
}
