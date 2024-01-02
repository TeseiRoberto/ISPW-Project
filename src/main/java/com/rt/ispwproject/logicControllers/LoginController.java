package com.rt.ispwproject.logicControllers;

import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.dao.ProfileDao;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.exceptions.UserNotFoundException;
import com.rt.ispwproject.model.Profile;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    public Session login(String username, String password) throws UserNotFoundException, DbException
    {
        Profile currUser;
        ProfileDao dao = new ProfileDao();
        currUser = dao.getProfile(username, password);

        if(currUser == null)
            throw new UserNotFoundException(username);

        return new Session(currUser.getUsername(), currUser.getEmail(), currUser.getUserRole());
    }
}
