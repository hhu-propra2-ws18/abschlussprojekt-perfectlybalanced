package de.hhu.abschlussprojektverleihplattform.service.propay;

import de.hhu.abschlussprojektverleihplattform.model.TransactionEntity;

import java.util.List;

public interface ITransactionService {

    TransactionEntity findById(Long transactionId);

    List<TransactionEntity> getAllTransactionsFromUser(Long userId);

    void addTransaction(TransactionEntity transaction);
}
