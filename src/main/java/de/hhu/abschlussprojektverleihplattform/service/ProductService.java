package de.hhu.abschlussprojektverleihplattform.service;

import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IProductService {

    @Autowired
    ProductRepository productRepository;

    @Override
    public void addProduct(ProductEntity product){
        productRepository.saveProduct(product);
    }

    @Override
    public void editProduct(ProductEntity product) {
        productRepository.editProduct(product);
    }

    @Override
    public List<ProductEntity> showAll(){
        return productRepository.getAllProducts();
    }

    @Override
    public ProductEntity getById(Long productId) throws EmptyResultDataAccessException {
        return productRepository.getProductById(productId);
    }

    @Override
    public List<ProductEntity> getAllProductsFromUser(UserEntity user) {
        return productRepository.getAllProductsFromUser(user);
    }

    @Override
    public List<ProductEntity> getAllAvailableProducts() {
        return productRepository.getAllAvailableProducts();
    }
}
