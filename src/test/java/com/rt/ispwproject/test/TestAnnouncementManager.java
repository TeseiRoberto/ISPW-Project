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


    // Tries to insert an announcement in the system and verify that the announcement gets inserted correctly
    @Test
    void testPostAnnouncement() throws IllegalArgumentException, DbException
    {
        Session testSession = createDummySession();
        Announcement announce = createDummyAnnouncement(testSession);

        // Post new announcement
        AnnouncementManager announcementManager = new AnnouncementManager();
        announcementManager.postAnnouncement(testSession, announce);

        // Check that the announcement was posted correctly
        List<Announcement> announcementList = announcementManager.getMyAnnouncements(testSession);
        assertTrue(announcementExistsInList(announce, announcementList));
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

            if(!announce.getHolidayDuration().isEqual(a.getHolidayDuration()))
                continue;

            if(!announce.getAccommodationRequirements().isEqual(a.getAccommodationRequirements()))
                continue;

            if(!announce.getTransportRequirements().isEqual(a.getTransportRequirements()))
                continue;

            return true;
        }

        return false;
    }


    // Creates a session associated to a test account
    private Session createDummySession() throws IllegalArgumentException, DbException
    {
        return SessionManager.getInstance().createNewSession("testUser", "0000");
    }


    // Creates a test announcement
    private Announcement createDummyAnnouncement(Session user) throws IllegalArgumentException
    {
        Accommodation accommodationReq = new Accommodation("Hotel", 4, 1);
        Transport transportReq = new Transport("Airplane", 3, "Rome", "London", 1);

        return new Announcement(
                user.getUsername(),
                "This is a test announcement",
                3000,
                new Duration(LocalDate.now().plusDays(31), LocalDate.now().plusDays(41)),
                accommodationReq,
                transportReq
        );
    }

}
