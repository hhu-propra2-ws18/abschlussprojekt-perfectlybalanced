package de.hhu.abschlussprojektverleihplattform.service;

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

}
