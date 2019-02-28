package de.hhu.abschlussprojektverleihplattform.repository;

import de.hhu.abschlussprojektverleihplattform.database.TransactionRowMapper;
import de.hhu.abschlussprojektverleihplattform.model.TransactionEntity;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TransactionRepository implements ITransactionRepository {

    final JdbcTemplate jdbcTemplate;

    final UserRepository userRepository;

    @Autowired
    public TransactionRepository(
        JdbcTemplate jdbcTemplate,
        UserRepository userRepository
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
    }

    @Override
    public TransactionEntity findById(Long transactionId) throws EmptyResultDataAccessException {
        String sql = "SELECT * FROM TRANSACTION_ENTITY WHERE ID = ? limit 1";
        return (TransactionEntity) jdbcTemplate.queryForObject(sql,
                new Object[]{transactionId},
                new TransactionRowMapper(userRepository)
        );
    }

    @Override
    public List<TransactionEntity> getAllTransactionsFromUser(Long userId){
        String sql ="SELECT * FROM TRANSACTION_ENTITY WHERE RECEIVER_USER_ID = "+ userId
                + "UNION (SELECT * FROM TRANSACTION_ENTITY WHERE SENDER_USER_ID = " + userId +");";
        return (List<TransactionEntity>) jdbcTemplate.query(sql,
                new TransactionRowMapper(userRepository));
    }

    @SuppressFBWarnings(justification="nullpointer exception")
    @Override
    public void addTransaction(TransactionEntity transaction){
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc(
            "INSERT INTO TRANSACTION_ENTITY ("
                + "AMOUNT,"
                + "DATE,"
                + "RECEIVER_USER_ID,"
                + "SENDER_USER_ID)"
                + "VALUES(?,?,?,?)",
            transaction.getAmount(),
            transaction.getDate(),
            transaction.getReceiver().getUserId(),
            transaction.getSender().getUserId()),
            keyHolder
        );
        transaction.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
    }
}
