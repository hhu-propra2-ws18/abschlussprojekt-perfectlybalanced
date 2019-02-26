package de.hhu.abschlussprojektverleihplattform.database;

import de.hhu.abschlussprojektverleihplattform.model.AddressEntity;
import de.hhu.abschlussprojektverleihplattform.model.Lendingstatus;
import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.ProductStatus;
import de.hhu.abschlussprojektverleihplattform.repository.UserRepository;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductEntityRowMapper implements RowMapper{

    private final UserRepository userRepository;

    public ProductEntityRowMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        ProductEntity product = new ProductEntity();
        AddressEntity location = new AddressEntity();

        int id = rs.findColumn("ID");
        int cost = rs.findColumn("COST");
        int description = rs.findColumn("DESCRIPTION");
        int city = rs.findColumn("CITY");
        int houseNumber = rs.findColumn("HOUSENUMBER");
        int postCode = rs.findColumn("POSTCODE");
        int street = rs.findColumn("STREET");
        int surety = rs.findColumn("SURETY");
        int title = rs.findColumn("TITLE");
        int ownerUserId = rs.findColumn("OWNER_USER_ID");
        int status = rs.findColumn("STATUS");
        int price = rs.findColumn("PRICE");

        location.setCity(rs.getString(city));
        location.setHousenumber(rs.getInt(houseNumber));
        location.setPostcode(rs.getInt(postCode));
        location.setStreet(rs.getString(street));

        product.setId(rs.getLong(id));
        product.setCost(rs.getInt(cost));
        product.setDescription(rs.getString(description));
        product.setSurety(rs.getInt(surety));
        product.setTitle(rs.getString(title));
        product.setOwner(userRepository.findById(rs.getLong(ownerUserId)));
        product.setLocation(location);
        product.setStatus(ProductStatus.values()[rs.getInt(status)]);
        product.setPrice(rs.getInt(price));
        return product;
    }
}
