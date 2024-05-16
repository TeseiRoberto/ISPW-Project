package com.rt.ispwproject.test;

import com.rt.ispwproject.beans.*;
import com.rt.ispwproject.config.SessionManager;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.logiccontrollers.AnnouncementManager;
import com.rt.ispwproject.logiccontrollers.OfferManager;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TestOfferManager {

    @Test
    void testMakeOffer() throws DbException, RuntimeException
    {
        Session travelAgency = SessionManager.getInstance().createNewSession("testTravelAgency", "0000");

        // Retrieve the test announcement and build the dummy offer
        Announcement announce = retrieveTestAnnouncement();
        Offer offer = createDummyOffer(travelAgency, announce);

        // Send the offer
        OfferManager offerManager = new OfferManager();
        offerManager.makeOfferToUser(travelAgency, announce, offer);

        // Check that the offer has been inserted correctly in the system
        List<Offer> offersList = offerManager.getMyOffers(travelAgency);
        assertTrue(offerExistsInList(offer, offersList));
    }


    // Retrieves the test announcement posted by the testPostAnnouncement method of the TestAnnouncementManager class
    private Announcement retrieveTestAnnouncement() throws DbException
    {
        Session testUser = SessionManager.getInstance().createNewSession("testUser", "0000");

        AnnouncementManager announcementManager = new AnnouncementManager();
        List<Announcement> announcements = announcementManager.getMyAnnouncements(testUser);

        if(announcements == null || announcements.isEmpty())
            throw new RuntimeException("testUSer has not posted any announcements yet, run the TestPostAnnouncement method first!");

        return announcements.getFirst();
    }


    // Creates a dummy Offer using data contained in the given announcement
    private Offer createDummyOffer(Session offerOwner, Announcement announce) throws IllegalArgumentException
    {
        Duration duration = new Duration(
                announce.getHolidayDuration().getDepartureDate(),
                announce.getHolidayDuration().getReturnDate()
        );

        Accommodation accommodation = new Accommodation(
                announce.getAccommodationRequirements().getType(),
                "Dummy accommodation",
                "Unreal avenue 123",
                announce.getAccommodationRequirements().getQuality(),
                announce.getAccommodationRequirements().getNumOfRooms(),
                1000
        );

        Transport transport = new Transport(
                announce.getTransportRequirements().getType(),
                "dummy transport company",
                announce.getTransportRequirements().getQuality(),
                announce.getTransportRequirements().getDepartureLocation(),
                announce.getTransportRequirements().getArrivalLocation(),
                announce.getTransportRequirements().getNumOfTravelers(),
                1000
        );

        return new Offer(
                offerOwner.getUsername(),
                announce.getOwnerUsername(),
                announce.getDestination(),
                duration,
                announce.getAvailableBudget(),
                accommodation,
                transport
        );
    }


    // Checks that the given offer exists in the given offers list, returns true if it does false otherwise
    private boolean offerExistsInList(Offer offer, List<Offer> list)
    {
        for(Offer o : list)
        {
            if(!offer.getBidderUsername().equals(o.getBidderUsername()))
                continue;

            if(!offer.getDestination().equals(o.getDestination()))
                continue;

            if(offer.getPrice() != o.getPrice())
                continue;

            if(!offer.getHolidayDuration().isEqual(o.getHolidayDuration()))
                continue;

            if(!offer.getAccommodationOffer().isEqual(o.getAccommodationOffer()))
                continue;

            if(!offer.getTransportOffer().isEqual(o.getTransportOffer()))
                continue;

            return true;
        }

        return false;
    }
}
