package de.hhu.abschlussprojektverleihplattform.repository;

import de.hhu.abschlussprojektverleihplattform.database.LendingEntityRowMapper;
import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import de.hhu.abschlussprojektverleihplattform.model.Lendingstatus;
import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
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

    //TODO: saveLanding in Update und Add aufteilen
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
    public void addLending(LendingEntity lending) {
        //make new lending object which is not in the database
        //TODO
    }

    @Override
    public void update(LendingEntity lending) {
        //update a lending entity
        //TODO
    }

    @Override
    public LendingEntity getLendingByProductAndUser(ProductEntity product, UserEntity user) {
        String sql = "SELECT * FROM LENDING_ENTITY WHERE PRODUCT_ID="+product.getId()+" AND BORROWER_USER_ID="+user.getUserId()+";";
        return (LendingEntity) jdbcTemplate.queryForObject(sql,new LendingEntityRowMapper(userRepository,productRepository));
    }

    @Override
    public List<LendingEntity> getAllLendings() {
        return (List<LendingEntity>)jdbcTemplate.query("SELECT * FROM LENDING_ENTITY",
                new Object[]{},
                new LendingEntityRowMapper(userRepository, productRepository));

    }

    @Override
    public List<LendingEntity> getAllLendingsFromProduct(ProductEntity product) {
        return
                (List<LendingEntity>)jdbcTemplate
                        .query("SELECT * FROM LENDING_ENTITY WHERE PRODUCT_ID='"+product.getId()+"';",
                                new Object[]{},
                                new LendingEntityRowMapper(userRepository,productRepository));
    }

    @Override
    public List<LendingEntity> getAllRequestsForUser(UserEntity user) {
        //TODO
        return null;
    }

    @Override
    public List<LendingEntity> getAllLendingsFromUser(UserEntity user) {
        //TODO
        return null;
    }

    @Override
    public List<LendingEntity> getAllLendingsForUser(UserEntity user) {
        //TODO
        return null;
    }

    @Override
    public List<LendingEntity> getReturnedLendingFromUser(UserEntity user) {
        //TODO
        return null;
    }

    @Override
    public List<LendingEntity> getAllConflicts() {
        //TODO
        return null;
    }
}