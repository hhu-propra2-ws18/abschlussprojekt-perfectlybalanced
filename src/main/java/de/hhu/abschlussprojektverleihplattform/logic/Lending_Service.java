package de.hhu.abschlussprojektverleihplattform.logic;

import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;

import java.util.List;

public interface Lending_Service {
    List<LendingEntity> getAllLendingsFromProduct(ProductEntity product);

    void addLending(LendingEntity lending);

    void Update(LendingEntity lending);

}
