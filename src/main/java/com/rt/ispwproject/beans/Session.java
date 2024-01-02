package com.rt.ispwproject.beans;

import com.rt.ispwproject.config.UserRole;

// Bean class that represents the session of a user
public class Session {
    private final String username;
    private final String email;
    private final UserRole role;

    public Session(String username, String email, UserRole role)
    {
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public String getUsername()    { return this.username; }
    public String getEmail()       { return this.email; }
    public UserRole getUserRole()  { return this.role; }
}
