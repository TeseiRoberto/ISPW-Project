package com.rt.ispwproject.graphiccontrollers.cmdgraphiccontrollers;

import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.cmdview.LoginViewCmd;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.logiccontrollers.LoginManager;

import java.io.IOException;

public class LoginGfxControllerCmd {

    private final LoginViewCmd loginView = new LoginViewCmd();

    public void start()
    {
        String username;
        String password;

        loginView.showWelcome();
        try {
            username = loginView.getUsername();
            password = loginView.getPassword();

            LoginManager loginMan = new LoginManager();
            Session currSession = loginMan.login(username, password);

            System.out.println("LOGGED AS: " + currSession.getUsername() + " - " + " - " + currSession.getUserRole().toString());
            // TODO: Need to switch to new "screen" according to user role

        } catch(RuntimeException | DbException | IOException e)
        {
            System.out.println(e.getMessage());
        }


    }

}
