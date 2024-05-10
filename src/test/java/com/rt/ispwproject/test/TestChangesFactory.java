package com.rt.ispwproject.test;

import com.rt.ispwproject.config.UserRole;
import com.rt.ispwproject.factories.ChangesFactory;
import com.rt.ispwproject.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestChangesFactory {


    // In the system we represent a request of changes made by a user as a couple of holiday offers, the first offer
    // is the one made by the travel agency and the second one is the offer desired by the user.
    // To build request of changes we use a factory class, this method tries to create a request of changes in which
    // the desired offer and the agency offer are the same.
    // In such case we expect the factory to throw an exception
    @Test
    void negativeTestCreateRequestOfChanges()
    {
        Throwable e = assertThrows(IllegalArgumentException.class, () -> {

                Profile travelAgency = new Profile(0, "dummyTravelAgency", "dummyAgency@email.com", UserRole.TRAVEL_AGENCY);
                Profile user = new Profile(0, "dummyUser", "dummyUser@email.com", UserRole.SIMPLE_USER);

                HolidayOffer offer = createDummyOffer(travelAgency, user);
                ChangesFactory.getInstance().createChangesRequest(offer, offer, "");
            }
        );

        assertEquals("User cannot request changes on an offer made by himself!", e.getMessage());
    }


    // This method tries to create a request of changes in which the desired offer and the agency offer have
    // one difference, in such case we expect the factory to not throw an exception
    @Test
    void positiveTestCreateRequestOfChanges()
    {
        Profile travelAgency = new Profile(0, "dummyTravelAgency", "dummyAgency@email.com", UserRole.TRAVEL_AGENCY);
        Profile user = new Profile(0, "dummyUser", "dummyUser@email.com", UserRole.SIMPLE_USER);

        HolidayOffer agencyOffer = createDummyOffer(travelAgency, user);
        HolidayOffer desiredOffer = createDummyOffer(user, travelAgency);

        // This must not fail because accommodation offer and transport offer instances are different
        // in the 2 offers, so it's like if changes on accommodation and transport are requested

        ChangesFactory.getInstance().createChangesRequest(agencyOffer, desiredOffer, "");
    }


    // Creates an HolidayOffer instance with dummy data
    private HolidayOffer createDummyOffer(Profile offerOwner, Profile offerAddressee)
    {
        HolidayOfferMetadata metadata = new HolidayOfferMetadata(offerOwner, HolidayOfferState.PENDING, 0, offerAddressee);

        Location departureLocation = new Location("Rome");
        Location arrivalLocation = new Location("London");
        Route fromToLocation = new Route(departureLocation, arrivalLocation);

        DateRange duration = new DateRange(
            LocalDate.now().plusDays(10),
            LocalDate.now().plusDays(20)
        );

        AccommodationOffer accommodation = new AccommodationOffer(
            AccommodationType.HOTEL,
            "dummyHotel",
            5,
            arrivalLocation,
            1,
            duration,
            1000
        );

        TransportOffer transport = new TransportOffer(
                TransportType.AIRPLANE,
                "dummyAirlines",
                5,
                2,
                1000,
                fromToLocation,
                duration
        );

        return new HolidayOffer(metadata, arrivalLocation, duration, 1000, accommodation, transport);
    }

}
