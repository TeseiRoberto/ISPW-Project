package com.rt.ispwproject.cmdview;

import com.rt.ispwproject.beans.Announcement;

import java.util.List;

public class MyAnnouncementsView extends BaseView {


    public MyAnnouncementsView()
    {
        showScreenTitle(BaseView.MY_ANNOUNCEMENTS_SCREEN_NAME);
    }


    public void showAnnouncements(List<Announcement> announcements)
    {
        if(announcements.isEmpty())
        {
            print("No announcement has been posted yet...\n");
        } else {
            print("List of posted announcements:\n");
            for(int i = 0; i < announcements.size(); i++)
            {
                printf("%d] %s - %s - %s - %s\n", i + 1,
                        announcements.get(i).getDestination(),
                        announcements.get(i).getHolidayDuration().getDepartureDate().toString(),
                        announcements.get(i).getHolidayDuration().getReturnDate().toString(),
                        announcements.get(i).getAvailableBudgetAsStr()
                );
            }
        }
        print("\n");
    }
}
