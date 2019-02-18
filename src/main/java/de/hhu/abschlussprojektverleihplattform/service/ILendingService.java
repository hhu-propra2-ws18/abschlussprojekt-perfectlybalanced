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
        ProductEntity
        product,
        Timestamp
        start,
        Timestamp end
    );

    // Accept/Deny a Request
    public boolean acceptLending(LendingEntity lending, boolean RequestIsAccepted);

    // the second Versions from the following Methodes are not jet safe to use,
    // if the Controllers/Views dont need them they get deleted completly

    // Return a Product (two Versions, depending on View/Controller)
    public void returnProduct(LendingEntity lending);
//    public void ReturnProduct(UserEntity actingUser, ProductEntity product);

    // Decide wether a returned product is in acceptable condition or
    // not (two Versions, depending on View/Controller)
    public boolean checkReturnedProduct(LendingEntity lending, boolean isAcceptable);
    // public boolean CheckReturnedProduct(UserEntity actingUser,
    // ProductEntity product, boolean isAcceptable);

    // An admin resolves a conflict and decides who gets the surety
    public boolean resolveConflict(LendingEntity lending, boolean OwnerRecivesSurety);
}