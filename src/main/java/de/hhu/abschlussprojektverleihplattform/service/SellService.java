package de.hhu.abschlussprojektverleihplattform.service;

import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.Productstatus;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.repository.IProductRepository;
import de.hhu.abschlussprojektverleihplattform.service.propay.IPaymentService;
import org.springframework.stereotype.Service;

@Service
public class SellService implements ISellService {

    private IProductRepository productRepository;
    private IPaymentService paymentService;

    public SellService(IProductRepository productRepository, IPaymentService paymentService) {
        this.productRepository = productRepository;
        this.paymentService = paymentService;
    }

    public void buyProduct(UserEntity actingUser, ProductEntity product) throws Exception {
        if(product.getStatus().equals(Productstatus.forLending)){
            throw new Exception("This Product can only be lend, not bought.");
        }
        if(product.getStatus().equals(Productstatus.sold)){
            throw new Exception("This Product already has been sold.");
        }
        Long userMoney = paymentService.usersCurrentBalance(actingUser.getUsername());
        if (userMoney < product.getPrice()) {
            throw new Exception("The cost and the surety sum up to: "
                    + product.getPrice() + "€, but you only have: " + userMoney + "€.");
        }
        Long paymentID = paymentService.reservateAmount(actingUser.getUsername(), product.getOwner().getUsername(), product.getPrice());
        paymentService.tranferReservatedMoney(actingUser.getUsername(), paymentID);
        product.setStatus(Productstatus.sold);
        productRepository.editProduct(product);
    }
}
