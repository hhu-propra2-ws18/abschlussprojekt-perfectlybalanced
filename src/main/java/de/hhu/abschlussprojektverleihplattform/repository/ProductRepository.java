package de.hhu.abschlussprojektverleihplattform.repository;

import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository implements IProductRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<ProductEntity> findAll() {
        ArrayList<ProductEntity> products=new ArrayList<>();

        jdbcTemplate.query("select * from product",
            new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet resultSet) throws SQLException {
                    while(resultSet.next()) {

                        products.add(null);
                    }
                }
            }
        );
        return products;
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
