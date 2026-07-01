package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static final String URL = "jdbc:postgre://localhost:5432/postgres"; //Non so perchè non mi va dopo prova un p' anche tu 
    //settati il db con questo user e pw 
    private static final String USER = "postgres";
    private static final String PASSWORD = "password"; // Password del database

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}