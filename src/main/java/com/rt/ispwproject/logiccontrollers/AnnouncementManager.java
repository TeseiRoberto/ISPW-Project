package com.rt.ispwproject.logiccontrollers;

import com.rt.ispwproject.beans.Announcement;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.config.SessionManager;
import com.rt.ispwproject.config.UserRole;
import com.rt.ispwproject.dao.HolidayRequirementsDao;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.factories.LocationFactory;
import com.rt.ispwproject.model.*;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementManager {


    // Inserts a new announcement (holiday requirements) in the system
    // @currSession: user that is posting a new announcement
    // @announce: bean class that contains holiday requirements requested by the user
    public void postAnnouncement(Session currSession, Announcement announce) throws DbException, IllegalCallerException, IllegalArgumentException
    {
        if(!SessionManager.getInstance().isLoggedAs(currSession, UserRole.SIMPLE_USER))
            throw new IllegalCallerException("You must be logged in to post an announcement");

        Profile user = SessionManager.getInstance().getProfile(currSession);

        // Create holiday requirements using data in the given announcement
        HolidayRequirementsMetadata metadata = new HolidayRequirementsMetadata(user, announce.getDateOfPost());

        DateRange holidayDuration = new DateRange(
                announce.getHolidayDuration().getDepartureDate(),
                announce.getHolidayDuration().getReturnDate()
        );

        LocationFactory locationFactory = new LocationFactory();
        Route fromToLocation = new Route(
                locationFactory.createLocation( announce.getTransportRequirements().getDepartureLocation() ),
                locationFactory.createLocation( announce.getTransportRequirements().getArrivalLocation() )
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
    // @currSession: user that wants to remove the announcement
    // @announce: the announcement that needs to be removed
    public void removeAnnouncement(Session currSession, Announcement announce) throws DbException, IllegalCallerException, IllegalArgumentException
    {
        if(!SessionManager.getInstance().isLoggedAs(currSession, UserRole.SIMPLE_USER))
            throw new IllegalCallerException("You must be logged in to remove an announcement");

        HolidayRequirementsDao requirementsDao = new HolidayRequirementsDao();
        HolidayRequirements requirements = requirementsDao.getRequirementsById(announce.getId());
        requirementsDao.removeRequirements(requirements);
    }


    // Retrieves the announcements posted by the given user
    // @returns: list of announcements posted by the given user
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
