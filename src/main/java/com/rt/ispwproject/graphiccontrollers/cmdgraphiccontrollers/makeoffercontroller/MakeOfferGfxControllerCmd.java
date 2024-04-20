package com.rt.ispwproject.graphiccontrollers.cmdgraphiccontrollers.makeoffercontroller;

import com.rt.ispwproject.CmdApplication;
import com.rt.ispwproject.beans.Announcement;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.cmdview.makeofferview.MakeOfferViewCmd;
import com.rt.ispwproject.graphiccontrollers.cmdgraphiccontrollers.SearchAnnouncementsGfxControllerCmd;


public class MakeOfferGfxControllerCmd extends BaseMakeGfxOfferControllerCmd {

    private final MakeOfferViewCmd  view;
    private final Announcement      currAnnounce;
    //private final Offer             currOffer;


    public MakeOfferGfxControllerCmd(Session session, Announcement announce)
    {
        super(session);
        view = new MakeOfferViewCmd();
        currAnnounce = announce;
        //currOffer = new Offer();
    }


    public void start()
    {
        menu();
    }


    protected void menu()
    {
        view.showScreenTitle();
        view.showErrorDialog("NOT IMPLEMENTED YET...");

        // TODO: Add implementation...
        CmdApplication.changeScreen( new SearchAnnouncementsGfxControllerCmd(currSession) );
    }
}
