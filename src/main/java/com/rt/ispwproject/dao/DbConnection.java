package com.rt.ispwproject.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnection {

    private final Connection dbConnection;
    private static DbConnection instance = null;
    private final static String dbCredentialsFileName = "myHolidayDb.cfg";


    // Load credentials to connect with db and try to connect
    private DbConnection() throws IOException, SQLException
    {
        InputStream credentialsFile = new FileInputStream(dbCredentialsFileName);
        Properties credentials = new Properties();
        credentials.load(credentialsFile);

        String username = credentials.getProperty("USERNAME");
        String password = credentials.getProperty("PASSWORD");
        String url = credentials.getProperty("CONNECTION");

        dbConnection = DriverManager.getConnection(url, username, password);
    }


    public static DbConnection getInstance() throws IOException, SQLException
    {
        if(instance == null)
            instance = new DbConnection();

        return instance;
    }

    public Connection getConnection() { return dbConnection; }
}
