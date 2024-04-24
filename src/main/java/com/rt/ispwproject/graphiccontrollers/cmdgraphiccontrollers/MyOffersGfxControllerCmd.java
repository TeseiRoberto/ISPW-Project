package com.rt.ispwproject.graphiccontrollers.cmdgraphiccontrollers;

import com.rt.ispwproject.CmdApplication;
import com.rt.ispwproject.beans.Offer;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.cmdview.BaseViewCmd;
import com.rt.ispwproject.cmdview.MyOffersViewCmd;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.logiccontrollers.OfferManager;

import java.util.List;

// Graphic controller that displays the list of offers made by the "TRAVEL_AGENCY"
public class MyOffersGfxControllerCmd extends BaseGfxControllerCmd {

    private boolean                 runLoop;
    private final MyOffersViewCmd   view;
    private List<Offer>             offers;


    public MyOffersGfxControllerCmd(Session session)
    {
        super(session);
        view = new MyOffersViewCmd();
    }


    // Loads all the offer made by the current user and starts the menu
    public void start()
    {
        try {
            OfferManager offerManager = new OfferManager();
            offers = offerManager.getMyOffers(currSession);

        } catch(DbException | IllegalArgumentException e)
        {
            offers.clear();
            view.showErrorDialog(e.getMessage());
        }

        menu();
    }


    private void menu()
    {
        runLoop = true;
        List<String> possibilities = List.of( "see offer details", "back to search announcements" );

        do {
            view.showScreenTitle();
            view.showOffers(offers);

            switch(view.getChoiceFromUser(possibilities))
            {
                case 1:     onSeeOfferDetailsSelected(); break;
                case 2:     onBackSelected(); break;
                default:    view.showErrorDialog(BaseViewCmd.INVALID_OPTION_MSG); break;
            }
        } while(runLoop);
    }


    // Invoked when "see offer details" is selected, switches to the "offer details" view
    private void onSeeOfferDetailsSelected()
    {
        if(offers.isEmpty())
        {
            view.print("No offer has been posted yet...\n");
            return;
        }

        view.print("Insert the number of the offer ==> ");
        int choice = view.getIntFromUser(1, offers.size());

        runLoop = false;
        CmdApplication.changeScreen( new OfferDetailsGfxControllerCmd(currSession, offers.get(choice - 1)) );
    }


    // Invoked when "back to search announcements" is selected, switches back to the "search announcements" view
    private void onBackSelected()
    {
        runLoop = false;
        CmdApplication.changeScreen( new SearchAnnouncementsGfxControllerCmd(currSession) );
    }
}
