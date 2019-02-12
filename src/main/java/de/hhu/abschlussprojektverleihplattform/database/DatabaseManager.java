package de.hhu.abschlussprojektverleihplattform.database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static DatabaseManager instance = null;

    private DatabaseManager(){
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseManager getInstance(){
        if (instance == null){
            DatabaseManager.instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        String username = "perfectlyBalanced";
        String password = "perfectlyBalanced";
        return DriverManager.getConnection("jdbc:h2:~/test", username, password);
    }



}
