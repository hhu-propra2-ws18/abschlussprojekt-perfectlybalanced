package de.hhu.abschlussprojektverleihplattform.repository;

import de.hhu.abschlussprojektverleihplattform.model.TransactionEntity;

import java.util.List;

public interface ITransactionRepository {

    TransactionEntity findById(Long transactionId);

    List<TransactionEntity> getAllTransactions(Long userId);

    void saveTransaction(TransactionEntity transaction);
}
