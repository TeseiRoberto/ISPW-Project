package com.rt.ispwproject.beans;

import com.rt.ispwproject.config.UserRole;

// Bean class that represents the session of a user
public class Session {

    private final int       userId;
    private final String    username;
    private final String    email;
    private final UserRole  role;


    public Session(int userId, String username, String email, UserRole role) throws IllegalArgumentException
    {
        if(username == null || username.isEmpty())
            throw new IllegalArgumentException("Username cannot be empty!");

        if(email == null || email.isEmpty())
            throw new IllegalArgumentException("Email cannot be empty!");

        if(role == null)
            throw new IllegalArgumentException("User role cannot be empty!");

        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
    }


    // Getters
    public int getUserId()          { return this.userId; }
    public String getUsername()     { return this.username; }
    public String getEmail()        { return this.email; }
    public UserRole getUserRole()   { return this.role; }
}
