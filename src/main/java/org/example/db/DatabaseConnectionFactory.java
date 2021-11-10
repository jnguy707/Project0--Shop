package org.example.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnectionFactory {
    private static Properties properties = new Properties();


    static {
        // static-block : to do class-level initialization once
        // Load connection properties
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/dbinfo.properties");
            properties.load(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Register JDBC Driver
        try {
            Class.forName(properties.getProperty("db.driver"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    public static Connection getConnection() throws SQLException {
        Connection connection;

        // Create DB Connection
        String url = properties.getProperty("db.url");
        String u = properties.getProperty("db.username");
        String p = properties.getProperty("db.password");
        connection = DriverManager.getConnection(url,u,p);

        return connection;
    }
}
