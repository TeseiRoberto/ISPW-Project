package com.rt.ispwproject.beans;

import com.rt.ispwproject.config.UserRole;

// Bean class that represents the session of a user
public class Session {

    private final String    username;
    private final UserRole  role;


    public Session(String username, UserRole role) throws IllegalArgumentException
    {
        if(username == null || username.isEmpty())
            throw new IllegalArgumentException("Username cannot be empty!");

        if(role == null)
            throw new IllegalArgumentException("User role cannot be empty!");

        this.username = username;
        this.role = role;
    }


    // Getters
    public String getUsername()     { return this.username; }
    public UserRole getUserRole()   { return this.role; }
}
