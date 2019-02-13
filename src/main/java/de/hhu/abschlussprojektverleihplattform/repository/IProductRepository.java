package de.hhu.abschlussprojektverleihplattform.repository;

import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;

import java.util.List;

public interface IProductRepository {

    List<ProductEntity> findAll();
    void saveProduct(ProductEntity product);
    ProductEntity getProductById(Long id);
    ProductEntity getProductByTitle(String title);
    List<ProductEntity> getProductsWithPriceLowerThan(int price);
    List<ProductEntity> getProductsWithSuretyLowerThan(int surety);

}
