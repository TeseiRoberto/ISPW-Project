package com.rt.ispwproject.beans;

import com.rt.ispwproject.config.UserRole;

// Bean class that represents the session of a user
public class Session {
    final private String username;
    final private String email;
    final private UserRole role;

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
