package com.rt.ispwproject.cmdview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoginViewCmd {

    public void showWelcome()
    {
        System.out.println("****************************************");
        System.out.println("*              My Holiday              *");
        System.out.println("****************************************");
    }


    // Reads username from console
    public String getUsername() throws IOException
    {
        String username;
        BufferedReader reader = new BufferedReader( new InputStreamReader(System.in) );

        System.out.print("Username: ");
        username = reader.readLine();

        return username;
    }


    // Reads password from console
    public String getPassword() throws IOException
    {
        String password;
        BufferedReader reader = new BufferedReader( new InputStreamReader(System.in) );

        System.out.print("Password: ");
        password = reader.readLine();

        return password;
    }
}
