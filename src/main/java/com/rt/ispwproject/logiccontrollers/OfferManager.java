package com.rt.ispwproject.logiccontrollers;

import com.rt.ispwproject.apiboundaries.AccommodationSearcher;
import com.rt.ispwproject.apiboundaries.TransportSearcher;
import com.rt.ispwproject.beans.*;
import com.rt.ispwproject.config.SessionManager;
import com.rt.ispwproject.config.UserRole;
import com.rt.ispwproject.dao.ChangesRequestDao;
import com.rt.ispwproject.dao.HolidayOfferDao;
import com.rt.ispwproject.dao.ProfileDao;
import com.rt.ispwproject.exceptions.ApiException;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.factories.LocationFactory;
import com.rt.ispwproject.model.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OfferManager {

    private static final double  TRAVEL_AGENCY_PERCENTAGE = 0.05;    // Percentage of profit for the travel agency if the offer is accepted by the user


    // Inserts a new offer in the system
    // @currSession: user that is inserting a new offer in the system
    // @announce: announcement (holiday requirements) for which the new offer is intended
    // @offer: bean class that contains the details of the holiday offer
    public void makeOfferToUser(Session currSession, Announcement announce, Offer offer) throws DbException, IllegalCallerException, IllegalArgumentException
    {
        if(!SessionManager.getInstance().isLoggedAs(currSession, UserRole.TRAVEL_AGENCY))
            throw new IllegalCallerException("You must be logged in to make an offer to another user");

        // Retrieve the profiles of the offer owner and announcement owner
        ProfileDao profileDao = new ProfileDao();
        Profile announcementOwner = profileDao.getProfileByUsername(offer.getRelativeAnnouncementOwnerUsername());
        Profile offerOwner = SessionManager.getInstance().getProfile(currSession);

        HolidayOffer newOffer = createOffer(offerOwner, offer, announcementOwner, announce.getId());
        HolidayOfferDao offerDao = new HolidayOfferDao();
        offerDao.postOffer(newOffer);
    }


    // Retrieves all the offers made by the travel agency associated to the given session
    // @returns: list of offers made by the given user
    public List<Offer> getMyOffers(Session currSession) throws DbException, IllegalCallerException, IllegalArgumentException
    {
        if(!SessionManager.getInstance().isLoggedAs(currSession, UserRole.TRAVEL_AGENCY))
            throw new IllegalCallerException("You must be logged in to retrieve your offers");

        Profile user = SessionManager.getInstance().getProfile(currSession);
        List<HolidayOffer> holidayOffers;
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
    // @currSession: user that wants to retrieve the offers
    // @announce: announcement for which we want to retrieve offers
    public List<Offer> getOffersForAnnouncement(Session currSession, Announcement announce) throws DbException, IllegalCallerException, IllegalArgumentException
    {
        if(!SessionManager.getInstance().isLoggedAs(currSession, UserRole.SIMPLE_USER))
            throw new IllegalCallerException("You must be logged in to retrieve the offers received for your announcement");

        List<HolidayOffer> holidayOffers;
        ArrayList<Offer> offerBeans = new ArrayList<>();

        HolidayOfferDao offerDao = new HolidayOfferDao();
        holidayOffers = offerDao.getOffersForHolidayRequirements(announce.getId());
        try {

            AccommodationSearcher searcher = new AccommodationSearcher();
            for(HolidayOffer o : holidayOffers)                 // Convert model classes to beans and retrieve accommodations images
            {
                List<URL> accommodationImgs = searcher.getAccommodationImages();

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
    // @currSession: user that is rejecting the offer
    // @offer: offer that needs to be marked as rejected
    public void rejectOffer(Session currSession, Offer offer) throws DbException, IllegalCallerException
    {
        if(!SessionManager.getInstance().isLoggedAs(currSession, UserRole.SIMPLE_USER))
            throw new IllegalCallerException("You must be logged in to reject an offer");

        HolidayOfferDao offerDao = new HolidayOfferDao();
        HolidayOffer currOffer = offerDao.getOfferById(offer.getId());
        currOffer.getMetadata().setOfferState(HolidayOfferState.REJECTED);
        offerDao.updateOffer(currOffer);
    }


    // Updates the original offer with the new one and removes the given request of changes from the system
    // @currSession: user that is making a counteroffer
    // @offer: bean class that contains the details of the counteroffer
    // @requestedChanges: bean class that contains the changes requested by the user on the original holiday offer
    public void makeCounteroffer(Session currSession, Offer offer, ChangesOnOffer requestedChanges) throws DbException, IllegalCallerException, IllegalArgumentException
    {
        if(!SessionManager.getInstance().isLoggedAs(currSession, UserRole.TRAVEL_AGENCY))
            throw new IllegalCallerException("You must be logged in to make a counteroffer");

        HolidayOfferDao offerDao = new HolidayOfferDao();
        HolidayOffer originalOffer = offerDao.getOfferById(requestedChanges.getRelativeOfferId());

        ChangesRequestDao changesDao = new ChangesRequestDao();
        ChangesRequest changes = changesDao.getChangesRequestById(requestedChanges.getId());

        // Create an updated holiday offer
        HolidayOffer counteroffer = createOffer(
                SessionManager.getInstance().getProfile(currSession),
                offer,
                originalOffer.getMetadata().getRelativeRequirementsOwner(),
                originalOffer.getMetadata().getRelativeRequirementsId()
        );

        // Set counteroffer metadata so that they are equal to the ones of the original offer
        counteroffer.setMetadata(originalOffer.getMetadata());
        counteroffer.getMetadata().setOfferState(HolidayOfferState.PENDING_WITH_CHANGES);
        counteroffer.getAccommodationOffer().setId(originalOffer.getAccommodationOffer().getId());
        counteroffer.getTransportOffer().setId(originalOffer.getTransportOffer().getId());

        // Save the updated offer and remove the request of changes from the system
        offerDao.updateOffer(counteroffer);
        changesDao.removeRequest(changes);

        // Update bean class status so that it can be displayed correctly in the view
        offer.setOfferStatus(HolidayOfferState.PENDING_WITH_CHANGES.toViewType());
    }


    // Queries the accommodation API to get the available accommodations
    // @returns: list of accommodations that satisfies the given criteria
    public List<Accommodation> getAvailableAccommodations(String destination, Duration checkInOutDates, int numOfRooms) throws ApiException
    {
        AccommodationSearcher searcher = new AccommodationSearcher();
        return searcher.searchAccommodationOffers(destination, checkInOutDates, numOfRooms);
    }


    // Queries the transportation API to get the available transports
    // @returns: list of transportations that satisfies the given criteria
    public List<Transport> getAvailableTransports(String departureLocation, String arrivalLocation,
                                                  Duration departureAndReturnDates, int numOfTravelers) throws ApiException
    {
        TransportSearcher searcher = new TransportSearcher();
        return searcher.searchTransportOffers(departureLocation, arrivalLocation, departureAndReturnDates, numOfTravelers);
    }


    // Calculates the price of the offer according to the accommodation and transport prices and the travel agency percentage
    // @accommodation: bean class that contains details about the accommodation
    // @transport: bean class that contains details about the transport
    // @returns: price of the offer with the given accommodation and transport
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


    // Creates an HolidayOffer instance using the given data
    // @offerOwner: profile of the user that is making the offer
    // @offerBean: bean class that contains the details of the offer
    // @announcementOwner: profile of the user that posted the announcement for which the offer is intended
    // @announcementId: id of the announcement for which the offer is intended
    private HolidayOffer createOffer(Profile offerOwner, Offer offerBean, Profile announcementOwner, int announcementId) throws IllegalArgumentException
    {
        HolidayOfferMetadata metadata = new HolidayOfferMetadata(
                offerOwner,
                HolidayOfferState.PENDING,
                announcementId,
                announcementOwner
        );

        DateRange holidayDuration = new DateRange(
                offerBean.getHolidayDuration().getDepartureDate(),
                offerBean.getHolidayDuration().getReturnDate()
        );

        Location destination = LocationFactory.getInstance().createLocation(offerBean.getDestination());

        Location accommodationLocation = LocationFactory.getInstance().createLocation(offerBean.getAccommodationOffer().getAddress());

        Route transportRoute = new Route(
                LocationFactory.getInstance().createLocation(offerBean.getTransportOffer().getDepartureLocation()),
                LocationFactory.getInstance().createLocation(offerBean.getTransportOffer().getArrivalLocation())
        );

        // TODO: We should check that data used to build the accommodation and transport offer are valid (???)
        AccommodationOffer accommodationOffer = new AccommodationOffer(
                AccommodationType.fromViewType(offerBean.getAccommodationOffer().getType()),
                offerBean.getAccommodationOffer().getName(),
                offerBean.getAccommodationOffer().getAccommodationId(),
                offerBean.getAccommodationOffer().getQuality(),
                accommodationLocation,
                offerBean.getAccommodationOffer().getNumOfRooms(),
                holidayDuration,
                offerBean.getAccommodationOffer().getPrice()
        );

        TransportOffer transportOffer = new TransportOffer(
                TransportType.fromViewType(offerBean.getTransportOffer().getType()),
                offerBean.getTransportOffer().getCompanyName(),
                offerBean.getTransportOffer().getCompanyId(),
                offerBean.getTransportOffer().getQuality(),
                offerBean.getTransportOffer().getNumOfTravelers(),
                offerBean.getTransportOffer().getPricePerTraveler(),
                transportRoute,
                holidayDuration
        );

        // Now we can build the new holiday offer
        return new HolidayOffer(metadata, destination, holidayDuration, offerBean.getPrice(), accommodationOffer, transportOffer);
    }

}
