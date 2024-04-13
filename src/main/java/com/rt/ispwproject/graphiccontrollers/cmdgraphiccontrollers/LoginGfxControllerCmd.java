package com.rt.ispwproject.graphiccontrollers.cmdgraphiccontrollers;

import com.rt.ispwproject.CmdApplication;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.cmdview.LoginViewCmd;
import com.rt.ispwproject.config.UserRole;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.logiccontrollers.LoginManager;

public class LoginGfxControllerCmd {

    public void start()
    {
        LoginViewCmd view = new LoginViewCmd();
        view.showScreenTitle();

        String username = view.getUsername();
        String password = view.getPassword();

        try {
            LoginManager loginMan = new LoginManager();
            Session currSession = loginMan.login(username, password);

            onLoginSuccess(currSession);
        } catch(RuntimeException | DbException e)
        {
            view.showErrorDialog(e.getMessage());
        }
    }


    private void onLoginSuccess(Session session)
    {
        if(session.getUserRole() == UserRole.SIMPLE_USER)
            CmdApplication.changeScreen( new MyAnnouncementsGfxControllerCmd(session) );
        /*else if(session.getUserRole() == UserRole.TRAVEL_AGENCY)
            // TODO: CmdApplication.changeScreen( new SearchAnnouncementsGfxControllerCmd(currSession) );
         */
    }

}
