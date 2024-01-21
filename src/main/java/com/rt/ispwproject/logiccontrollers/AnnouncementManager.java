package com.rt.ispwproject.logiccontrollers;

import com.rt.ispwproject.beans.Announcement;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.dao.HolidayRequirementsDao;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.model.*;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementManager {

    // Inserts a new announcement in the system
    public void postAnnouncement(Session currUser, Announcement announce) throws DbException
    {
        // Create holiday requirements from the given announcement
        HolidayRequirements newReq = new HolidayRequirements(
                new HolidayMetadata(announce.getId(), announce.getOwner(), announce.getDateOfPost(), announce.getNumOfViews()),
                announce.getDestination(), announce.getHolidayDescription(),
                new DateRange(announce.getHolidayDuration().getStartDate(), announce.getHolidayDuration().getEndDate()),
                announce.getAvailableBudget(),
                new Accommodation(announce.getAccommodationType(), announce.getAccommodationQuality(), announce.getNumOfRoomsRequired()),
                new Transport(announce.getTransportType(), announce.getTransportQuality(),
                        new Route(announce.getDepartureLocation(), announce.getDestination()), announce.getNumOfTravelers())
        );

        HolidayRequirementsDao requirementsDao = new HolidayRequirementsDao();
        requirementsDao.postRequirements(currUser.getUserId(), newReq);
    }


    // Removes an existing announcement from the system
    public void removeAnnouncement(Session currUser, Announcement announce) throws DbException
    {
        HolidayRequirementsDao requirementsDao = new HolidayRequirementsDao();
        requirementsDao.removeRequirementsPostedByUser(currUser.getUserId(), announce.getId());
    }


    // Retrieves the announcements published by the given user
    public List<Announcement> getMyAnnouncements(Session currUser) throws DbException
    {
        HolidayRequirementsDao requirementsDao = new HolidayRequirementsDao();

        List<HolidayRequirements> requirements = requirementsDao.loadRequirementsPostedByUser(currUser.getUserId());
        ArrayList<Announcement> announcements = new ArrayList<>();

        for(HolidayRequirements req : requirements)         // Convert model instances to beans
        {
            try {
                Announcement announce = req.toAnnouncement();
                announcements.add(announce);
            }
            catch (IllegalArgumentException e) {
                throw new DbException("Db returned invalid data for an announcement: " + e.getMessage());
            }
        }

        return announcements;
    }


}
