package de.hhu.abschlussprojektverleihplattform.repository;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;

import java.util.List;

public interface IUserRepository {

    List<UserEntity> findAll();
    void addUserToDatabase(UserEntity user);

}
