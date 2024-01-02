package com.rt.ispwproject.model;

import com.rt.ispwproject.config.UserRole;

public class Profile {
    private final String username;
    private final String email;
    private final UserRole role;

    public Profile(String username, String email, UserRole role)
    {
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public String getUsername()    { return this.username; }
    public String getEmail()       { return this.email; }
    public UserRole getUserRole()  { return this.role; }
}
