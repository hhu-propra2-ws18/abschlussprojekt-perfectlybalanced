package de.hhu.abschlussprojektverleihplattform.service;

import de.hhu.abschlussprojektverleihplattform.logic.Timespan;
import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;

import java.sql.Timestamp;
import java.util.List;

public interface ILendingService {

    // maybee needs to be changes, once the ZeitraumModel has been implemented
    public List<Timespan> getTime(ProductEntity product);

    // Request a new Lending
    public boolean requestLending(
            UserEntity actingUser,
            ProductEntity product,
            Timestamp start,
            Timestamp end
    );

    // Accept/Deny a Request
    public boolean acceptLendingRequest(LendingEntity lending);

    public void denyLendingRequest(LendingEntity lending);

    // Return a Product
    public void returnProduct(LendingEntity lending);

    // Decide wether a returned product is in acceptable condition or not
    public boolean acceptReturnedProduct(LendingEntity lending);

    public void denyRetunedProduct(LendingEntity lending);


    // An admin resolves a conflict and decides who gets the surety
    public boolean ownerRecivesSurety(LendingEntity lending);

    public boolean borrowerRecivesSurety(LendingEntity lending);

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