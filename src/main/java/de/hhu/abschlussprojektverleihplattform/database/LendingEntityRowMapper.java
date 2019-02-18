package de.hhu.abschlussprojektverleihplattform.database;

import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import de.hhu.abschlussprojektverleihplattform.model.Lendingstatus;
import de.hhu.abschlussprojektverleihplattform.repository.ProductRepository;
import de.hhu.abschlussprojektverleihplattform.repository.UserRepository;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LendingEntityRowMapper implements RowMapper {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public LendingEntityRowMapper(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        LendingEntity lendingEntity=new LendingEntity();

        int id = rs.findColumn("ID");
        int costReservationId = rs.findColumn("COST_RESERVATIONID");
        int endTime = rs.findColumn("END");
        int startTime = rs.findColumn("START");
        int status = rs.findColumn("STATUS");
        int suretyId = rs.findColumn("SURETY_RESERVATIONID");
        int borrowerUserId  = rs.findColumn("BORROWER_USER_ID");
        int productId = rs.findColumn("PRODUCT_ID");



        lendingEntity.setId(rs.getLong(id));
        lendingEntity.setCostReservationID((long) costReservationId);
        lendingEntity.setEnd(rs.getTimestamp(endTime));
        lendingEntity.setStart(rs.getTimestamp(startTime));
        lendingEntity.setStatus(Lendingstatus.values()[status-1]);
        lendingEntity.setSuretyReservationID((long) suretyId);
        lendingEntity.setBorrower(userRepository.findById(rs.getLong(borrowerUserId)));
        lendingEntity.setProduct(productRepository.getProductById(rs.getLong(productId)));

        return lendingEntity;
    }
}
