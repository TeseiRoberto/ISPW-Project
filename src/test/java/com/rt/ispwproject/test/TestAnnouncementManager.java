package com.rt.ispwproject.test;

import com.rt.ispwproject.beans.*;
import com.rt.ispwproject.config.SessionManager;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.logiccontrollers.AnnouncementManager;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TestAnnouncementManager {

    // Try to post an announcement and verify that the announcement gets inserted in the system
    @Test
    void testPostAnnouncement() throws IllegalArgumentException, DbException
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
            if(!announce.getOwnerUsername().equals(a.getOwnerUsername()))
                continue;

            if(!announce.getDestination().equals(a.getDestination()))
                continue;

            if(!announce.getHolidayDescription().equals(a.getHolidayDescription()))
                continue;

            if(announce.getAvailableBudget() != a.getAvailableBudget())
                continue;

            if(!announce.getDateOfPost().equals(a.getDateOfPost()))
                continue;

            if(!announce.getHolidayDuration().getDepartureDate().equals(a.getHolidayDuration().getDepartureDate()))
                continue;

            if(!announce.getHolidayDuration().getReturnDate().equals(a.getHolidayDuration().getReturnDate()))
                continue;

            if(!announce.getAccommodationRequirements().getType().equals(a.getAccommodationRequirements().getType()))
                continue;

            if(announce.getAccommodationRequirements().getQuality() != a.getAccommodationRequirements().getQuality())
                continue;

            if(announce.getAccommodationRequirements().getNumOfRooms() != a.getAccommodationRequirements().getNumOfRooms())
                continue;

            if(!announce.getTransportRequirements().getType().equals(a.getTransportRequirements().getType()))
                continue;

            if(announce.getTransportRequirements().getQuality() != a.getTransportRequirements().getQuality())
                continue;

            if(announce.getTransportRequirements().getNumOfTravelers() != a.getTransportRequirements().getNumOfTravelers())
                continue;

            if(!announce.getTransportRequirements().getDepartureLocation().equals(a.getTransportRequirements().getDepartureLocation()))
                continue;

            return true;
        }

        return false;
    }


    // Creates a test session
    private Session createTestSession() throws IllegalArgumentException, DbException
    {
        return SessionManager.getInstance().createNewSession("testUser", "0000");
    }


    // Creates a test announcement
    private Announcement createTestAnnouncement(Session user) throws IllegalArgumentException
    {
        Accommodation accommodationReq = new Accommodation("Hotel", 4, 1);
        Transport transportReq = new Transport("Airplane", 3, "Rome", 1);

        Announcement announce = new Announcement();
        announce.setOwnerUsername(user.getUsername());
        announce.setDestination("London");
        announce.setHolidayDescription("This is a test announcement");
        announce.setAvailableBudget(3000);
        announce.setAccommodationRequirements(accommodationReq);
        announce.setTransportRequirements(transportReq);

        Duration duration = new Duration(LocalDate.now().plusDays(31), LocalDate.now().plusDays(41));
        announce.setHolidayDuration(duration);

        return announce;
    }


}
