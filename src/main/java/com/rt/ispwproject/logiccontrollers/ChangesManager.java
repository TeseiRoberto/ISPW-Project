package com.rt.ispwproject.logiccontrollers;

import com.rt.ispwproject.beans.Offer;
import com.rt.ispwproject.beans.ChangesRequest;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.config.SessionManager;
import com.rt.ispwproject.config.UserRole;
import com.rt.ispwproject.dao.HolidayOfferChangesDao;
import com.rt.ispwproject.dao.ProfileDao;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.model.HolidayOfferChanges;
import com.rt.ispwproject.model.HolidayOfferChangesMetadata;
import com.rt.ispwproject.model.Profile;

public class ChangesManager {


    // Sends the given request of changes to the travel agency that made the given offer
    public void requestChangesOnOffer(Session currSession, ChangesRequest requestedChanges, Offer currOffer) throws DbException
    {
        Profile user = SessionManager.getInstance().getProfile(currSession);
        if(user == null)                                        // Check if currSession is valid
            throw new IllegalCallerException("You must be logged in to request changes on an offer");

        if(user.getUserRole() != UserRole.SIMPLE_USER)
            throw new IllegalCallerException("Only simple users can request changes on offers");

        // Retrieve user that made the offer
        ProfileDao profileDao = new ProfileDao();
        Profile bidderAgency = profileDao.getProfile(currOffer.getBidderUsername());

        HolidayOfferChangesMetadata metadata = new HolidayOfferChangesMetadata(user, currOffer.getId(), bidderAgency);
        HolidayOfferChanges changes = new HolidayOfferChanges(metadata);

        // TODO: Actually set values in the HolidayOfferChanges class...

        HolidayOfferChangesDao changesDao = new HolidayOfferChangesDao();
        changesDao.requestHolidayOfferChanges(changes);
    }


    // Retrieves the changes requested by user on the given offer
    public ChangesRequest getRequestedChangesOnOffer(Session currSession, Offer currOffer) throws DbException
    {
        // TODO: Add implementation...
        return null;
    }


}
