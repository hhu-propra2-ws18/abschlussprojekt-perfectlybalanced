package de.hhu.abschlussprojektverleihplattform.repository;

import de.hhu.abschlussprojektverleihplattform.database.LendingEntityRowMapper;
import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import de.hhu.abschlussprojektverleihplattform.model.Lendingstatus;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Data
@Repository
public class LendingRepository implements ILendingRepository {

    final JdbcTemplate jdbcTemplate;

    final UserRepository userRepository;

    final ProductRepository productRepository;

    @Autowired
    public LendingRepository(JdbcTemplate jdbcTemplate, UserRepository userRepository, ProductRepository productRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void saveLending(LendingEntity lending) {
        jdbcTemplate.update(
                "INSERT INTO LENDING_ENTITY (STATUS, START, END, BORROWER_USER_ID, PRODUCT_ID, COST_RESERVATIONID, SURETY_RESERVATIONID)" +
                        "VALUES (?,?,?,?,?,?,?)",
                Lendingstatus.getLemdingStatusValueFrom(lending.getStatus()),
                lending.getStart(),
                lending.getEnd(),
                lending.getBorrower().getUserId(),
                lending.getProduct().getId(),
                lending.getCostReservationID(),
                lending.getSuretyReservationID());
    }

    @Override
    public List<LendingEntity> getAllLendings() {
        return (List<LendingEntity>)jdbcTemplate.query("SELECT * FROM LENDING_ENTITY",
                new Object[]{},
                new LendingEntityRowMapper(userRepository, productRepository));

    }
}