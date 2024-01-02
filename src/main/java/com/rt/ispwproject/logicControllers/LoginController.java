package com.rt.ispwproject.logicControllers;

import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.config.UserRole;
import com.rt.ispwproject.dao.ProfileDao;
import com.rt.ispwproject.model.Profile;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    public Session login(String username, String password) throws RuntimeException
    {
        Profile currUser = null;
        ProfileDao dao = new ProfileDao();

        try {
            currUser = dao.getProfile(username, password);
        } catch(IOException | SQLException e)
        {
            throw new RuntimeException(e.getMessage());
        }

        if(currUser == null)
            throw new RuntimeException("User not found");

        return new Session(currUser.getUsername(), currUser.getEmail(), currUser.getUserRole());
    }
}
