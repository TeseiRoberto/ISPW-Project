package com.rt.ispwproject.logiccontrollers;

import com.rt.ispwproject.beans.Announcement;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.config.SessionManager;
import com.rt.ispwproject.dao.HolidayRequirementsDao;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.model.HolidayRequirements;

import java.util.ArrayList;
import java.util.List;

public class SearchAnnouncementsManager {


    // Returns a list of announcements width ids starting from startId, the list size is between 0 and maxAnnouncementsNum
    public List<Announcement> loadAnnouncements(Session currSession, int startId, int maxAnnouncementsNum) throws DbException, IllegalArgumentException
    {
        if(SessionManager.getInstance().getProfile(currSession) == null)
            throw new IllegalArgumentException("You must be logged in to load announcements!");

        ArrayList<Announcement> announcements = new ArrayList<>();
        List<HolidayRequirements> requirements;

        HolidayRequirementsDao requirementsDao = new HolidayRequirementsDao();
        requirements = requirementsDao.searchRequirements(startId, maxAnnouncementsNum);

        for(HolidayRequirements req : requirements)
        {
            try {
                announcements.add(req.toAnnouncement());
            }
            catch (IllegalArgumentException e) {
                throw new DbException("Db returned invalid data for an announcement: " + e.getMessage());
            }
        }

        return announcements;
    }

}
