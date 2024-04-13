package com.rt.ispwproject.graphiccontrollers.cmdgraphiccontrollers;

import com.rt.ispwproject.CmdApplication;
import com.rt.ispwproject.beans.Session;

public class CreateAnnouncementGfxControllerCmd extends BaseGfxControllerCmd {


    public CreateAnnouncementGfxControllerCmd(Session session)
    {
        super(session);
    }


    public void start()
    {
        // TODO: Add implementation...
        CmdApplication.changeScreen( new MyAnnouncementsGfxControllerCmd(currSession) );
    }
}
