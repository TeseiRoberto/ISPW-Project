package com.rt.ispwproject.config;

import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.dao.ProfileDao;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.model.Profile;

import java.util.HashMap;

// The SessionManager is a singleton class responsible for mapping Session (bean) classes to Profile (model) classes.
// In this way we can keep track of the users correctly logged in the system, and we can check that a user
// is actually logged in when he tries to perform any operation in the system.
public class SessionManager {

    private static SessionManager instance = null;
    private final HashMap<Session, Profile> users = new HashMap<>();


    private SessionManager() {}


    public static SessionManager getInstance()
    {
        if(instance == null)
            instance = new SessionManager();

        return instance;
    }


    // Retrieves profile with given credentials from db, builds a session class and associates it to that profile
    public Session createNewSession(String username, String password) throws IllegalArgumentException, DbException
    {
        if(username == null || username.isEmpty())
            throw new IllegalArgumentException("Username cannot be empty");

        if(password == null || password.isEmpty())
            throw new IllegalArgumentException("Password cannot be empty");

        ProfileDao dao = new ProfileDao();
        Profile profile = dao.getProfile(username, password);
        Session newSession = new Session(profile.getUsername(), profile.getUserRole());

        users.put(newSession, profile);
        return newSession;
    }


    // Returns the profile associated to the given session or null if the given session is not valid
    public Profile getProfile(Session session)
    {
        return users.get(session);
    }
}
