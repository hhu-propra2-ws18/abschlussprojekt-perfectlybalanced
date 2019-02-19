package de.hhu.abschlussprojektverleihplattform.service;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;

public interface IUserService {
    void addUser(UserEntity userEntity);

    UserEntity showUserById(Long userId);
    
    UserEntity findByUsername(String username);

    void autoLogin(String username, String plainPassword);
}
