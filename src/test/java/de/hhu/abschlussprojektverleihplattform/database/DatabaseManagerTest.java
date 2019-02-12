package de.hhu.abschlussprojektverleihplattform.database;

import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManagerTest {

    @Test
    public void connectionTest() throws SQLException {
        Connection testConnection = DatabaseManager.getInstance().getConnection();
    }

}