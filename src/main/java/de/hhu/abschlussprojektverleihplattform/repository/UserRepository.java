package de.hhu.abschlussprojektverleihplattform.repository;

import de.hhu.abschlussprojektverleihplattform.database.DatabaseManager;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserRepository implements IUserRepository{

    @Override
    public List<UserEntity> findAll() {
        return null;
    }

    @Override
    public void addUserToDatabase(UserEntity benutzer) {
        try {
            Connection connection = DatabaseManager.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
