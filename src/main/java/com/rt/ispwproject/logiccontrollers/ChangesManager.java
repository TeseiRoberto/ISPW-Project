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


    // Inserts a new request of changes in the system
    // @currSession: user that requesting changes
    // @changes: bean class that contains details about the requested changes
    // @offer: the offer on which changes are requested
    public void requestChangesOnOffer(Session currSession, ChangesOnOffer changes, Offer offer) throws DbException, IllegalArgumentException, IllegalCallerException
    {
        if(!SessionManager.getInstance().isLoggedAs(currSession, UserRole.SIMPLE_USER))
            throw new IllegalCallerException("You must be logged in to request changes on an offer");

        // Retrieve profiles of the user that is requesting changes and the user that made the offer
        ProfileDao profileDao = new ProfileDao();
        Profile offerOwner = profileDao.getProfileByUsername(offer.getBidderUsername());
        Profile requestOwner = SessionManager.getInstance().getProfile(currSession);

        // Retrieve the holiday offer on which changes are requested
        HolidayOfferDao offerDao = new HolidayOfferDao();
        HolidayOffer currOffer = offerDao.getOfferById(offer.getId());

        ChangesRequest request = createRequest(requestOwner, changes, offerOwner, currOffer.getMetadata().getOfferId());

        if(!isRequestValid(request, currOffer))
            throw new IllegalArgumentException("No change has been specified");

        ChangesRequestDao changesDao = new ChangesRequestDao();
        changesDao.postRequest(request);
    }


    // Retrieves the request of changes relative to the given offer
    // @currSession: user that wants to retrieve the request of changes
    // @offer: offer to check for changes request
    // @returns: bean class that contains details about the requested changes, null if no request of changes is available for the given offer
    public ChangesOnOffer getRequestedChangesOnOffer(Session currSession, Offer currOffer) throws DbException, IllegalArgumentException, IllegalCallerException
    {
        if(!SessionManager.getInstance().isLoggedAs(currSession, UserRole.TRAVEL_AGENCY))
            throw new IllegalCallerException("You must be logged in to retrieve requests of changes on an offer");

        ChangesRequestDao changesDao = new ChangesRequestDao();
        ChangesRequest request = changesDao.getChangesRequestForOffer(currOffer.getId());

        return request == null ? null : request.toChangesOnOfferBean();
    }


    // Updates the state of the holiday offer on which changes were requested and delete the request of changes
    // @currSession: user that is rejecting the request of changes
    // @changes: request of changes that will be deleted from the system
    public void rejectChangesRequest(Session currSession, ChangesOnOffer changes) throws DbException, IllegalCallerException
    {
        if(!SessionManager.getInstance().isLoggedAs(currSession, UserRole.TRAVEL_AGENCY))
            throw new IllegalCallerException("You must be logged in to reject a request of changes on an offer");

        // Retrieve the request of changes from db
        ChangesRequestDao changesDao = new ChangesRequestDao();
        ChangesRequest request = changesDao.getChangesRequestById(changes.getId());

        // Delete request from the db
        changesDao.removeRequest(request);

        // Retrieve holiday offer from db
        HolidayOfferDao offerDao = new HolidayOfferDao();
        HolidayOffer offer = offerDao.getOfferById(request.getMetadata().getRelativeOfferId());

        // Update state of the offer
        offer.getMetadata().setOfferState(HolidayOfferState.PENDING_WITH_REJECTED_CHANGES);
        offerDao.updateOffer(offer);
    }


    // Creates a ChangesRequest instance using the given data
    // @requestOwner: user that is requesting changes
    // @changes: bean class that contains details about the changes requested
    // @offerOwner: user that made the offer on which changes are requested
    // @offerId: id of the holiday offer on which changes are requested
    private ChangesRequest createRequest(Profile requestOwner, ChangesOnOffer changes, Profile offerOwner, int offerId) throws IllegalArgumentException
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


    // Checks that the given request of changes is a valid request for the given holiday offer.
    // A request of changes is valid if it contains at least one change and the value specified (for the change) differs from
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
