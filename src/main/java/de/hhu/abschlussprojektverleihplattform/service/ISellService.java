package de.hhu.abschlussprojektverleihplattform.service;

import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;

public interface ISellService {
    public void buyProduct(UserEntity actingUser, ProductEntity product) throws Exception;
}
