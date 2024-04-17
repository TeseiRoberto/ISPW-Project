package com.rt.ispwproject.graphiccontrollers.cmdgraphiccontrollers;

import com.rt.ispwproject.CmdApplication;
import com.rt.ispwproject.beans.Announcement;
import com.rt.ispwproject.beans.Offer;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.cmdview.RequestChangesViewCmd;


// Graphic controller used by the "SIMPLE_USER" to create and send a request of changes on an offer received
public class RequestChangesGfxControllerCmd extends BaseGfxControllerCmd {

    private final RequestChangesViewCmd view;
    private final Announcement          currAnnounce;
    private final Offer                 currOffer;


    public RequestChangesGfxControllerCmd(Session session, Announcement announce, Offer offer)
    {
        super(session);
        currOffer = offer;
        currAnnounce = announce;
        view = new RequestChangesViewCmd();
    }


    public void start()
    {
        menu();
    }


    private void menu()
    {
        view.showScreenTitle();

        // TODO: Add implementation
        view.showErrorDialog("NOT IMPLEMENTED YET...");

        CmdApplication.changeScreen( new AnnouncementDetailsGfxControllerCmd(currSession, currAnnounce) );
    }

}
