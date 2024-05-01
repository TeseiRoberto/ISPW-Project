package com.rt.ispwproject.logiccontrollers;

import com.rt.ispwproject.beans.Announcement;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.config.SessionManager;
import com.rt.ispwproject.config.UserRole;
import com.rt.ispwproject.dao.HolidayRequirementsDao;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.model.HolidayRequirements;

import java.util.ArrayList;
import java.util.List;

public class SearchAnnouncementsManager {


    // Returns a list of announcements that have been posted in the system by users
    // @currSession: user that is searching for announcements
    // @startId: indicates from which announcement to start searching
    // @maxAnnouncementsNum: maximum number of elements that will be returned
    public List<Announcement> searchAnnouncements(Session currSession, int startId, int maxAnnouncementsNum) throws DbException, IllegalCallerException, IllegalArgumentException
    {
        if(!SessionManager.getInstance().isLoggedAs(currSession, UserRole.TRAVEL_AGENCY))
            throw new IllegalCallerException("You must be logged in to load announcements!");

        ArrayList<Announcement> announcements = new ArrayList<>();
        List<HolidayRequirements> requirements;

        HolidayRequirementsDao requirementsDao = new HolidayRequirementsDao();
        requirements = requirementsDao.searchRequirements(startId, maxAnnouncementsNum);

        for(HolidayRequirements req : requirements)
        {
            try {
                announcements.add(req.toAnnouncementBean());
            }
            catch (IllegalArgumentException e) {
                throw new DbException("Db returned invalid data for an announcement: " + e.getMessage());
            }
        }

        return announcements;
    }

}
