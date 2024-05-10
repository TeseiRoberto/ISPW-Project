package com.rt.ispwproject.dao.profiledao;

import com.rt.ispwproject.config.DbConnection;
import com.rt.ispwproject.config.UserRole;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.model.Profile;

import java.sql.*;

public class ProfileDaoMysql implements ProfileDao {


    // Retrieves profile associated to the given credentials from db
    public Profile getProfile(String username, String password) throws DbException
    {
        return getProfileDetails(null, username, password);
    }


    // Retrieves profile associated to the given id from db
    public Profile getProfileById(int userId) throws DbException
    {
        return getProfileDetails(userId, null, null);
    }


    // Retrieves profile associated to the given username from db
    public Profile getProfileByUsername(String username) throws DbException
    {
        return getProfileDetails(null, username, null);
    }


    // General purpose method to get profile details
    private Profile getProfileDetails(Integer userId, String username, String password) throws DbException
    {
        Profile profile = null;
        Connection connection = DbConnection.getInstance().getConnection();

        try (CallableStatement getUserProc = connection.prepareCall("call getUserDetails(?, ?, ?, ?, ?, ?, ?)"))
        {
            getUserProc.setNull("userId_in", Types.INTEGER);
            getUserProc.setNull("username_in", Types.VARCHAR);
            getUserProc.setNull("password_in", Types.VARCHAR);

            getUserProc.registerOutParameter("userId_out", Types.INTEGER);
            getUserProc.registerOutParameter("username_out", Types.VARCHAR);
            getUserProc.registerOutParameter("email_out", Types.VARCHAR);
            getUserProc.registerOutParameter("userRole_out", Types.VARCHAR);

            // Determine which parameters between the given ones are to be used to retrieve profile from db
            if(userId == null)
            {
                if(username == null || username.isEmpty())
                    throw new DbException("Cannot get profile details, user id and username are both empty");

                getUserProc.setString("username_in", username);
                if(password != null && !password.isEmpty())
                    getUserProc.setString("password_in", password);

            } else {
                getUserProc.setInt("userId_in", userId);
            }

            // execute stored procedure, retrieve output parameters and construct Profile instance
            getUserProc.execute();
            int idOut = getUserProc.getInt("userId_out");
            String usernameOut = getUserProc.getString("username_out");
            String emailOut = getUserProc.getString("email_out");
            String roleOut = getUserProc.getString("userRole_out");

            if (!getUserProc.wasNull())                                       // If we got not null output parameters
                profile = new Profile(idOut, usernameOut, emailOut, UserRole.fromPersistenceType(roleOut));
            else
                throw new DbException("User not found");

        } catch(SQLException e)
        {
            throw new DbException("Failed to invoke the \"getUserDetails\" stored procedure:\n\"" + e.getMessage());
        } catch (IllegalArgumentException e)
        {
            throw new DbException("Failed to invoke the \"getUserDetails\" stored procedure:\nCannot determine user role");
        }

        return profile;
    }

}
