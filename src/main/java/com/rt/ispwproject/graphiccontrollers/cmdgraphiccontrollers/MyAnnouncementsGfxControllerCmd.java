package com.rt.ispwproject.graphiccontrollers.cmdgraphiccontrollers;

import com.rt.ispwproject.CmdApplication;
import com.rt.ispwproject.beans.Announcement;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.cmdview.MyAnnouncementsViewCmd;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.logiccontrollers.AnnouncementManager;

import java.util.List;

public class MyAnnouncementsGfxControllerCmd extends BaseGfxControllerCmd {

    private List<Announcement>              announcements;
    private final MyAnnouncementsViewCmd    view;


    public MyAnnouncementsGfxControllerCmd(Session session)
    {
        super(session);
        view = new MyAnnouncementsViewCmd();
    }


    public void start()
    {
        // Load the announcements posted by the user
        try {
            AnnouncementManager annManager = new AnnouncementManager();
            announcements = annManager.getMyAnnouncements(currSession);
        } catch(DbException | IllegalCallerException | IllegalArgumentException e)
        {
            view.showErrorDialog(e.getMessage());
            announcements.clear();
        }

        view.showScreenTitle();
        view.showMyAnnouncements(announcements);
        view.print("\n");

        String[] possibilities = { "see announcement details", "create announcement", "exit" };
        int choice = view.getChoiceFromUser(possibilities);
        switch(choice)
        {
            case 1: onSeeAnnouncementDetailsSelected(); break;
            case 2: onCreateAnnouncementSelected(); break;
            case 3: onExitSelected(); break;
        }
    }


    // Invoked when the user wants to see the details of an announcement
    public void onSeeAnnouncementDetailsSelected()
    {
        view.print("Insert the number of the announcement ==>");
        view.getIntFromUser(1, announcements.size());

        // TODO: Need to switch to the "show announcement view" passing the selected announcement...
    }


    // Invoked when the user wants to create a new announcement, switches to the "create announcement view"
    public void onCreateAnnouncementSelected()
    {
        CmdApplication.changeScreen( new CreateAnnouncementGfxControllerCmd(currSession) );
    }


    // Invoked when the user wants to exit the application
    public void onExitSelected()
    {
        CmdApplication.changeScreen(null);
        view.print("Goodbye!\n");
    }

}
