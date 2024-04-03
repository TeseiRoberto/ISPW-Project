package com.rt.ispwproject.graphiccontrollers.cmdgraphiccontrollers;

import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.cmdview.LoginViewCmd;
import com.rt.ispwproject.config.UserRole;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.logiccontrollers.LoginManager;

import java.io.IOException;

public class LoginGfxControllerCmd {

    private final LoginViewCmd loginView = new LoginViewCmd();

    public void start()
    {
        while(true)
        {
            loginView.showTitle();
            try {
                String username = loginView.getUsername();
                String password = loginView.getPassword();

                LoginManager loginMan = new LoginManager();
                Session currSession = loginMan.login(username, password);

                // TODO: Need to switch to new "screen" according to user role
                switch(currSession.getUserRole())
                {
                    case UserRole.SIMPLE_USER ->    loginView.print("Hello SIMPLE_USER!\n");
                    case UserRole.TRAVEL_AGENCY ->  loginView.print("Hello TRAVEL_AGENCY!\n");
                }
            } catch(RuntimeException | DbException | IOException e)
            {
                loginView.displayErrorDialog(e.getMessage());
            }
        }
    }

}
