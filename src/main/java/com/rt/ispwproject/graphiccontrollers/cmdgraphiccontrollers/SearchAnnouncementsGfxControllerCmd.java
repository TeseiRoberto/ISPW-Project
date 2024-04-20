package com.rt.ispwproject.graphiccontrollers.cmdgraphiccontrollers;

import com.rt.ispwproject.CmdApplication;
import com.rt.ispwproject.beans.Announcement;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.cmdview.BaseViewCmd;
import com.rt.ispwproject.cmdview.SearchAnnouncementsViewCmd;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.graphiccontrollers.cmdgraphiccontrollers.makeoffercontroller.MakeOfferGfxControllerCmd;
import com.rt.ispwproject.logiccontrollers.SearchAnnouncementsManager;

import java.util.List;

// Graphic controller used by the "TRAVEL_AGENCY" to search for announcements
public class SearchAnnouncementsGfxControllerCmd extends BaseGfxControllerCmd {

    private boolean                         runLoop;
    private List<Announcement>              announcements;
    private final SearchAnnouncementsViewCmd view;

    private static final int                MAX_NUM_OF_ANNOUNCEMENTS_DISPLAYED = 2;


    public SearchAnnouncementsGfxControllerCmd(Session session)
    {
        super(session);
        view = new SearchAnnouncementsViewCmd();
    }


    public void start()
    {
        onLoadMoreAnnouncementsSelected();
        menu();
    }


    private void menu()
    {
        runLoop = true;
        List<String> possibilities = List.of( "see announcement details", "load more announcements", "see my offers", "exit" );

        do {
            view.showScreenTitle();

            if(announcements.isEmpty())
            {
                view.print("\nNo announcement is currently available.\n");
            } else {
                view.showAnnouncementsList(announcements, true);
                view.print("\n");
            }

            int choice = view.getChoiceFromUser(possibilities);
            switch(choice)
            {
                case 1: onSeeAnnouncementDetailsSelected(); break;
                case 2: onLoadMoreAnnouncementsSelected(); break;
                case 3: onSeeMyOffersSelected(); break;
                case 4: onExitSelected(); break;
                default: view.showErrorDialog(BaseViewCmd.INVALID_OPTION_MSG); break;
            }
        } while(runLoop);
    }


    // Invoked when the user wants to see the details of an announcement, switches to the "make offer" view
    private void onSeeAnnouncementDetailsSelected()
    {
        if(announcements.isEmpty())
        {
            view.print("No announcement is available...\n");
            return;
        }

        view.print("Insert the number of the announcement ==> ");
        int choice = view.getIntFromUser(1, announcements.size());

        runLoop = false;
        CmdApplication.changeScreen( new MakeOfferGfxControllerCmd(currSession, announcements.get(choice - 1)));
    }


    // Invoked when the user wants to load more announcements, updates the announcements list with new announcements
    private void onLoadMoreAnnouncementsSelected()
    {
        // Indicates from which announcement the logic controller will start to load the announcements
        int startId = 0;
        if(announcements != null && !announcements.isEmpty())
            startId = announcements.getLast().getId() + 1;

        try {
            SearchAnnouncementsManager searchManager = new SearchAnnouncementsManager();
            announcements = searchManager.searchAnnouncements(currSession, startId, MAX_NUM_OF_ANNOUNCEMENTS_DISPLAYED);
        } catch(DbException | IllegalCallerException | IllegalArgumentException e)
        {
            view.showErrorDialog(e.getMessage());
            announcements.clear();
        }
    }


    // Invoked when the user wants to see the offers sent by him, switches to the "my offers" view
    private void onSeeMyOffersSelected()
    {
        view.showErrorDialog("NOT IMPLEMENTED YET...");
        // TODO: Add implementation
    }


    // Invoked when the user wants to exit the application
    private void onExitSelected()
    {
        runLoop = false;
        CmdApplication.changeScreen(null);
        view.print("Goodbye!\n");
    }
}
