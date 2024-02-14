package com.rt.ispwproject.dao;

import com.rt.ispwproject.config.UserRole;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.model.Profile;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class ProfileDao {

    // Retrieves profile associated to given credentials from db
    public Profile getProfile(String username, String password) throws DbException
    {
        Profile newProfile = null;
        Connection connection = DbConnection.getInstance().getConnection();

        // Create callable statement and set up parameters to invoke the login stored procedure
        try (CallableStatement loginProc = connection.prepareCall("call getUserByCredentials(?, ?, ?, ?, ?)"))
        {
            loginProc.setString("username_in", username);
            loginProc.setString("password_in", password);
            loginProc.registerOutParameter("userId_out", Types.INTEGER);
            loginProc.registerOutParameter("role_out", Types.VARCHAR);
            loginProc.registerOutParameter("email_out", Types.VARCHAR);

            loginProc.executeQuery();

            // Retrieve output parameters
            int userId = loginProc.getInt("userId_out");
            String roleAsStr = loginProc.getString("role_out");
            String email = loginProc.getString("email_out");

            if (!loginProc.wasNull())                                       // If we got not null output parameters
            {
                UserRole role = UserRole.valueOf(roleAsStr);                // Get user role
                newProfile = new Profile(userId, username, email, role);
            }

        } catch(IllegalArgumentException e)
        {
            throw new DbException("User has been found but cannot recognize his role in the system: " + e.getMessage());
        } catch (SQLException e) {
            throw new DbException("Failed to invoke the \"getUserByCredentials\" stored procedure: " + e.getMessage());
        }

        if(newProfile == null)
            throw new DbException("User \"" + username + "\" not found");

        return newProfile;
    }


    // Retrieves profile associated to given identifier from db
    public Profile getProfile(int userId) throws DbException
    {
        Profile newProfile = null;
        Connection connection = DbConnection.getInstance().getConnection();

        // Create callable statement and set up parameters to invoke the login stored procedure
        try (CallableStatement loginProc = connection.prepareCall("call getUserById(?, ?, ?, ?)"))
        {
            loginProc.setInt("userId_in", userId);
            loginProc.registerOutParameter("username_out", Types.INTEGER);
            loginProc.registerOutParameter("role_out", Types.VARCHAR);
            loginProc.registerOutParameter("email_out", Types.VARCHAR);

            loginProc.executeQuery();

            // Retrieve output parameters
            String username = loginProc.getString("username_out");
            String roleAsStr = loginProc.getString("role_out");
            String email = loginProc.getString("email_out");

            if (!loginProc.wasNull())                                       // If we got not null output parameters
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

        if(newProfile == null)
            throw new DbException("User associated to id \"" + userId + "\" not found");

        return newProfile;
    }
}
