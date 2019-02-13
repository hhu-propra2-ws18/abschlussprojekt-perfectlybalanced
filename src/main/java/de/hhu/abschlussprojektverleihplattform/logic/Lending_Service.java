package de.hhu.abschlussprojektverleihplattform.logic;

import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;

import java.util.List;

public interface Lending_Service {
    List<LendingEntity> getAllLendingsFromProduct(ProductEntity product);

    void addLending(LendingEntity lending);

    void update(LendingEntity lending);

    LendingEntity getLendingByProductAndUser(ProductEntity product, UserEntity user);
}
