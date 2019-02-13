package de.hhu.abschlussprojektverleihplattform.repository;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Data
@Repository
public class UserRepository implements IUserRepository{

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public UserEntity findById(Long userId) {
        return jdbcTemplate.queryForObject("SELECT * FROM USER_ENTITY where user_Id=?",
                                            new Object[] { userId },
                                            new BeanPropertyRowMapper<UserEntity>(UserEntity.class));
    }

    @Override
    public void saveUser(UserEntity user) {
        jdbcTemplate.update(
                "INSERT INTO USER_ENTITY (FIRSTNAME, LASTNAME, USERNAME, PASSWORD, EMAIL)" +
                        "VALUES (?,?,?,?,?)",
                        new Object[]{user.getFirstname(),
                                    user.getLastname(),
                                    user.getUsername(),
                                    user.getPassword(),
                                    user.getEmail()}
        );
    }

    @Override
    public int getNumberOfUsers() {
        return jdbcTemplate.queryForObject("select count (*) from USER_ENTITY", Integer.class);
    }

    @Override
    public List<UserEntity> alleUser() {
        return null;
    }
}
