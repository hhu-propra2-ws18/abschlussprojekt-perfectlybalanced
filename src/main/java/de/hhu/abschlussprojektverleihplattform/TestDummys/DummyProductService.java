package de.hhu.abschlussprojektverleihplattform.TestDummys;

import de.hhu.abschlussprojektverleihplattform.logic.IProduct;
import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;

import java.util.ArrayList;
import java.util.List;

public class DummyProductService implements IProduct {

    private ArrayList<ProductEntity> products;

    public DummyProductService() {
        products = new ArrayList<>();
    }

    @Override
    public void addProduct(ProductEntity product) {
        products.add(product);
    }

    @Override
    public List<ProductEntity> getAllProducts() {
        return products;
    }

    public  ArrayList<ProductEntity> getProducts() {
        return products;
    }
}
