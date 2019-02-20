package de.hhu.abschlussprojektverleihplattform.service;

import de.hhu.abschlussprojektverleihplattform.logic.TempZeitraumModel;
import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;

import java.sql.Timestamp;
import java.util.List;

public interface ILendingService {

    // maybee needs to be changes, once the ZeitraumModel has been implemented
    public TempZeitraumModel getTime(ProductEntity product);

    // Request a new Lending
    public boolean requestLending(
            UserEntity actingUser,
            ProductEntity product,
            Timestamp start,
            Timestamp end
    );

    // Accept/Deny a Request
    public boolean acceptLendingRequest(LendingEntity lending);

    public boolean denyLendingRequest(LendingEntity lending);

    // the second Versions from the following Methodes are not jet safe to use,
    // if the Controllers/Views dont need them they get deleted completly

    // Return a Product (two Versions, depending on View/Controller)
    public void returnProduct(LendingEntity lending);
    // public void ReturnProduct(UserEntity actingUser, ProductEntity product);

    // Decide wether a returned product is in acceptable condition or
    // not (two Versions, depending on View/Controller)
    public boolean checkReturnedProduct(LendingEntity lending, boolean isAcceptable);
    // public boolean checkReturnedProduct(UserEntity actingUser,
    // ProductEntity product, boolean isAcceptable);

    // An admin resolves a conflict and decides who gets the surety
    public boolean resolveConflict(LendingEntity lending, boolean ownerRecivesSurety);

    // The following Methodes are just for the views,
    // calling them doesnt change anything in the database

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

    LendingEntity getLendingById(Long id);
}