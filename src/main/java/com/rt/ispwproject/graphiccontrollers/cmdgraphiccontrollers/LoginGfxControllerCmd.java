package com.rt.ispwproject.graphiccontrollers.cmdgraphiccontrollers;

import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.cmdview.LoginViewCmd;
import com.rt.ispwproject.config.UserRole;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.logiccontrollers.LoginManager;

public class LoginGfxControllerCmd {

    public void start()
    {
        LoginViewCmd view = new LoginViewCmd();
        view.showLoginForm();

        try {
            LoginManager loginMan = new LoginManager();
            Session currSession = loginMan.login(view.getUsername(), view.getPassword());

            if(currSession.getUserRole() == UserRole.SIMPLE_USER)
                BaseGfxControllerCmd.changeScreen( new MyAnnouncementsGfxControllerCmd(currSession) );
            else if(currSession.getUserRole() == UserRole.TRAVEL_AGENCY)
                // TODO: BaseGfxController.changeScreen( new SearchAnnouncementsGfxControllerCmd(currSession) );
                view.print("Hello TRAVEL_AGENCY!\n");

        } catch(RuntimeException | DbException e)
        {
            view.showErrorDialog(e.getMessage());
        }
    }

}
