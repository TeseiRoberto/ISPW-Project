package com.rt.ispwproject.dao.profiledao;

import com.rt.ispwproject.config.UserRole;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.model.Profile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ProfileDaoFileSystem implements ProfileDao {

    private static final String DATA_FILENAME = "users.csv";


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
        if(userId == null && username == null)
            throw new DbException("Cannot get profile details, user id and username are both empty");

        int profileId = 0;
        String profileName = "";
        String profileEmail = "";
        String profilePassword = "";
        UserRole profileRole = null;

        String line;
        boolean found = false;

        try (BufferedReader profileReader = new BufferedReader( new FileReader(DATA_FILENAME) ))
        {
            while((line = profileReader.readLine()) != null && !found)
            {
                String[] data = line.split(",");

                profileId = Integer.parseInt(data[0]);
                profileName = data[1];
                profileEmail = data[2];
                profilePassword = data[3];
                profileRole = UserRole.fromPersistenceType(data[4]);

                if(userId != null && userId == profileId)
                {
                    found = true;
                } else if(username != null && username.equals(profileName))
                {
                    if(password != null && !password.equals(profilePassword))
                        break;

                    found = true;
                }
            }

        } catch(IOException | IllegalArgumentException e) {
            throw new DbException("Failed to retrieve profile details, read operation of data file has failed:\n" + e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            throw new DbException("Failed to retrieve profile details: the users data file is corrupted");
        }

        if(!found)
            throw new DbException("User not found");

        return new Profile(profileId, profileName, profileEmail, profileRole);
    }

}
