package com.rt.ispwproject.test;

import com.rt.ispwproject.beans.Announcement;
import com.rt.ispwproject.beans.Duration;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.config.UserRole;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.logiccontrollers.AnnouncementManager;
import com.rt.ispwproject.model.AccommodationType;
import com.rt.ispwproject.model.TransportType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestAnnouncementManager {

    // Try to post an announcement and verify that the announcement gets inserted in the system
    @Test
    public void testPostAnnouncement() throws IllegalArgumentException, DbException
    {
        Session testSession = createTestSession();
        Announcement testAnnounce = createTestAnnouncement(testSession);

        // Post new announcement
        AnnouncementManager announcementManager = new AnnouncementManager();
        announcementManager.postAnnouncement(testSession, testAnnounce);

        // Check that the announcement was posted correctly
        List<Announcement> announcementList = announcementManager.getMyAnnouncements(testSession);
        assertTrue(announcementExistsInList(testAnnounce, announcementList));
    }


    // Checks that the given announcement exists in the given announcements list, returns true if it does false otherwise
    private boolean announcementExistsInList(Announcement announce, List<Announcement> list)
    {
        for(Announcement a : list)
        {
            if(!announce.getOwner().equals(a.getOwner()))
                continue;

            if(!announce.getDestination().equals(a.getDestination()))
                continue;

            if(!announce.getHolidayDescription().equals(a.getHolidayDescription()))
                continue;

            if(announce.getAvailableBudget() != a.getAvailableBudget())
                continue;

            if(!announce.getDateOfPost().equals(a.getDateOfPost()))
                continue;

            if(!announce.getHolidayDuration().getStartDate().equals(a.getHolidayDuration().getStartDate()))
                continue;

            if(!announce.getHolidayDuration().getEndDate().equals(a.getHolidayDuration().getEndDate()))
                continue;

            if(announce.getAccommodationType() != a.getAccommodationType())
                continue;

            if(announce.getAccommodationQuality() != a.getAccommodationQuality())
                continue;

            if(announce.getNumOfRoomsRequired() != a.getNumOfRoomsRequired())
                continue;

            if(announce.getTransportType() != a.getTransportType())
                continue;

            if(announce.getTransportQuality() != a.getTransportQuality())
                continue;

            if(announce.getNumOfTravelers() != a.getNumOfTravelers())
                continue;

            if(!announce.getDepartureLocation().equals(a.getDepartureLocation()))
                continue;

            return true;
        }

        return false;
    }


    // Creates a test session
    private Session createTestSession() throws IllegalArgumentException
    {
        return new Session(4, "testUser", "test_user@email.com", UserRole.SIMPLE_USER);
    }


    // Creates a test announcement
    private Announcement createTestAnnouncement(Session user) throws IllegalArgumentException
    {
        Announcement announce = new Announcement();
        announce.setOwner(user.getUsername());
        announce.setDestination("London");
        announce.setHolidayDescription("This is a test announcement");
        announce.setAvailableBudget(3000);
        announce.setAccommodationType(AccommodationType.HOTEL);
        announce.setAccommodationQuality(4);
        announce.setNumOfRoomsRequired(1);
        announce.setTransportType(TransportType.AIRPLANE);
        announce.setTransportQuality(3);
        announce.setNumOfTravelers(1);
        announce.setDepartureLocation("Rome");

        Duration duration = new Duration(LocalDate.now().plusDays(31), LocalDate.now().plusDays(41));
        announce.setHolidayDuration(duration);

        return announce;
    }


}
