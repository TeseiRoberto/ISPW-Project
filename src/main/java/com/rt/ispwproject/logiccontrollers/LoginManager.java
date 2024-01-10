package com.rt.ispwproject.logiccontrollers;

import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.dao.ProfileDao;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.model.Profile;

public class LoginManager {

    public Session login(String username, String password) throws RuntimeException, DbException
    {
        if(username == null || username.isEmpty())
            throw new RuntimeException("Username cannot be empty");

        if(password == null || password.isEmpty())
            throw new RuntimeException("Password cannot be empty");

        Profile currUser;
        ProfileDao dao = new ProfileDao();
        currUser = dao.getProfile(username, password);

        if(currUser == null)
            throw new RuntimeException("User not found");

        return new Session(currUser.getUserId(), currUser.getUsername(), currUser.getEmail(), currUser.getUserRole());
    }
}
