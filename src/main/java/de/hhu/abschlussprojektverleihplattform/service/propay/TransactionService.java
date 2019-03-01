package de.hhu.abschlussprojektverleihplattform.service.propay;

import de.hhu.abschlussprojektverleihplattform.model.TransactionEntity;
import de.hhu.abschlussprojektverleihplattform.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionService implements ITransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public TransactionEntity findById (Long transactionId) {
        return transactionRepository.findById(transactionId);
    }

    @Override
    public List<TransactionEntity> getAllTransactionsFromUser (Long userId) {
        return transactionRepository.getAllTransactionsFromUser(userId);
    }

    @Override
    public void addTransaction (TransactionEntity transaction) {
        transactionRepository.addTransaction(transaction);
    }

}
