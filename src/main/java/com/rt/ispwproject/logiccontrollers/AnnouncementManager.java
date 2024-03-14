package com.rt.ispwproject.logiccontrollers;

import com.rt.ispwproject.apiboundaries.AddressChecker;
import com.rt.ispwproject.beans.Announcement;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.config.SessionManager;
import com.rt.ispwproject.config.UserRole;
import com.rt.ispwproject.dao.HolidayRequirementsDao;
import com.rt.ispwproject.exceptions.ApiException;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.factories.LocationFactory;
import com.rt.ispwproject.model.*;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementManager {


    // Inserts a new announcement in the system
    public void postAnnouncement(Session currSession, Announcement announce) throws DbException, IllegalCallerException, IllegalArgumentException
    {
        if(!SessionManager.getInstance().isLoggedAs(currSession, UserRole.SIMPLE_USER))
            throw new IllegalCallerException("You must be logged in to post an announcement");

        Profile user = SessionManager.getInstance().getProfile(currSession);

        // Create holiday requirements with data in the given announcement
        HolidayRequirementsMetadata metadata = new HolidayRequirementsMetadata(user, announce.getDateOfPost());
        DateRange holidayDuration = new DateRange(announce.getHolidayDuration().getDepartureDate(), announce.getHolidayDuration().getReturnDate());
        Route fromToLocation = new Route(
                LocationFactory.getInstance().createLocation( announce.getTransportRequirements().getDepartureLocation() ),
                LocationFactory.getInstance().createLocation( announce.getTransportRequirements().getArrivalLocation() )
        );

        AccommodationRequirements accommodationReq = new AccommodationRequirements(
                AccommodationType.fromViewType(announce.getAccommodationRequirements().getType()),
                announce.getAccommodationRequirements().getQuality(),
                announce.getAccommodationRequirements().getNumOfRooms()
        );

        TransportRequirements transportReq = new TransportRequirements(
                TransportType.fromViewType(announce.getTransportRequirements().getType()),
                announce.getTransportRequirements().getQuality(),
                announce.getTransportRequirements().getNumOfTravelers(),
                fromToLocation
        );

        HolidayRequirements newReq = new HolidayRequirements(
                metadata, announce.getHolidayDescription(), holidayDuration,
                announce.getAvailableBudget(), accommodationReq, transportReq
        );

        HolidayRequirementsDao requirementsDao = new HolidayRequirementsDao();
        requirementsDao.postRequirements(newReq);
    }


    // Removes an existing announcement from the system
    public void removeAnnouncement(Session currSession, Announcement announce) throws DbException, IllegalCallerException, IllegalArgumentException
    {
        if(!SessionManager.getInstance().isLoggedAs(currSession, UserRole.SIMPLE_USER))
            throw new IllegalCallerException("You must be logged in to remove an announcement");

        HolidayRequirementsDao requirementsDao = new HolidayRequirementsDao();
        HolidayRequirements requirements = requirementsDao.getRequirementsById(announce.getId());
        requirementsDao.removeRequirements(requirements);
    }


    // Retrieves the announcements published by the given user
    public List<Announcement> getMyAnnouncements(Session currSession) throws DbException, IllegalCallerException, IllegalArgumentException
    {
        if(!SessionManager.getInstance().isLoggedAs(currSession, UserRole.SIMPLE_USER))
            throw new IllegalArgumentException("You must be logged in to get your announcements");

        Profile user = SessionManager.getInstance().getProfile(currSession);
        HolidayRequirementsDao requirementsDao = new HolidayRequirementsDao();
        List<HolidayRequirements> requirements = requirementsDao.getRequirementsPostedByUser(user.getUserId());

        ArrayList<Announcement> announcements = new ArrayList<>();
        for(HolidayRequirements req : requirements)         // Convert model instances to beans
        {
            try {
                Announcement announce = req.toAnnouncementBean();
                announcements.add(announce);
            }
            catch (IllegalArgumentException e) {
                throw new DbException("Db returned invalid data for an announcement:\n" + e.getMessage());
            }
        }

        return announcements;
    }

}
