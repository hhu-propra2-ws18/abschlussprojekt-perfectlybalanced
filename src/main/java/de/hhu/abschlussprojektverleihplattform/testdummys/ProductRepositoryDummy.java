package de.hhu.abschlussprojektverleihplattform.testdummys;

import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.repository.IProductRepository;

import java.util.List;

public class ProductRepositoryDummy implements IProductRepository {

    private ProductEntity productToUpdate;
    private boolean hasBeenUpdated;

    public ProductRepositoryDummy() {
        hasBeenUpdated = false;
        productToUpdate = null;
    }

    @Override
    public void editProduct(ProductEntity productEntity) {
        if(productEntity.getTitle().equals(productToUpdate.getTitle())
            && productEntity.getPrice() == productToUpdate.getPrice()
        ) {
            hasBeenUpdated = true;
        }
    }

    // Methodes for Testing

    public boolean getHasBeenUpdated() {
        return hasBeenUpdated;
    }

    public void setProductToUpdate(ProductEntity product) {
        productToUpdate = product;
    }

    // Methodes from the Interface that are not needed for Testing

    @Override
    public ProductEntity getProductById(Long id) {
        return null;
    }

    @Override
    public List<ProductEntity> getAllProductsFromUser(UserEntity user) {
        return null;
    }

    @Override
    public List<ProductEntity> getAllProducts() {
        return null;
    }

    @Override
    public void saveProduct(ProductEntity productEntity) {

    }
}
