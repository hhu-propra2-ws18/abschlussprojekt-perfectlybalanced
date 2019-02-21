package de.hhu.abschlussprojektverleihplattform.repository;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

import static de.hhu.abschlussprojektverleihplattform.database.DBUtils.psc;


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
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM USER_ENTITY where user_Id=? limit 1",
                    new Object[]{userId},
                    new BeanPropertyRowMapper<>(UserEntity.class));
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public UserEntity findByUsername(String username) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM USER_ENTITY WHERE username=?",
                    new Object[]{username},
                    new BeanPropertyRowMapper<>(UserEntity.class));
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public UserEntity getUserByFirstname(String firstname) {
        return jdbcTemplate.queryForObject("SELECT * FROM USER_ENTITY WHERE firstname=?",
                new Object[]{firstname},
                new BeanPropertyRowMapper<UserEntity>(UserEntity.class));
    }

    @Override
    public UserEntity findByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM USER_ENTITY WHERE email=?",
                    new Object[]{email},
                    new BeanPropertyRowMapper<>(UserEntity.class));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void saveUser(UserEntity user) {
        KeyHolder  keyHolder=new GeneratedKeyHolder();
        jdbcTemplate.update(psc(
                "INSERT INTO USER_ENTITY (FIRSTNAME, LASTNAME, USERNAME, PASSWORD, EMAIL, ROLE)"
                        + "VALUES (?,?,?,?,?,?)",
                user.getFirstname(),
                user.getLastname(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getRole().ordinal()),
                keyHolder
        );

        user.setUserId(keyHolder.getKey().longValue());
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
