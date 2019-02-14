package de.hhu.abschlussprojektverleihplattform.repository;

import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;

import java.util.List;

public interface ILendingRepository {

    void saveLending(LendingEntity lending);
    List<LendingEntity> getAllLendings();
}
