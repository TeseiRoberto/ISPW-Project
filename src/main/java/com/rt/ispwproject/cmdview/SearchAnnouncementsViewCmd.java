package com.rt.ispwproject.cmdview;

import com.rt.ispwproject.beans.Announcement;

import java.util.List;

public class SearchAnnouncementsViewCmd extends BaseViewCmd {

    public void showScreenTitle()
    {
        printTitle(BaseViewCmd.SEARCH_ANNOUNCEMENTS_SCREEN_NAME);
    }


    public void showAnnouncements(List<Announcement> announcements)
    {
        if(announcements.isEmpty())
        {
            print("\nNo announcement is currently available.\n");
        } else {
            showAnnouncementsList(announcements, true);
            print("\n");
        }
    }
}
