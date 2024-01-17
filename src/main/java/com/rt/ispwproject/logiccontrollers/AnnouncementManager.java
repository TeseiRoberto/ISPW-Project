package com.rt.ispwproject.logiccontrollers;

import com.rt.ispwproject.beans.Announcement;
import com.rt.ispwproject.beans.Duration;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.dao.HolidayRequirementsDao;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.model.HolidayRequirements;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementManager {

    // Inserts a new announcement in the system
    public void postAnnouncement(Session currUser, Announcement announce) throws DbException
    {
        HolidayRequirements newReq = new HolidayRequirements();

        // Set holiday requirements using announcement data
        newReq.setOwner(currUser.getUsername());
        newReq.setDestination(announce.getDestination());
        newReq.setHolidayDescription(announce.getHolidayDescription());
        newReq.setAvailableBudget(announce.getAvailableBudget());
        newReq.setDateOfPost(announce.getDateOfPost());
        newReq.setDepartureDate(announce.getHolidayDuration().getStartDate());
        newReq.setReturnDate(announce.getHolidayDuration().getEndDate());
        newReq.setAccommodationType(announce.getAccommodationType());
        newReq.setAccommodationQuality(announce.getAccommodationQuality());
        newReq.setNumOfRoomsRequired(announce.getNumOfRoomsRequired());
        newReq.setTransportType(announce.getTransportType());
        newReq.setTransportQuality(announce.getTransportQuality());
        newReq.setNumOfTravelers(announce.getNumOfTravelers());
        newReq.setDepartureLocation(announce.getDepartureLocation());

        HolidayRequirementsDao requirementsDao = new HolidayRequirementsDao();
        requirementsDao.postRequirements(currUser.getUserId(), newReq);
    }


    // Removes an existing announcement from the system
    public void removeAnnouncement(Session currUser, Announcement announce) throws DbException
    {
        HolidayRequirementsDao requirementsDao = new HolidayRequirementsDao();
        requirementsDao.removeRequirements(currUser.getUserId(), announce.getId());
    }


    // Retrieves the announcements published by the given user
    public List<Announcement> getMyAnnouncements(Session currUser) throws DbException
    {
        HolidayRequirementsDao requirementsDao = new HolidayRequirementsDao();

        List<HolidayRequirements> requirements = requirementsDao.getPostedRequirements(currUser.getUserId());
        ArrayList<Announcement> announcements = new ArrayList<>();

        for(HolidayRequirements req : requirements)         // Convert model instances to beans
        {
            try {
                Announcement a = new Announcement();
                a.setId(req.getId());
                a.setOwner(req.getOwner());
                a.setDestination(req.getDestination());
                a.setHolidayDescription(req.getHolidayDescription());
                a.setAvailableBudget(req.getAvailableBudget());
                a.setDateOfPost(req.getDateOfPost());
                a.setHolidayDuration( new Duration(req.getDepartureDate(), req.getReturnDate()) );
                a.setAccommodationType(req.getAccommodationType());
                a.setAccommodationQuality(req.getAccommodationQuality());
                a.setNumOfRoomsRequired(req.getNumOfRoomsRequired());
                a.setTransportType(req.getTransportType());
                a.setTransportQuality(req.getTransportQuality());
                a.setDepartureLocation(req.getDepartureLocation());
                a.setNumOfTravelers(req.getNumOfTravelers());
                a.setNumOfViews(req.getNumOfViews());

                announcements.add(a);
            }
            catch (IllegalArgumentException e) {
                throw new DbException("Db returned invalid data for an announcement: " + e.getMessage());
            }
        }

        return announcements;
    }


}
