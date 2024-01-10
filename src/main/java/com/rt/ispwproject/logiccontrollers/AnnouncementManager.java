package com.rt.ispwproject.logiccontrollers;

import com.rt.ispwproject.beans.Announcement;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.dao.HolidayRequirementsDao;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.model.HolidayRequirements;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementManager {

    // Inserts a new announcement in the system
    public void postAnnouncement(Session currUser, Announcement announcement) throws DbException
    {
        HolidayRequirements newReq = new HolidayRequirements();

        // Set holiday requirements using announcement data
        newReq.setOwner(currUser.getUsername());
        newReq.setDestination(announcement.getDestination());
        newReq.setHolidayDescription(announcement.getHolidayDescription());
        newReq.setAvailableBudget(announcement.getAvailableBudget());
        newReq.setNumOfTravelers(announcement.getNumOfTravelers());
        newReq.setDepartureDate(announcement.getDepartureDate());
        newReq.setReturnDate(announcement.getReturnDate());
        newReq.setAccommodationType(announcement.getAccommodationType());
        newReq.setAccommodationQuality(announcement.getAccommodationQuality());
        newReq.setNumOfRoomsRequired(announcement.getNumOfRoomsRequired());
        newReq.setTransportType(announcement.getTransportType());
        newReq.setTransportQuality(announcement.getTransportQuality());

        newReq.setDateOfPost(LocalDate.now());

        //newReq.checkValidity(); TODO: This must be done in the bean class

        HolidayRequirementsDao requirementsDao = new HolidayRequirementsDao();
        requirementsDao.postRequirements(currUser.getUserId(), newReq);
    }


    // Removes an existing announcement from the system
    public void removeAnnouncement(Session currUser, Announcement announcement) throws DbException
    {
        HolidayRequirementsDao requirementsDao = new HolidayRequirementsDao();
        requirementsDao.removeRequirements(currUser.getUserId(), announcement.getId());
    }


    // Retrieves the announcements published by the given user
    public List<Announcement> getMyAnnouncements(Session currUser) throws DbException
    {
        HolidayRequirementsDao requirementsDao = new HolidayRequirementsDao();

        List<HolidayRequirements> requirements = requirementsDao.getPostedRequirements(currUser.getUserId());
        ArrayList<Announcement> announcements = new ArrayList<>();

        for(HolidayRequirements req : requirements)         // Convert model instances to beans
        {
            Announcement a = new Announcement();
            a.setId(req.getId());
            a.setOwner(req.getOwner());
            a.setDestination(req.getDestination());
            a.setHolidayDescription(req.getHolidayDescription());
            a.setAvailableBudget(req.getAvailableBudget());
            a.setNumOfTravelers(req.getNumOfTravelers());
            a.setDateOfPost(req.getDateOfPost());
            a.setDepartureDate(req.getDepartureDate());
            a.setReturnDate(req.getReturnDate());
            a.setAccommodationType(req.getAccommodationType());
            a.setAccommodationQuality(req.getAccommodationQuality());
            a.setNumOfRoomsRequired(req.getNumOfRoomsRequired());
            a.setTransportType(req.getTransportType());
            a.setTransportQuality(req.getTransportQuality());
            a.setDepartureLocation(req.getDepartureLocation());
            a.setNumOfViews(req.getNumOfViews());

            announcements.add(a);
        }

        return announcements;
    }


}
