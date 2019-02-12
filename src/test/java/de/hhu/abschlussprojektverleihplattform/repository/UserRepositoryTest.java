package de.hhu.abschlussprojektverleihplattform.repository;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserRepositoryTest {

    UserRepository userRepository = new UserRepository();

    @Test
    public void findAllReturnNull(){
        List<UserEntity> testList = userRepository.findAll();
    }

}