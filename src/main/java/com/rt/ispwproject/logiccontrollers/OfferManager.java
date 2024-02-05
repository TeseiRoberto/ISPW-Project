package com.rt.ispwproject.logiccontrollers;

import com.rt.ispwproject.beans.*;
import com.rt.ispwproject.dao.HolidayOfferDao;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.jfxwidgets.AccommodationOfferGfxElement;
import com.rt.ispwproject.model.*;

import java.time.LocalDate;
import java.util.List;

public class OfferManager {

    private static final double  TRAVEL_AGENCY_PERCENTAGE = 0.05;    // Percentage of profit for the travel agency if the offer is accepted by the user


    // Inserts a new offer (intended for the given announcement) in the system
    public void makeOfferToUser(Session currUser, Announcement announce, Offer offer) throws DbException, IllegalArgumentException
    {
        HolidayMetadata metadata = new HolidayMetadata(0, offer.getBidder(), LocalDate.now(), 0);
        DateRange holidayDuration = new DateRange(offer.getDepartureDate(), offer.getReturnDate());
        Location destination = new Location(offer.getDestination());
        Location departureLocation = new Location(offer.getTransportOffer().getDepartureLocation());
        Location accommodationLocation = new Location(offer.getAccommodationOffer().getAddress());


        Accommodation accommodation = new Accommodation(
                offer.getAccommodationOffer().getType(),
                offer.getAccommodationOffer().getName(),
                accommodationLocation,
                offer.getAccommodationOffer().getQuality(),
                offer.getAccommodationOffer().getNumOfRooms(),
                holidayDuration,
                offer.getAccommodationOffer().getPricePerNight()
        );

        Transport transport = new Transport(
                offer.getTransportOffer().getType(),
                offer.getTransportOffer().getCompanyName(),
                offer.getTransportOffer().getQuality(),
                new Route(departureLocation, destination),
                offer.getTransportOffer().getNumOfTravelers(),
                offer.getTransportOffer().getPricePerTraveler(),
                holidayDuration
        );

        HolidayOffer holidayOffer = new HolidayOffer(metadata, destination, holidayDuration, offer.getPrice(), accommodation, transport);
        HolidayOfferDao offerDao = new HolidayOfferDao();
        offerDao.postOffer(currUser.getUserId(), announce.getId(), holidayOffer);
    }


    // Queries the accommodation API and returns a list of the available accommodations
    public List<AccommodationOffer> getAvailableAccommodations(String destination, Duration checkInOutDates, int numOfRooms)
    {
        // TODO: Need to request the list of the available accommodations to the Accommodation API,
        //  the travel agency will then select one accommodation as the chosen accommodation offer.
        //  (Note: the accommodation API must return for each accommodation a list of links to the accommodation images)
        //  For now we simply build a list dummy offers...

        AccommodationOffer offer1 = new AccommodationOffer(AccommodationType.HOTEL, "Dummy accommodation", "dummy road 124", 3, 1, 100, 1000);
        AccommodationOffer offer2 = new AccommodationOffer(AccommodationType.HOTEL, "Unreal Hotel", "Unreal avenue 75", 5, 1, 200, 2000);
        AccommodationOffer offer3 = new AccommodationOffer(AccommodationType.HOTEL, "Not a real hotel", "Fake street 15", 2, 1, 85, 950);

        offer2.setImagesLinks(List.of(AccommodationOfferGfxElement.class.getResource("hotelRoomImage.jpg")));

        return List.of(offer1, offer2, offer3);
    }


    // Queries the transportation API and returns a list of the available transports
    public List<TransportOffer> getAvailableTransports(String departureLocation, String destination, Duration departureAndReturnDates, int numOfTravelers)
    {
        // TODO: Need to request the list of the available transports to the Transportations API,
        //  the travel agency will then select one transport as the chosen transport offer
        //  For now we simply build a list dummy offers...

        return List.of(
                new TransportOffer(TransportType.AIRPLANE, "Fake Air", 3, "unreal place", 1, 100),
                new TransportOffer(TransportType.BUS, "Magic buses", 2, "unreal place", 1, 50),
                new TransportOffer(TransportType.AIRPLANE, "Banzai air", 4, "unreal place", 1, 250),
                new TransportOffer(TransportType.TRAIN, "Magic trains", 3, "unreal place", 1, 125)
        );
    }


    // Calculates the price of the offer according to the accommodation and transport prices and the travel agency percentage
    public int calculateOfferPrice(AccommodationOffer accommodation, TransportOffer transport) throws IllegalArgumentException
    {
        int price = 0;
        if(accommodation != null)
            price += accommodation.getPrice();

        if(transport != null)
            price += transport.getPrice();

        price += (int) (price * TRAVEL_AGENCY_PERCENTAGE);
        if(price <= 0)
            throw new IllegalArgumentException("Offer price cannot be negative or zero!");

        return price;
    }

}
