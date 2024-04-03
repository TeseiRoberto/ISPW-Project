package com.rt.ispwproject.cmdview;

import java.io.IOException;

public class LoginViewCmd extends BaseView {


    public void showTitle()
    {
        print("\n****************************************\n");
        print("*              My Holiday              *\n");
        print("****************************************\n");
    }


    // Reads username from console
    public String getUsername() throws IOException
    {
        String username;
        print("Username: ");
        username = getStringFromUser();

        return username;
    }


    // Reads password from console
    public String getPassword() throws IOException
    {
        String password;
        print("Password: ");
        password = getStringFromUser();

        return password;
    }
}
