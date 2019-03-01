package de.hhu.abschlussprojektverleihplattform.service;

import de.hhu.abschlussprojektverleihplattform.logic.Timespan;
import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;

import java.sql.Timestamp;
import java.util.List;

public interface ILendingService {

    List<Timespan> getAvailableTime(ProductEntity product);

    // Request a new Lending
    LendingEntity requestLending(
            UserEntity actingUser,
            ProductEntity product,
            Timestamp start,
            Timestamp end
    ) throws Exception;

    // Accept/Deny a Request
    void acceptLendingRequest(LendingEntity lending) throws Exception;

    void denyLendingRequest(LendingEntity lending) throws Exception;

    // Return a Product
    void returnProduct(LendingEntity lending)throws Exception;

    // Decide wether a returned product is in acceptable condition or not
    void acceptReturnedProduct(LendingEntity lending) throws Exception;

    void denyReturnedProduct(LendingEntity lending) throws Exception;


    // An admin resolves a conflict and decides who gets the surety
    void ownerReceivesSuretyAfterConflict(LendingEntity lending) throws Exception;

    void borrowerReceivesSuretyAfterConflict(LendingEntity lending) throws Exception;

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

    // return all Lendings in the Database
    List<LendingEntity> getAllLendings();

    LendingEntity getLendingById(Long id) throws Exception;

    //Sorting methodes for the MyLendingsView

    List<LendingEntity> getAllRequestedLendings(List<LendingEntity> allLendings);

    List<LendingEntity> getAllConfirmedLendings(List<LendingEntity> allLendings);

    List<LendingEntity> getAllReturnedLendings(List<LendingEntity> allLendings);

    List<LendingEntity> getAllConflictedLendings(List<LendingEntity> allLendings);

    //Done and Denied Lendings, scine bothe are just history and have no further interactions
    List<LendingEntity> getAllCompletedLendings(List<LendingEntity> allLendings);

    List<LendingEntity> getAllReminder(List<LendingEntity> allLendings);
}