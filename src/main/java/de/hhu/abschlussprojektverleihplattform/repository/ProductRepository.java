package de.hhu.abschlussprojektverleihplattform.repository;

import de.hhu.abschlussprojektverleihplattform.database.ProductEntityRowMapper;
import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

import static de.hhu.abschlussprojektverleihplattform.database.DBUtils.psc;


@Data
@Repository
public class ProductRepository implements IProductRepository {

    final JdbcTemplate jdbcTemplate;
    final UserRepository userRepository;

    @Autowired
    public ProductRepository(JdbcTemplate jdbcTemplate, UserRepository userRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
    }


    @Override
    public ProductEntity getProductById(Long id) throws EmptyResultDataAccessException{

        return (ProductEntity) jdbcTemplate.queryForObject(
            "SELECT * FROM PRODUCT_ENTITY WHERE id=?",
            new Object[]{id},
            new ProductEntityRowMapper(userRepository)
        );
    }

    @Override
    public List<ProductEntity> getAllProducts() {
        return (List<ProductEntity>)jdbcTemplate.query(
	    "SELECT * FROM PRODUCT_ENTITY",
            new Object[]{},
            new ProductEntityRowMapper(userRepository)
	);

    }

    @SuppressFBWarnings(justification="nullpointer exception")
    @Override
    public void saveProduct(ProductEntity product) {
        KeyHolder keyHolder=new GeneratedKeyHolder();


        jdbcTemplate.update(psc(
            "INSERT INTO PRODUCT_ENTITY ("
	        +"COST,"
	        +" DESCRIPTION,"
	        +" CITY,"
	        +" HOUSENUMBER,"
	        +" POSTCODE,"
	        +" STREET,"
	        +" SURETY,"
            +"TITLE,"
            +"STATUS,"
            +"PRICE,"
            +"OWNER_USER_ID)"
	        +" VALUES (?,?,?,?,?,?,?,?,?,?,?)",
            product.getCost(),
            product.getDescription(),
            product.getLocation().getCity(),
            product.getLocation().getHousenumber(),
            product.getLocation().getPostcode(),
            product.getLocation().getStreet(),
            product.getSurety(),
            product.getTitle(),
            product.getStatus().ordinal(),
            product.getPrice(),
            product.getOwner().getUserId()),
            keyHolder
        );
        product.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
    }

    @Override
    public void editProduct(ProductEntity productEntity) throws DataAccessException {
        jdbcTemplate.update("UPDATE PRODUCT_ENTITY "
                + "SET COST = ?, "
                + "DESCRIPTION = ?, "
                + "CITY = ?, "
                + "HOUSENUMBER = ?, "
                + "POSTCODE = ?, "
                + "STREET = ?, "
                + "SURETY = ?, "
                + "TITLE = ?, "
                + "OWNER_USER_ID = ?, "
                + "STATUS = ?, "
                + "PRICE = ?"
                + " WHERE ID = ?",
                productEntity.getCost(),
                productEntity.getDescription(),
                productEntity.getLocation().getCity(),
                productEntity.getLocation().getHousenumber(),
                productEntity.getLocation().getPostcode(),
                productEntity.getLocation().getStreet(),
                productEntity.getSurety(),
                productEntity.getTitle(),
                productEntity.getOwner().getUserId(),
                productEntity.getStatus().ordinal(),
                productEntity.getPrice(),
                productEntity.getId()
        );
    }

    @Override
    public List<ProductEntity> getAllProductsFromUser(UserEntity user)
            throws EmptyResultDataAccessException{
        String query = "SELECT * FROM PRODUCT_ENTITY WHERE OWNER_USER_ID=" + user.getUserId();
        return (List<ProductEntity>)jdbcTemplate.query(query,
                new Object[]{},
                new ProductEntityRowMapper(userRepository));
    }

    @Override
    public List<ProductEntity> getAllAvailableProducts() {
        String query = "SELECT * FROM PRODUCT_ENTITY WHERE STATUS<>2";
        return jdbcTemplate.query(query, new Object[]{}, new ProductEntityRowMapper(userRepository));
    }
}
