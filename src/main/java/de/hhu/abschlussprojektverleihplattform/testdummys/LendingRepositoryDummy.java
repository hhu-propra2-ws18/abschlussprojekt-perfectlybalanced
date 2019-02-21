package de.hhu.abschlussprojektverleihplattform.testdummys;

import de.hhu.abschlussprojektverleihplattform.repository.ILendingRepository;
import de.hhu.abschlussprojektverleihplattform.service.ILendingService;
import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;

import java.util.ArrayList;
import java.util.List;

public class LendingRepositoryDummy implements ILendingRepository {

    private ArrayList<LendingEntity> lendings;
    private LendingEntity lendigToUpdate;
    private boolean isUpdated;

    private LendingEntity lendingByProductAndUser;

    public LendingRepositoryDummy() {
        lendings = new ArrayList<LendingEntity>();
    }

    @Override
    public void addLending(LendingEntity lending) {
        lendings.add(lending);
    }

    @Override
    public void update(LendingEntity lending) {
        if (lending.getProduct().getTitle().equals(lendigToUpdate.getProduct().getTitle())
                && lending.getEnd().equals(lendigToUpdate.getEnd())) {
            isUpdated = true;
        }
    }

    @Override
    public List<LendingEntity> getAllLendingsFromProduct(ProductEntity product) {
        ArrayList<LendingEntity> ret = new ArrayList<LendingEntity>();
        for (LendingEntity lend : lendings) {
            if (lend.getProduct().getTitle().equals(product.getTitle())) {
                ret.add(lend);
            }
        }
        return ret;
    }

    // Methodes for Testing

    public LendingEntity getFirst() {
        return lendings.get(0);
    }

    public void setLendingToUpdate(LendingEntity lending) {
        lendigToUpdate = lending;
        isUpdated = false;
    }

    public boolean hasBeenUpdated() {
        return isUpdated;
    }

    public void setLendingByProductAndUser(LendingEntity lending) {
        lendingByProductAndUser = lending;
    }

    // Methodes from the Inertface that are not needed for Testing

    @Override
    public List<LendingEntity> getAllLendings() {
        return null;
    }

    @Override
    public List<LendingEntity> getAllRequestsForUser(UserEntity user) {
        return null;
    }

    @Override
    public List<LendingEntity> getAllLendingsFromUser(UserEntity user) {
        return null;
    }

    @Override
    public List<LendingEntity> getAllLendingsForUser(UserEntity user) {
        return null;
    }

    @Override
    public List<LendingEntity> getReturnedLendingFromUser(UserEntity user) {
        return null;
    }

    @Override
    public List<LendingEntity> getAllConflicts() {
        return null;
    }

    @Override
    public LendingEntity getLendingById(Long id) {
        return null;
    }
}
