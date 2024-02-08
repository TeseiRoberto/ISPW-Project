package com.rt.ispwproject.logiccontrollers;

import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.config.SessionManager;
import com.rt.ispwproject.exceptions.DbException;

public class LoginManager {

    public Session login(String username, String password) throws IllegalArgumentException, DbException
    {
        return SessionManager.getInstance().createNewSession(username, password);
    }
}
