package com.rt.ispwproject.dao;

import com.rt.ispwproject.config.UserRole;
import com.rt.ispwproject.model.Profile;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class ProfileDao {

    // Retrieves profile from db
    public Profile getProfile(String username, String password) throws IOException, SQLException
    {
        Connection connection = DbConnection.getInstance().getConnection();
        CallableStatement loginProc = connection.prepareCall("{call login(?, ?, ?, ?)}");

        // Set up parameters for stored procedure call
        loginProc.setString("username_in", username);
        loginProc.setString("password_in", password);
        loginProc.registerOutParameter("email_out", Types.VARCHAR);
        loginProc.registerOutParameter("role_out", Types.NUMERIC);

        loginProc.executeQuery();

        int roleNum = loginProc.getInt(3);                  // Retrieve output parameters
        String email = loginProc.getString(4);

        if(loginProc.wasNull())
            return null;

        return new Profile(username, email, UserRole.fromInt(roleNum));
    }
}
