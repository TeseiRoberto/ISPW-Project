package com.rt.ispwproject.config;

import com.rt.ispwproject.exceptions.DbException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnection {

    private final Connection connection;
    private static DbConnection instance = null;
    private static final String DB_CREDENTIALS_FILE_NAME = "myHolidayDb.cfg";


    // Load credentials to connect with db and try to connect
    private DbConnection() throws DbException
    {
        try (InputStream credentialsFile = new FileInputStream(DB_CREDENTIALS_FILE_NAME))
        {
            Properties credentials = new Properties();
            credentials.load(credentialsFile);

            String username = credentials.getProperty("USERNAME");
            String password = credentials.getProperty("PASSWORD");
            String url = credentials.getProperty("CONNECTION");

            connection = DriverManager.getConnection(url, username, password);
        } catch (IOException e) {
            throw new DbException("Cannot connect to db, " + DB_CREDENTIALS_FILE_NAME + " not found");
        } catch (SQLException e) {
            throw new DbException("Db error: " + e.getMessage());
        }
    }


    public static DbConnection getInstance() throws DbException
    {
        if(instance == null)
            instance = new DbConnection();

        return instance;
    }

    public Connection getConnection() { return connection; }
}
