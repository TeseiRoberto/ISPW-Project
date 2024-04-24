package com.rt.ispwproject.graphiccontrollers.cmdgraphiccontrollers.makeoffercontroller;

import com.rt.ispwproject.CmdApplication;
import com.rt.ispwproject.beans.*;
import com.rt.ispwproject.cmdview.BaseViewCmd;
import com.rt.ispwproject.cmdview.makeofferview.MakeOfferViewCmd;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.graphiccontrollers.cmdgraphiccontrollers.SearchAnnouncementsGfxControllerCmd;
import com.rt.ispwproject.logiccontrollers.OfferManager;

import java.util.List;

// Graphic controller used by the "TRAVEL_AGENCY" to make an offer for the announcement posted by a user
public class MakeOfferGfxControllerCmd extends BaseMakeOfferGfxControllerCmd {

    private final Announcement      currAnnounce;


    public MakeOfferGfxControllerCmd(Session session, Announcement announce)
    {
        super(session, new MakeOfferViewCmd());
        currAnnounce = announce;
        currOffer.setBidderUsername(session.getUsername());
        currOffer.setRelativeAnnouncementOwnerUsername(announce.getOwnerUsername());
    }


    public void start()
    {
        menu();
    }


    protected void menu()
    {
        runLoop = true;
        List<String> possibilities = List.of( "Edit offer field", "make offer to user", "back to search announcements" );

        do {
            view.showScreenTitle();
            view.showUserRequest(currAnnounce);
            view.showOffer(currOffer);

            int choice = view.getChoiceFromUser(possibilities);
            switch(choice)
            {
                case 1:     onEditOfferFieldSelected(); break;
                case 2:     onMakeOfferSelected(); break;
                case 3:     onBackSelected(); break;
                default:    view.showErrorDialog(BaseViewCmd.INVALID_OPTION_MSG); break;
            }
        } while(runLoop);
    }


    // Invoked when "make offer to user" is selected, uses the logic controller to insert the offer in the system
    private void onMakeOfferSelected()
    {
        try {
            if(!accommodationChosen)
                throw new IllegalArgumentException("No accommodation has been selected yet...");

            if(!transportChosen)
                throw new IllegalArgumentException("No transport has been selected yet...");

            OfferManager offerManager = new OfferManager();
            offerManager.makeOfferToUser(currSession, currAnnounce, currOffer);
        } catch(IllegalCallerException | IllegalArgumentException | DbException e)
        {
            view.showErrorDialog(e.getMessage());
            return;
        }

        runLoop = false;
        view.showInfoDialog("Offer sent correctly!");
        CmdApplication.changeScreen( new SearchAnnouncementsGfxControllerCmd(currSession) );
    }


    // Invoked when "back to search announcements" is selected, switches back to the "search announcements" view
    private void onBackSelected()
    {
        runLoop = false;
        CmdApplication.changeScreen( new SearchAnnouncementsGfxControllerCmd(currSession) );
    }

}
