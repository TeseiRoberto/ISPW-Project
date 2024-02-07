package com.rt.ispwproject.logiccontrollers;

import com.rt.ispwproject.apiboundaries.AccommodationSearcher;
import com.rt.ispwproject.apiboundaries.TransportSearcher;
import com.rt.ispwproject.beans.*;
import com.rt.ispwproject.dao.HolidayOfferDao;
import com.rt.ispwproject.exceptions.ApiException;
import com.rt.ispwproject.exceptions.DbException;
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
    public List<AccommodationOffer> getAvailableAccommodations(String destination, Duration checkInOutDates, int numOfRooms) throws ApiException
    {
        AccommodationSearcher searcher = new AccommodationSearcher();
        return searcher.searchAccommodations(destination, checkInOutDates, numOfRooms);
    }


    // Queries the transportation API and returns a list of the available transports
    public List<TransportOffer> getAvailableTransports(String departureLocation, String destination,
                                                       Duration departureAndReturnDates, int numOfTravelers) throws ApiException
    {
        TransportSearcher searcher = new TransportSearcher();
        return searcher.searchTransports(departureLocation, destination, departureAndReturnDates, numOfTravelers);
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
