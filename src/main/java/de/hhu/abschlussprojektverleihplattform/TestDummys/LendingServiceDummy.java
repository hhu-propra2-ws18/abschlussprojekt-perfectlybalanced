package de.hhu.abschlussprojektverleihplattform.TestDummys;

import de.hhu.abschlussprojektverleihplattform.logic.ILending;
import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;

import java.util.ArrayList;
import java.util.List;

public class LendingServiceDummy implements ILending {

    private ArrayList<LendingEntity> lendings;

    public LendingServiceDummy() {
        lendings = new ArrayList<>();
    }

    @Override
    public void addLending(LendingEntity lending) {
        lendings.add(lending);
    }

    @Override
    public void update(LendingEntity lending) {

    }

    @Override
    public LendingEntity getLendingByProductAndUser(ProductEntity product, UserEntity user) {
        return null;
    }

    @Override
    public List<LendingEntity> getAllLendingsFromProduct(ProductEntity product) {
        ArrayList<LendingEntity> ret = new ArrayList<>();
        for (LendingEntity lend: lendings) {
            if(lend.getProduct().getTitle().equals(product.getTitle())) {
                ret.add(lend);
            }
        }
        return ret;
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

    public LendingEntity getFirst() {
        return lendings.get(0);
    }
}
