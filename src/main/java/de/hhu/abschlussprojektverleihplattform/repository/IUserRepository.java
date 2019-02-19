package de.hhu.abschlussprojektverleihplattform.repository;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;

import java.util.List;

public interface IUserRepository {

    UserEntity findById(Long userId);

    UserEntity findByUsername(String username);

    void saveUser(UserEntity user);

    int getNumberOfUsers();

    List<UserEntity> getAllUser();

    UserEntity getUserByFirstname(String firstname);
    
    UserEntity getUserByUsername(String username);
}
