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

        // Create callable statement and set up parameters to invoke the login stored procedure
        try (CallableStatement loginProc = connection.prepareCall("call login(?, ?, ?, ?, ?)"))
        {
            loginProc.setString("username_in", username);
            loginProc.setString("password_in", password);
            loginProc.registerOutParameter("userId_out", Types.INTEGER);
            loginProc.registerOutParameter("role_out", Types.VARCHAR);
            loginProc.registerOutParameter("email_out", Types.VARCHAR);

            loginProc.executeQuery();

            int userId = loginProc.getInt(3);                                   // Retrieve output parameters
            String roleAsStr = loginProc.getString(4);
            String email = loginProc.getString(5);

            if (!loginProc.wasNull())                                           // If we got not null output parameters
            {
                UserRole role = UserRole.valueOf(roleAsStr);                // Get user role
                newProfile = new Profile(userId, username, email, role);
            }

        } catch(IllegalArgumentException e)
        {
            throw new DbException("User has been found but cannot recognize his role in the system: " + e.getMessage());
        } catch (SQLException e) {
            throw new DbException("Failed to invoke the \"login\" stored procedure: " + e.getMessage());
        }

        return newProfile;
    }
}
