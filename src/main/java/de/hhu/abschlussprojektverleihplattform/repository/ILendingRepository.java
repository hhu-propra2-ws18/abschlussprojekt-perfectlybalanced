package de.hhu.abschlussprojektverleihplattform.repository;

import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;

import java.util.List;

public interface ILendingRepository {


    // Mit lendings owned by user meine ich, das der user der owner des Produktes ist, welches verliehen wird


    void addLending(LendingEntity lending);

    void update(LendingEntity lending);

    // hier weis ich noch nicht was genau ich brauche, event brauche ich sie garnicht
    LendingEntity getLendingByProductAndUser(ProductEntity product, UserEntity user);

    List<LendingEntity> getAllLendings();

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
