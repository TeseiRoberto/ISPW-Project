package com.rt.ispwproject.logiccontrollers;

import com.rt.ispwproject.beans.Announcement;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.config.SessionManager;
import com.rt.ispwproject.dao.HolidayRequirementsDao;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.model.*;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementManager {

    // Inserts a new announcement in the system
    public void postAnnouncement(Session currSession, Announcement announce) throws DbException, IllegalArgumentException
    {
        Profile user = SessionManager.getInstance().getProfile(currSession);
        if(user == null)                                        // Check if currSession is valid
            throw new IllegalArgumentException("You must be logged in to post an announcement");

        // Create holiday requirements with data in the given announcement
        HolidayMetadata metadata = new HolidayMetadata(0, currSession.getUsername(), announce.getDateOfPost(), announce.getNumOfViews());
        DateRange holidayDuration = new DateRange(announce.getHolidayDuration().getDepartureDate(), announce.getHolidayDuration().getReturnDate());
        Location destination = new Location(announce.getDestination());
        Location departureLocation = new Location(announce.getTransportRequirements().getDepartureLocation());

        AccommodationRequirements accommodationReq = new AccommodationRequirements(
                announce.getAccommodationRequirements().getType(),
                announce.getAccommodationRequirements().getQuality(),
                announce.getAccommodationRequirements().getNumOfRooms()
        );

        TransportRequirements transportReq = new TransportRequirements(
                announce.getTransportRequirements().getType(),
                announce.getTransportRequirements().getQuality(),
                announce.getTransportRequirements().getNumOfTravelers(),
                departureLocation
        );

        HolidayRequirements newReq = new HolidayRequirements(
                metadata, destination, announce.getHolidayDescription(), holidayDuration,
                announce.getAvailableBudget(), accommodationReq, transportReq
        );

        HolidayRequirementsDao requirementsDao = new HolidayRequirementsDao();
        requirementsDao.postRequirements(user.getUserId(), newReq);
    }


    // Removes an existing announcement from the system
    public void removeAnnouncement(Session currSession, Announcement announce) throws DbException, IllegalArgumentException
    {
        Profile user = SessionManager.getInstance().getProfile(currSession);
        if(user == null)                                        // Check if currSession is valid
            throw new IllegalArgumentException("You must be logged in to remove an announcement");

        HolidayRequirementsDao requirementsDao = new HolidayRequirementsDao();
        requirementsDao.removeRequirementsPostedByUser(user.getUserId(), announce.getId());
    }


    // Retrieves the announcements published by the given user
    public List<Announcement> getMyAnnouncements(Session currSession) throws DbException, IllegalArgumentException
    {
        Profile user = SessionManager.getInstance().getProfile(currSession);
        if(user == null)                                        // Check if currSession is valid
            throw new IllegalArgumentException("You must be logged in to get your announcements");

        HolidayRequirementsDao requirementsDao = new HolidayRequirementsDao();
        List<HolidayRequirements> requirements = requirementsDao.loadRequirementsPostedByUser(user.getUserId());

        ArrayList<Announcement> announcements = new ArrayList<>();
        for(HolidayRequirements req : requirements)         // Convert model instances to beans
        {
            try {
                Announcement announce = req.toAnnouncement();
                announcements.add(announce);
            }
            catch (IllegalArgumentException e) {
                throw new DbException("Db returned invalid data for an announcement:\n" + e.getMessage());
            }
        }

        return announcements;
    }


}
