package de.hhu.abschlussprojektverleihplattform.repository;

import de.hhu.abschlussprojektverleihplattform.database.LendingEntityRowMapper;
import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import de.hhu.abschlussprojektverleihplattform.model.Lendingstatus;
import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.util.List;

import static de.hhu.abschlussprojektverleihplattform.database.DBUtils.psc;

@Data
@Repository
public class LendingRepository implements ILendingRepository {

    final JdbcTemplate jdbcTemplate;

    final UserRepository userRepository;

    final ProductRepository productRepository;

    @Autowired
    public LendingRepository(
            JdbcTemplate jdbcTemplate,
            UserRepository userRepository,
            ProductRepository productRepository
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void addLending(LendingEntity lending) {
        KeyHolder keyHolder=new GeneratedKeyHolder();
        jdbcTemplate.update(psc(
                "INSERT INTO LENDING_ENTITY "
                        + "(STATUS,"
                        + "START,"
                        + "END,"
                        + "BORROWER_USER_ID,"
                        + "PRODUCT_ID,"
                        + "COST_RESERVATIONID,"
                        + "SURETY_RESERVATIONID)"
                        + "VALUES (?,?,?,?,?,?,?)",
                lending.getStatus().ordinal(),
                lending.getStart(),
                lending.getEnd(),
                lending.getBorrower().getUserId(),
                lending.getProduct().getId(),
                lending.getCostReservationID(),
                lending.getSuretyReservationID()),
                keyHolder
        );

        lending.setId(keyHolder.getKey().longValue());
    }

    @Override
    public void update(LendingEntity lending) {
        String query = "UPDATE LENDING_ENTITY "
                + "SET "
                + "STATUS=?,"
                + "START=?,"
                + "END=?,"
                + "BORROWER_USER_ID=?,"
                + "PRODUCT_ID=?,"
                + "COST_RESERVATIONID=?,"
                + "SURETY_RESERVATIONID=?"
                + " WHERE ID=?";

        jdbcTemplate.update(query,
                lending.getStatus().ordinal(),
                lending.getStart(),
                lending.getEnd(),
                lending.getBorrower().getUserId(),
                lending.getProduct().getId(),
                lending.getCostReservationID(),
                lending.getSuretyReservationID(),
                lending.getId());
    }

    @Override
    public LendingEntity getLendingById(Long id) {
        String query = "SELECT * FROM LENDING_ENTITY WHERE ID=" + id;
        return (LendingEntity) jdbcTemplate.queryForObject(query,
                new LendingEntityRowMapper(userRepository, productRepository));
    }

    public List<LendingEntity> getLendingsByProductAndBorrower(
        ProductEntity product, UserEntity user
    ) {
        String sql = "SELECT * FROM LENDING_ENTITY WHERE PRODUCT_ID=" + product.getId()
                + " AND BORROWER_USER_ID=" + user.getUserId() + ";";

        return (List<LendingEntity>) jdbcTemplate.query(sql,
                new LendingEntityRowMapper(userRepository, productRepository));
    }

    @Override
    public List<LendingEntity> getAllLendings() {
        return (List<LendingEntity>) jdbcTemplate.query("SELECT * FROM LENDING_ENTITY",
                new Object[]{},
                new LendingEntityRowMapper(userRepository, productRepository));

    }

    @Override
    public List<LendingEntity> getAllLendingsFromProduct(ProductEntity product) {
        return
                (List<LendingEntity>) jdbcTemplate
                        .query("SELECT * FROM LENDING_ENTITY WHERE PRODUCT_ID='"
                                        + product.getId() + "';",
                                new Object[]{},
                                new LendingEntityRowMapper(userRepository, productRepository));
    }

    @Override
    public List<LendingEntity> getAllLendingRequestsForProductOwner(UserEntity user) {
        String query2 = "select * from product_entity p "
                + "join lending_entity l on p.id = l.product_id"
                + " where p.owner_user_id =" + user.getUserId()
                + " and l.status=" + Lendingstatus.requested.ordinal();


        return (List<LendingEntity>) jdbcTemplate.query(query2,
                new Object[]{},
                new LendingEntityRowMapper(userRepository, productRepository));
    }

    @Override
    public List<LendingEntity> getAllLendingsFromUser(UserEntity user) {
        String query = "SELECT * FROM LENDING_ENTITY l WHERE EXISTS "
                + "(SELECT p.id FROM PRODUCT_ENTITY p WHERE l.product_id = p.id "
                + "AND p.OWNER_USER_ID=" + user.getUserId() + ")";

        return (List<LendingEntity>) jdbcTemplate.query(query,
                new Object[]{},
                new LendingEntityRowMapper(userRepository, productRepository));
    }

    @Override
    public List<LendingEntity> getAllLendingsForUser(UserEntity user) {
        String query = "SELECT * FROM LENDING_ENTITY WHERE BORROWER_USER_ID="
                + user.getUserId();
        return
                (List<LendingEntity>) jdbcTemplate
                        .query(query,
                                new Object[]{},
                                new LendingEntityRowMapper(userRepository, productRepository));
    }

    @Override
    public List<LendingEntity> getReturnedLendingFromUser(UserEntity user) {
        String query = "SELECT * FROM LENDING_ENTITY l WHERE "
                + "(EXISTS (SELECT p.id FROM PRODUCT_ENTITY p WHERE l.product_id = p.id "
                + "AND p.OWNER_USER_ID=" + user.getUserId() + ")"
                + "AND STATUS=" + Lendingstatus.returned.ordinal() + ")";

        return (List<LendingEntity>) jdbcTemplate.query(query,
                new Object[]{},
                new LendingEntityRowMapper(userRepository, productRepository));
    }

    @Override
    public List<LendingEntity> getAllConflicts() {
        return
                (List<LendingEntity>) jdbcTemplate
                        .query("SELECT * FROM LENDING_ENTITY WHERE STATUS='"
                                        + Lendingstatus.conflict.ordinal() + "';",
                                new Object[]{},
                                new LendingEntityRowMapper(userRepository, productRepository));
    }
}
