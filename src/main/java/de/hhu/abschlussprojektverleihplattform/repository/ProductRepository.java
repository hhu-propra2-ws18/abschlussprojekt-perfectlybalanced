package de.hhu.abschlussprojektverleihplattform.repository;

import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;

import java.util.List;

public class ProductRepository implements IProductRepository {

    @Override
    public List<ProductEntity> findAll() {
        return null;
    }

    @Override
    public void saveProduct(ProductEntity product) {

    }

    @Override
    public ProductEntity getProductById(Long id) {
        return null;
    }

    @Override
    public ProductEntity getProductByTitle(String title) {
        return null;
    }

    @Override
    public List<ProductEntity> getProductsWithPriceLowerThan(int price) {
        return null;
    }

    @Override
    public List<ProductEntity> getProductsWithSuretyLowerThan(int surety) {
        return null;
    }
}
