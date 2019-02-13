package de.hhu.abschlussprojektverleihplattform.repository;

import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ProductRepositoryTest {

    private ProductRepository productRepository = new ProductRepository();

    @Test
    public void testFindAllExpectedNull(){
        List<ProductEntity> testList = productRepository.findAll();
        assertNull(testList);
    }


}