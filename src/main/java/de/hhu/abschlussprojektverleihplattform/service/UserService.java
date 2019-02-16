package de.hhu.abschlussprojektverleihplattform.service;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService implements IUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void addUser(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.saveUser(user);
    }

    @Override
    public UserEntity showUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public UserEntity findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}