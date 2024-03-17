package com.rt.ispwproject.logiccontrollers;

import com.rt.ispwproject.beans.ChangesOnOffer;
import com.rt.ispwproject.beans.Offer;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.config.SessionManager;
import com.rt.ispwproject.config.UserRole;
import com.rt.ispwproject.dao.ChangesRequestDao;
import com.rt.ispwproject.dao.HolidayOfferDao;
import com.rt.ispwproject.dao.ProfileDao;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.factories.AccommodationFactory;
import com.rt.ispwproject.factories.LocationFactory;
import com.rt.ispwproject.factories.TransportFactory;
import com.rt.ispwproject.model.*;

public class ChangesManager {


    // Sends the given request of changes to the travel agency that made the given offer
    public void requestChangesOnOffer(Session currSession, ChangesOnOffer changes, Offer currOffer) throws DbException, IllegalArgumentException
    {
        if(!SessionManager.getInstance().isLoggedAs(currSession, UserRole.SIMPLE_USER))
            throw new IllegalCallerException("You must be logged in to request changes on an offer");

        // Retrieve profiles of the user that is requesting changes and the user that made the offer
        Profile requestOwner = SessionManager.getInstance().getProfile(currSession);
        ProfileDao profileDao = new ProfileDao();
        Profile offerOwner = profileDao.getProfileByUsername(currOffer.getBidderUsername());

        // Retrieve the holiday offer on which changes are requested
        HolidayOfferDao offerDao = new HolidayOfferDao();
        HolidayOffer offer = offerDao.getOfferById(currOffer.getId());

        ChangesRequest request = buildRequest(requestOwner, changes, offerOwner, offer.getMetadata().getOfferId());

        if(!isRequestValid(request, offer))
            throw new IllegalArgumentException("No change has been specified");

        ChangesRequestDao changesDao = new ChangesRequestDao();
        changesDao.postRequest(request);
    }


    // Retrieves the changes requested by user on the given offer
    public ChangesOnOffer getRequestedChangesOnOffer(Session currSession, Offer currOffer) throws DbException
    {
        if(!SessionManager.getInstance().isLoggedAs(currSession, UserRole.TRAVEL_AGENCY))
            throw new IllegalCallerException("You must be logged in to retrieve requests of changes on an offer");

        ChangesRequestDao changesDao = new ChangesRequestDao();
        ChangesRequest request = changesDao.getChangesRequestForOffer(currOffer.getId());

        return request == null ? null : request.toChangesOnOfferBean();
    }


    // Returns a ChangesRequest instance that contains the changes specified in the given bean class
    private ChangesRequest buildRequest(Profile requestOwner, ChangesOnOffer changes, Profile offerOwner, int offerId) throws IllegalArgumentException
    {
        ChangesRequestMetadata metadata = new ChangesRequestMetadata(requestOwner, offerId, offerOwner);
        ChangesRequest newRequest = new ChangesRequest(metadata, changes.getChangesDescription());

        if(changes.isDurationChangeRequired())              // Set changes on holiday duration
        {
            newRequest.setHolidayDuration(
                    new DateRange(
                            changes.getHolidayDuration().getDepartureDate(),
                            changes.getHolidayDuration().getReturnDate()
                    )
            );
        }

        if(changes.isPriceChangeRequired())                 // Set changes on holiday price
            newRequest.setPrice(changes.getPrice());

        if(changes.isAccommodationChangeRequired())         // Set changes on accommodation
        {
            AccommodationChangesRequest accommodationChanges = AccommodationFactory.getInstance().createChangesRequest(
                    AccommodationType.fromViewType(changes.getAccommodationChanges().getType()),
                    changes.getAccommodationChanges().getQuality(),
                    changes.getAccommodationChanges().getNumOfRooms()
            );
            newRequest.setAccommodationChanges(accommodationChanges);
        }

        if(changes.isTransportChangeRequired())             // Set changes on transport
        {
            Route fromToLocation = new Route(
                    LocationFactory.getInstance().createLocation(changes.getTransportChanges().getDepartureLocation()),
                    LocationFactory.getInstance().createLocation(changes.getTransportChanges().getArrivalLocation())
            );

            TransportChangesRequest transportChanges = TransportFactory.getInstance().createChangesRequest(
                    TransportType.fromViewType(changes.getTransportChanges().getType()),
                    changes.getTransportChanges().getQuality(),
                    fromToLocation,
                    changes.getTransportChanges().getNumOfTravelers()
            );
            newRequest.setTransportChanges(transportChanges);
        }

        return newRequest;
    }


    // Checks that the given changes request is a valid request for the given holiday offer.
    // A changes request is valid if it contains at least one change and the value specified (for the change) differs from
    // the value in the relative holiday offer.
    private boolean isRequestValid(ChangesRequest request, HolidayOffer offer)
    {
        boolean isValid = false;

        // Check for changes on price
        if(request.isPriceChangeRequired() && offer.getPrice() != request.getPrice())
            isValid = true;

        // Check for changes on holiday duration
        if(request.isHolidayDurationChangeRequired() && !request.getHolidayDuration().isEqual(offer.getHolidayDuration()))
            isValid = true;

        // Check for changes on the accommodation
        if(request.isAccommodationChangeRequired())
            isValid = true;

        // Check for changes on the transport
        if(request.isTransportChangeRequired())
            isValid = true;

        return isValid;
    }

}
