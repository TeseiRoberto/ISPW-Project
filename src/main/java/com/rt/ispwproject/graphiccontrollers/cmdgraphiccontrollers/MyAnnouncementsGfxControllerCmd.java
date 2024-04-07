package com.rt.ispwproject.graphiccontrollers.cmdgraphiccontrollers;

import com.rt.ispwproject.beans.Announcement;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.cmdview.MyAnnouncementsView;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.logiccontrollers.AnnouncementManager;

import java.util.List;

public class MyAnnouncementsGfxControllerCmd extends BaseGfxControllerCmd {

    private List<Announcement>          announcements;
    private final MyAnnouncementsView   view;


    public MyAnnouncementsGfxControllerCmd(Session session)
    {
        super(session);
        view = new MyAnnouncementsView();
    }


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

        view.showAnnouncements(announcements);

        String[] possibilities = { "see announcement details", "create announcement", "exit" };
        int choice = view.showMenu(possibilities);
        switch(choice)
        {
            case 1:
                // TODO: Need to change screen according to user choice
                view.print("See announcement details chosen!!!\n");
                break;

            case 2:
                // TODO: Need to change screen according to user choice
                view.print("Create announcement chosen!!!\n");
                break;

            case 3:
                changeScreen(null);
                view.print("Goodbye!\n");
                break;
        }
    }

}
