package de.hhu.abschlussprojektverleihplattform.service;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import org.springframework.dao.EmptyResultDataAccessException;

public interface IUserService {
    void addUser(UserEntity userEntity);

    UserEntity showUserById(Long userId);
    
    UserEntity findByUsername(String username) throws EmptyResultDataAccessException;

    UserEntity findByEmail(String email);

    void autoLogin(String username, String plainPassword);

}
