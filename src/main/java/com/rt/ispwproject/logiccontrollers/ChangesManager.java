package com.rt.ispwproject.logiccontrollers;

import com.rt.ispwproject.beans.ChangesOnOffer;
import com.rt.ispwproject.beans.Offer;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.config.SessionManager;
import com.rt.ispwproject.config.UserRole;
import com.rt.ispwproject.dao.ChangesRequestDao;
import com.rt.ispwproject.dao.HolidayOfferDao;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.factories.ChangesFactory;
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

        // Retrieve profiles of the user that is requesting changes
        Profile requestOwner = SessionManager.getInstance().getProfile(currSession);

        // Retrieve the holiday offer on which changes are requested
        HolidayOfferDao offerDao = new HolidayOfferDao();
        HolidayOffer agencyOffer = offerDao.getOfferById(offer.getId());

        // Use factory to create the request of changes
        ChangesRequest request = ChangesFactory.getInstance().createChangesRequest(agencyOffer, requestOwner, changes);

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


    // Updates the state of the holiday offer on which changes were requested and removes the request of changes from the system
    // @currSession: user that is rejecting the request of changes
    // @changes: request of changes that will be deleted from the system
    public void rejectChangesRequest(Session currSession, ChangesOnOffer changes) throws DbException, IllegalCallerException
    {
        if(!SessionManager.getInstance().isLoggedAs(currSession, UserRole.TRAVEL_AGENCY))
            throw new IllegalCallerException("You must be logged in to reject a request of changes on an offer");

        // Retrieve the request of changes from db
        ChangesRequestDao changesDao = new ChangesRequestDao();
        ChangesRequest request = changesDao.getChangesRequestById(changes.getId());

        // Retrieve holiday offer from db
        HolidayOfferDao offerDao = new HolidayOfferDao();
        HolidayOffer offer = offerDao.getOfferById(request.getRelativeOfferId());

        // Delete request from db
        changesDao.removeRequest(request);

        // Update state of the offer
        offer.getMetadata().setOfferState(HolidayOfferState.PENDING_WITH_REJECTED_CHANGES);
        offerDao.updateOffer(offer);
    }

}
