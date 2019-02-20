package de.hhu.abschlussprojektverleihplattform.service;

import de.hhu.abschlussprojektverleihplattform.model.Role;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.repository.UserRepository;
import de.hhu.abschlussprojektverleihplattform.security.AuthenticatedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService implements IUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthenticatedUserService authenticatedUserService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void addUser(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);
        userRepository.saveUser(user);
    }

    @Override
    public UserEntity showUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void autoLogin(String username, String plainPassword) {
        UserDetails userDetails = authenticatedUserService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                plainPassword,
                userDetails.getAuthorities());

        Authentication auth = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        if (auth.isAuthenticated()) {
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(auth);
        }
    }


}