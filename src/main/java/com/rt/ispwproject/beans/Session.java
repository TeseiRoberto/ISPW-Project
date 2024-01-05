package com.rt.ispwproject.beans;

import com.rt.ispwproject.config.UserRole;

// Bean class that represents the session of a user
public class Session {
    private final int userId;
    private final String username;
    private final String email;
    private final UserRole role;

    public Session(int userId, String username, String email, UserRole role)
    {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public int getUserId()          { return this.userId; }
    public String getUsername()     { return this.username; }
    public String getEmail()        { return this.email; }
    public UserRole getUserRole()   { return this.role; }
}
