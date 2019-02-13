package de.hhu.abschlussprojektverleihplattform.logic;

import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;

import java.util.List;

public interface Lending_Service {

    void addLending(LendingEntity lending);

    void update(LendingEntity lending);

    LendingEntity getLendingByProductAndUser(ProductEntity product, UserEntity user);

    List<LendingEntity> getAllLendingsFromProduct(ProductEntity product);

    // return all Lendings, that are owned by the user and have the status requested
    List<LendingEntity> getAllRequestsForUser(UserEntity user);

    // return all Lendings, that are owned by the user
    List<LendingEntity> getAllLendingsFromUser(UserEntity user);

    // return all Lendings, that are borrowed by the user
    List<LendingEntity> getAllLendingsForUser(UserEntity user);

    // return all Lendings, that are owned by the user and have the status returned
    List<LendingEntity> getReturnedLendingFromUser(UserEntity user);

    // return all Lendings, that have the status conflict
    List<LendingEntity> getAllConflicts();
}
