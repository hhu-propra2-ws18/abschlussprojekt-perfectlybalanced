package de.hhu.abschlussprojektverleihplattform.repository;

import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import de.hhu.abschlussprojektverleihplattform.model.Lendingstatus;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Data
@Repository
public class LendingRepository implements ILendingRepository {

    final JdbcTemplate jdbcTemplate;

    @Autowired
    public LendingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    UserRepository userRepository;

    @Override
    public void saveLending(LendingEntity lending) {
        jdbcTemplate.update(
                "INSERT INTO LENDING_ENTITY (STATUS, START, END, BORROWER_USER_ID, PRODUCT_ID, COST_RESERVATIONID, SURETY_RESERVATIONID)" +
                        "VALUES (?,?,?,?,?,?,?)",
                lending.getValueFromLendingStatus(lending.getStatus()),
                lending.getStart(),
                lending.getEnd(),
                lending.getBorrower().getUserId(),
                lending.getProduct().getId(),
                lending.getCostReservationID(),
                lending.getSuretyReservationID());
    }

    @Override
    public List<LendingEntity> getAllLendings() {
        return jdbcTemplate.query("SELECT * FROM LENDING_ENTITY", new RowMapper<LendingEntity>() {
            //ArrayList<LendingEntity> results=new ArrayList<>();
            @Override
            public LendingEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
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
                lendingEntity.setStatus(Lendingstatus.valueOf(status));
                lendingEntity.setSuretyReservationID((long) suretyId);
                lendingEntity.setBorrower(userRepository.findById(rs.getLong(borrowerUserId)));

                return lendingEntity;
            }
        });
    }
}