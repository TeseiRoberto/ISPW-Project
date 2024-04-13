package com.rt.ispwproject.cmdview;


import com.rt.ispwproject.beans.Announcement;

import java.util.List;

public class MyAnnouncementsViewCmd extends BaseViewCmd {

    public void showScreenTitle()
    {
        printTitle(BaseViewCmd.MY_ANNOUNCEMENTS_SCREEN_NAME);
    }


    public void showMyAnnouncements(List<Announcement> announcements)
    {
        if(announcements.isEmpty())
        {
            print("No announcement has been posted yet...\n");
        } else {
            print("List of posted announcements:\n");
            showAnnouncementsList(announcements);
        }
    }

}
