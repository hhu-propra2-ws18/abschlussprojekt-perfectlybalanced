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
public class UserRepository implements IUserRepository {

    final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserEntity findById(Long userId) {
        return jdbcTemplate.queryForObject("SELECT * FROM USER_ENTITY where user_Id=?",
                new Object[]{userId},
                new BeanPropertyRowMapper<>(UserEntity.class));
    }

    @Override
    public UserEntity findByUsername(String username) {
        return jdbcTemplate.queryForObject("SELECT * FROM USER_ENTITY WHERE username=?",
                new Object[]{username},
                new BeanPropertyRowMapper<>(UserEntity.class));
    }

    @Override
    public UserEntity getUserByFirstname(String firstname) {
        return jdbcTemplate.queryForObject("SELECT * FROM USER_ENTITY WHERE firstname=?",
                new Object[]{firstname},
                new BeanPropertyRowMapper<UserEntity>(UserEntity.class));
    }

    @Override
    public void saveUser(UserEntity user) {
        jdbcTemplate.update(
                "INSERT INTO USER_ENTITY (FIRSTNAME, LASTNAME, USERNAME, PASSWORD, EMAIL, ROLE)"
                        + "VALUES (?,?,?,?,?,?)",
                user.getFirstname(),
                user.getLastname(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getRole().ordinal()
        );
    }

    @Override
    public int getNumberOfUsers() {
        return jdbcTemplate.queryForObject("select count (*) from USER_ENTITY", Integer.class);
    }

    @Override
    public List<UserEntity> getAllUser() {
        return jdbcTemplate.query(
                "SELECT * FROM USER_ENTITY",
                new BeanPropertyRowMapper<>(UserEntity.class)
        );
    }
}
