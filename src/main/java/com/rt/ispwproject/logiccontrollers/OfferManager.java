package com.rt.ispwproject.logiccontrollers;

import com.rt.ispwproject.apiboundaries.AccommodationSearcher;
import com.rt.ispwproject.apiboundaries.TransportSearcher;
import com.rt.ispwproject.beans.*;
import com.rt.ispwproject.config.SessionManager;
import com.rt.ispwproject.config.UserRole;
import com.rt.ispwproject.dao.HolidayOfferDao;
import com.rt.ispwproject.dao.ProfileDao;
import com.rt.ispwproject.exceptions.ApiException;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.model.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OfferManager {

    private static final double  TRAVEL_AGENCY_PERCENTAGE = 0.05;    // Percentage of profit for the travel agency if the offer is accepted by the user


    // Inserts a new offer (intended for the given announcement) in the system
    public void makeOfferToUser(Session currSession, Announcement announce, Offer offer) throws DbException, IllegalCallerException, IllegalArgumentException
    {
        Profile user = SessionManager.getInstance().getProfile(currSession);
        if(user == null)                                        // Check if currSession is valid
            throw new IllegalCallerException("You must be logged in to make an offer to another user");

        if(user.getUserRole() != UserRole.TRAVEL_AGENCY)
            throw new IllegalCallerException("Only travel agencies can make offers to users");

        // Retrieve user that posted the announcement for which the offer is intended
        ProfileDao profileDao = new ProfileDao();
        Profile announcementOwner = profileDao.getProfile(announce.getOwnerId());

        HolidayOfferMetadata metadata = new HolidayOfferMetadata(user, HolidayOfferState.PENDING, announce.getId(), announcementOwner);
        DateRange holidayDuration = new DateRange(offer.getDepartureDate(), offer.getReturnDate());
        Location destination = new Location(offer.getDestination());
        Location departureLocation = new Location(offer.getTransportOffer().getDepartureLocation());
        Location accommodationLocation = new Location(offer.getAccommodationOffer().getAddress());

        AccommodationOffer accommodation = new AccommodationOffer(
                AccommodationType.fromViewType(offer.getAccommodationOffer().getType()),
                offer.getAccommodationOffer().getName(),
                accommodationLocation,
                offer.getAccommodationOffer().getQuality(),
                offer.getAccommodationOffer().getNumOfRooms(),
                holidayDuration,
                offer.getAccommodationOffer().getPricePerNight()
        );

        TransportOffer transportOffer = new TransportOffer(
                TransportType.fromViewType(offer.getTransportOffer().getType()),
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


    // Retrieves all the offers made by the travel agency associated to the given session
    public List<Offer> getMyOffers(Session currSession) throws DbException, IllegalCallerException, IllegalArgumentException
    {
        Profile user = SessionManager.getInstance().getProfile(currSession);
        if(user == null)                                        // Check if currSession is valid
            throw new IllegalCallerException("You must be logged in to retrieve your offers");

        if(user.getUserRole() != UserRole.TRAVEL_AGENCY)
            throw new IllegalCallerException("Only travel agencies can retrieve offers they made");

        List<HolidayOffer> holidayOffers = null;
        ArrayList<Offer> offerBeans = new ArrayList<>();

        HolidayOfferDao offerDao = new HolidayOfferDao();
        holidayOffers = offerDao.getOffersMadeByUser(user.getUserId());
        try {
            for(HolidayOffer o : holidayOffers)                 // Convert model classes to beans
                offerBeans.add(o.toOfferBean());

        } catch (IllegalArgumentException e) {
            throw new DbException("Db returned invalid data for an offer:\n" + e.getMessage());
        }

        return offerBeans;
    }


    // Retrieves all the offers made by travel agencies to the given announcement
    public List<Offer> getOffersForAnnouncement(Session currSession, Announcement announce) throws DbException, IllegalCallerException, IllegalArgumentException
    {
        Profile user = SessionManager.getInstance().getProfile(currSession);
        if(user == null)
            throw new IllegalCallerException("You must be logged in to retrieve the offers received for your announcement");

        List<HolidayOffer> holidayOffers = null;
        ArrayList<Offer> offerBeans = new ArrayList<>();

        HolidayOfferDao offerDao = new HolidayOfferDao();
        holidayOffers = offerDao.getOffersForHolidayRequirements(announce.getId());
        try {

            AccommodationSearcher searcher = new AccommodationSearcher();
            for(HolidayOffer o : holidayOffers)                 // Convert model classes to beans and retrieve accommodations images
            {
                List<URL> accommodationImgs = searcher.getAccommodationImages(o.getAccommodation().getAccommodationId());

                Offer newOffer = o.toOfferBean();
                newOffer.getAccommodationOffer().setImagesLinks(accommodationImgs);
                offerBeans.add(newOffer);
            }

        } catch (IllegalArgumentException e) {
            throw new DbException("Db returned invalid data for an offer:\n" + e.getMessage());
        }

        return offerBeans;
    }


    // Marks the given offer as rejected
    public void rejectOffer(Session currSession, Offer currOffer) throws DbException, IllegalCallerException
    {
        Profile user = SessionManager.getInstance().getProfile(currSession);
        if(user == null)
            throw new IllegalCallerException("You must be logged in to reject an offer");

        if(user.getUserRole() != UserRole.SIMPLE_USER)
            throw new IllegalCallerException("Only simple users can reject offers");

        HolidayOfferDao offerDao = new HolidayOfferDao();
        offerDao.updateOfferState(currOffer.getId(), HolidayOfferState.REJECTED);
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
