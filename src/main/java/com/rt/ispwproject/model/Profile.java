package com.rt.ispwproject.model;

import com.rt.ispwproject.config.UserRole;

public class Profile {

    private final int       userId;
    private final String    username;
    private final String    email;
    private final UserRole  role;


    public Profile(int userId, String username, String email, UserRole role)
    {
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

    public boolean isEqual(Profile other) { return this.username.equals(other.getUsername()); }
}
