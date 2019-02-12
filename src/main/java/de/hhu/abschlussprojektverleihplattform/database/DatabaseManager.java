package de.hhu.abschlussprojektverleihplattform.database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static DatabaseManager instance = null;

    private DatabaseManager(){
        try {
            Class.forName("org.h2.Driver");
            createUserTable();
            createProductTable();
            createAdressTable();
            createLendinTable();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
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

    private void createUserTable() throws SQLException{
            Connection connection = DatabaseManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            String sqlQuery = "CREATE TABLE user " +
                    "(id LONG not NULL AUTO_INCREMENT, " +
                    "firstName VARCHAR(255), " +
                    "lastName VARCHAR(255), " +
                    "userName VARCHAR(255)," +
                    "password VARCHAR(255), " +
                    "mail VARCHAR(255)," +
                    " PRIMARY KEY ( id ))";

            statement.execute(sqlQuery);
            connection.close();
    }

    private void createProductTable() throws  SQLException{
            Connection connection = DatabaseManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            String sqlQuery = "CREATE TABLE product " +
                    "(id LONG not NULL AUTO_INCREMENT, " +
                    "description VARCHAR(255), " +
                    "title VARCHAR(255), " +
                    "surety INTEGER ," +
                    "cost INTEGER , " +
                    "ownerId LONG" +
                    "locationId" +
                    " PRIMARY KEY ( id ))";

            statement.execute(sqlQuery);
            connection.close();
    }

    private void createAdressTable() throws SQLException{
            Connection connection = DatabaseManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            String sqlQuery = "CREATE TABLE adress " +
                    "(id LONG not NULL AUTO_INCREMENT, " +
                    "street VARCHAR(255), " +
                    "housenumber INTEGER , " +
                    "postcode INTEGER ," +
                    "city VARCHAR(255) , " +
                    " PRIMARY KEY ( id ))";

            statement.execute(sqlQuery);
            connection.close();
    }

    private void createLendinTable() throws SQLException{
            Connection connection = DatabaseManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            String sqlQuery = "CREATE TABLE lending " +
                    "(id LONG not NULL AUTO_INCREMENT, " +
                    "productId LONG, " +
                    "customerId LONG , " +
                    " PRIMARY KEY ( id ))";

            statement.execute(sqlQuery);
            connection.close();
    }

}
