package de.hhu.abschlussprojektverleihplattform.repository;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;

import java.util.List;

public interface IUserRepository {

    UserEntity findById(Long userId);
    void saveUser(UserEntity user);
    int getNumberOfUsers();
    List<UserEntity> getAllUser();
    UserEntity getUserByFirstname(String firstname);

}
