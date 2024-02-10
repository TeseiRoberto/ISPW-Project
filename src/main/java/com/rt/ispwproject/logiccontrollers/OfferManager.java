package com.rt.ispwproject.logiccontrollers;

import com.rt.ispwproject.apiboundaries.AccommodationSearcher;
import com.rt.ispwproject.apiboundaries.TransportSearcher;
import com.rt.ispwproject.beans.*;
import com.rt.ispwproject.config.SessionManager;
import com.rt.ispwproject.dao.HolidayOfferDao;
import com.rt.ispwproject.exceptions.ApiException;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.model.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OfferManager {

    private static final double  TRAVEL_AGENCY_PERCENTAGE = 0.05;    // Percentage of profit for the travel agency if the offer is accepted by the user


    // Inserts a new offer (intended for the given announcement) in the system
    public void makeOfferToUser(Session currSession, Announcement announce, Offer offer) throws DbException, IllegalArgumentException
    {
        Profile user = SessionManager.getInstance().getProfile(currSession);
        if(user == null)                                        // Check if currSession is valid
            throw new IllegalArgumentException("You must be logged in to make an offer to another user");

        HolidayOfferMetadata metadata = new HolidayOfferMetadata(announce.getId(), user.getUsername(), user.getUserId());
        DateRange holidayDuration = new DateRange(offer.getDepartureDate(), offer.getReturnDate());
        Location destination = new Location(offer.getDestination());
        Location departureLocation = new Location(offer.getTransportOffer().getDepartureLocation());
        Location accommodationLocation = new Location(offer.getAccommodationOffer().getAddress());

        AccommodationOffer accommodation = new AccommodationOffer(
                offer.getAccommodationOffer().getType(),
                offer.getAccommodationOffer().getName(),
                accommodationLocation,
                offer.getAccommodationOffer().getQuality(),
                offer.getAccommodationOffer().getNumOfRooms(),
                holidayDuration,
                offer.getAccommodationOffer().getPricePerNight()
        );

        TransportOffer transportOffer = new TransportOffer(
                offer.getTransportOffer().getType(),
                offer.getTransportOffer().getCompanyName(),
                offer.getTransportOffer().getQuality(),
                new Route(departureLocation, destination),
                offer.getTransportOffer().getNumOfTravelers(),
                offer.getTransportOffer().getPricePerTraveler(),
                holidayDuration
        );

        HolidayOffer holidayOffer = new HolidayOffer(metadata, destination, holidayDuration, offer.getPrice(), accommodation, transportOffer);
        HolidayOfferDao offerDao = new HolidayOfferDao();
        offerDao.postOffer(holidayOffer);
    }


    // Retrieves all the offers made by the given user (the user must be a travel agency otherwise an empty list is returned)
    public List<Offer> getMyOffers(Session currSession) throws DbException, IllegalArgumentException
    {
        Profile user = SessionManager.getInstance().getProfile(currSession);
        if(user == null)                                        // Check if currSession is valid
            throw new IllegalArgumentException("You must be logged in to retrieve your offers");

        // TODO: Add implementation...
        /*if(currUser.getUserRole() != UserRole.TRAVEL_AGENCY)
            return List.of();

        List<HolidayOffer> holidayOffers = null;
        HolidayOfferDao offerDao = new HolidayOfferDao();
        holidayOffers = offerDao.getOffersMadeByUser(currUser.getUserId());

        ArrayList<Offer> offers = new ArrayList<>();
        try {
            for(HolidayOffer o : holidayOffers)                 // Convert model classes to beans
                offers.add(o.toOffer());

        } catch (IllegalArgumentException e) {
            throw new DbException("Db returned invalid data for an offer:\n" + e.getMessage());
        }

        return offers;*/
        System.out.println("getMyOffers() INVOKED!");
        return List.of();
    }


    // Retrieves all the offers made by travel agencies to the given announcement
    public List<Offer> getOffersForAnnouncement(Session currSession, Announcement announce) throws DbException, IllegalArgumentException
    {
        Profile user = SessionManager.getInstance().getProfile(currSession);
        if(user == null)
            throw new IllegalArgumentException("You must be logged in to retrieve the offers received for your announcement");

        List<HolidayOffer> holidayOffers = null;
        HolidayOfferDao offerDao = new HolidayOfferDao();
        holidayOffers = offerDao.getOffersForHolidayRequirements(announce.getId());

        ArrayList<Offer> offers = new ArrayList<>();
        try {

            AccommodationSearcher searcher = new AccommodationSearcher();
            for(HolidayOffer o : holidayOffers)                 // Convert model classes to beans and retrieve accommodations images
            {
                List<URL> accommodationImgs = searcher.getAccommodationImages(o.getAccommodation().getAccommodationId());

                Offer newOffer = o.toOffer();
                newOffer.getAccommodationOffer().setImagesLinks(accommodationImgs);
                offers.add(newOffer);
            }

        } catch (IllegalArgumentException e) {
            throw new DbException("Db returned invalid data for an offer:\n" + e.getMessage());
        }

        return offers;
    }


    // Queries the accommodation API and returns a list of the available accommodations
    public List<Accommodation> getAvailableAccommodations(String destination, Duration checkInOutDates, int numOfRooms) throws ApiException
    {
        AccommodationSearcher searcher = new AccommodationSearcher();
        return searcher.searchAccommodations(destination, checkInOutDates, numOfRooms);
    }


    // Queries the transportation API and returns a list of the available transports
    public List<Transport> getAvailableTransports(String departureLocation, String destination,
                                                  Duration departureAndReturnDates, int numOfTravelers) throws ApiException
    {
        TransportSearcher searcher = new TransportSearcher();
        return searcher.searchTransports(departureLocation, destination, departureAndReturnDates, numOfTravelers);
    }


    // Calculates the price of the offer according to the accommodation and transport prices and the travel agency percentage
    public int calculateOfferPrice(Accommodation accommodation, Transport transport) throws IllegalArgumentException
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
