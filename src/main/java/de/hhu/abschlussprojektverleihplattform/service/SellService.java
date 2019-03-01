package de.hhu.abschlussprojektverleihplattform.service;

import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.Productstatus;
import de.hhu.abschlussprojektverleihplattform.model.TransactionEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.repository.IProductRepository;
import de.hhu.abschlussprojektverleihplattform.repository.ITransactionRepository;
import de.hhu.abschlussprojektverleihplattform.service.propay.interfaces.IPaymentService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class SellService implements ISellService {

    private final IProductRepository productRepository;
    private final IPaymentService paymentService;
    private final ITransactionRepository transactionRepository;

    public SellService(IProductRepository productRepository,
                       IPaymentService paymentService,
                       ITransactionRepository transactionRepository) {
        this.productRepository = productRepository;
        this.paymentService = paymentService;
        this.transactionRepository = transactionRepository;
    }

    public void buyProduct(UserEntity actingUser, ProductEntity product) throws Exception {
        if(product.getStatus().equals(Productstatus.forLending)){
            throw new Exception("Dieses Produkt kann nur geliehen, nicht gekauft werden");
        }
        if(product.getStatus().equals(Productstatus.sold)){
            throw new Exception("Dieses Produkt wurde bereits verkauft.");
        }
        Long userMoney = paymentService.usersCurrentBalance(actingUser.getUsername());
        if (userMoney < product.getPrice()) {
            throw new Exception("Dieses Prddukt kostet: " + product.getPrice()
                    + "€, aber sie haben nur: " + userMoney + "€ auf ihrem Konto.");
        }
        Long paymentID = paymentService.reservateAmount(
            actingUser.getUsername(),
            product.getOwner().getUsername(),
            product.getPrice()
        );
        paymentService.tranferReservatedMoney(actingUser.getUsername(), paymentID);
        product.setStatus(Productstatus.sold);
        productRepository.editProduct(product);

        TransactionEntity transaction = new TransactionEntity(actingUser,
                product.getOwner(),
                product.getPrice(),
                new Timestamp(System.currentTimeMillis()));
        transactionRepository.addTransaction(transaction);
    }
}
