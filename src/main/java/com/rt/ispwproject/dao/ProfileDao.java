package com.rt.ispwproject.dao;

import com.rt.ispwproject.config.UserRole;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.model.Profile;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class ProfileDao {

    // Retrieves profile from db
    public Profile getProfile(String username, String password) throws DbException
    {
        Profile newProfile = null;
        Connection connection = DbConnection.getInstance().getConnection();

        // Create callable statement and set up parameters to call the login stored procedure
        try {
            CallableStatement loginProc = connection.prepareCall("{call login(?, ?, ?, ?)}");

            loginProc.setString("username_in", username);
            loginProc.setString("password_in", password);
            loginProc.registerOutParameter("email_out", Types.VARCHAR);
            loginProc.registerOutParameter("role_out", Types.NUMERIC);

            loginProc.executeQuery();

            int roleNum = loginProc.getInt(3);                  // Retrieve output parameters
            String email = loginProc.getString(4);

            if (!loginProc.wasNull())
                newProfile = new Profile(username, email, UserRole.fromInt(roleNum));

            loginProc.close();
        } catch (SQLException e) {
            throw new DbException("Failed to invoke the login stored procedure");
        }

        return newProfile;
    }
}
