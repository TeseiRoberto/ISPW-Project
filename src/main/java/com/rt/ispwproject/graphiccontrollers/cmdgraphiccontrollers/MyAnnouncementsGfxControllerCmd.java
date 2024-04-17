package com.rt.ispwproject.graphiccontrollers.cmdgraphiccontrollers;

import com.rt.ispwproject.CmdApplication;
import com.rt.ispwproject.beans.Announcement;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.cmdview.BaseViewCmd;
import com.rt.ispwproject.cmdview.MyAnnouncementsViewCmd;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.logiccontrollers.AnnouncementManager;

import java.util.List;

// Graphic controller that displays the list of announcements posted by a "SIMPLE_USER"
public class MyAnnouncementsGfxControllerCmd extends BaseGfxControllerCmd {

    private List<Announcement>              announcements;
    private final MyAnnouncementsViewCmd    view;


    public MyAnnouncementsGfxControllerCmd(Session session)
    {
        super(session);
        view = new MyAnnouncementsViewCmd();
    }


    // Load the announcements posted by the user and starts the menu
    public void start()
    {
        try {
            AnnouncementManager annManager = new AnnouncementManager();
            announcements = annManager.getMyAnnouncements(currSession);
        } catch(DbException | IllegalCallerException | IllegalArgumentException e)
        {
            view.showErrorDialog(e.getMessage());
            announcements.clear();
        }

        menu();
    }


    private void menu()
    {
        view.showScreenTitle();
        view.showMyAnnouncements(announcements);
        view.print("\n");

        List<String> possibilities = List.of( "see announcement details", "create announcement", "exit" );
        int choice = view.getChoiceFromUser(possibilities);
        switch(choice)
        {
            case 1:     onSeeAnnouncementDetailsSelected(); break;
            case 2:     onCreateAnnouncementSelected(); break;
            case 3:     onExitSelected(); break;
            default:    view.showErrorDialog(BaseViewCmd.INVALID_OPTION_MSG); break;
        }
    }


    // Invoked when the user wants to see the details of an announcement, switches to the "announcement details" view
    private void onSeeAnnouncementDetailsSelected()
    {
        if(announcements.isEmpty())
        {
            view.print("No announcement has been posted yet...\n");
            return;
        }

        view.print("Insert the number of the announcement ==> ");
        int choice = view.getIntFromUser(1, announcements.size());
        CmdApplication.changeScreen( new AnnouncementDetailsGfxControllerCmd(currSession, announcements.get(choice - 1)) );
    }


    // Invoked when the user wants to create a new announcement, switches to the "create announcement view"
    private void onCreateAnnouncementSelected()
    {
        CmdApplication.changeScreen( new CreateAnnouncementGfxControllerCmd(currSession) );
    }


    // Invoked when the user wants to exit the application
    private void onExitSelected()
    {
        CmdApplication.changeScreen(null);
        view.print("Goodbye!\n");
    }

}
