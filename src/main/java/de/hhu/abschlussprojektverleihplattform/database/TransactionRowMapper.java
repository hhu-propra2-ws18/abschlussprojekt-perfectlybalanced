package de.hhu.abschlussprojektverleihplattform.database;

import de.hhu.abschlussprojektverleihplattform.model.TransactionEntity;
import de.hhu.abschlussprojektverleihplattform.repository.UserRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionRowMapper implements RowMapper {

    private final UserRepository userRepository;
     

    public TransactionRowMapper(@NonNull UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        TransactionEntity transaction = new TransactionEntity();

        int id = rs.findColumn("ID");
        int senderId = rs.findColumn("SENDER_ID");
        int receiverId = rs.findColumn("RECIEVER_ID");
        int amount = rs.findColumn("AMOUNT");
        int date = rs.findColumn("DATE");

        transaction.setId(rs.getLong(id));
        transaction.setSender(userRepository.findById(rs.getLong(senderId)));
        transaction.setReceiver(userRepository.findById(rs.getLong(receiverId)));
        transaction.setAmount(rs.getInt(amount));
        transaction.setDate(rs.getTimestamp(date));

        return transaction;
    }
}
