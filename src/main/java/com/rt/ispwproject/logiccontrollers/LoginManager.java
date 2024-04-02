package com.rt.ispwproject.logiccontrollers;

import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.config.SessionManager;
import com.rt.ispwproject.exceptions.DbException;

public class LoginManager {

    // Enables a user to log in the system
    // @username: name of user that is attempting to log in
    // @password: secret string associated to the profile that enables the login
    // @returns: session associated to the profile relative to the given credentials
    public Session login(String username, String password) throws IllegalArgumentException, DbException
    {
        return SessionManager.getInstance().createNewSession(username, password);
    }
}
