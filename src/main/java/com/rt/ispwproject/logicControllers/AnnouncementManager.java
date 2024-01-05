package com.rt.ispwproject.logicControllers;

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
    public void PostAnnouncement(Session currUser, Announcement announcement) throws RuntimeException, DbException
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

        LocalDate today = LocalDate.now();
        newReq.setDateOfPost(today);

        newReq.checkValidity();

        HolidayRequirementsDao requirementsDao = new HolidayRequirementsDao();
        requirementsDao.postRequirements(currUser.getUserId(), newReq);
    }


    // Removes an existing announcement from the system
    public void RemoveAnnouncement(Session currUser, Announcement announcement) throws DbException
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
            Announcement a = new Announcement(
              req.getId(), req.getOwner(), req.getDestination(), req.getHolidayDescription(), req.getAvailableBudget(), req.getNumOfTravelers(),
              req.getDateOfPost(), req.getDepartureDate(), req.getReturnDate(), req.getAccommodationType(), req.getAccommodationQuality(),
              req.getNumOfRoomsRequired(), req.getTransportType(), req.getTransportQuality(), req.getNumOfViews()
            );

            announcements.add(a);
        }

        return announcements;
    }


}
