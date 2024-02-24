package com.rt.ispwproject.logiccontrollers;

import com.rt.ispwproject.beans.ChangesOnOffer;
import com.rt.ispwproject.beans.Offer;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.config.SessionManager;
import com.rt.ispwproject.config.UserRole;
import com.rt.ispwproject.dao.HolidayOfferChangesDao;
import com.rt.ispwproject.dao.HolidayOfferDao;
import com.rt.ispwproject.dao.ProfileDao;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.model.HolidayOffer;
import com.rt.ispwproject.model.HolidayOfferChanges;
import com.rt.ispwproject.model.HolidayOfferChangesMetadata;
import com.rt.ispwproject.model.Profile;

public class ChangesManager {


    // Sends the given request of changes to the travel agency that made the given offer
    public void requestChangesOnOffer(Session currSession, ChangesOnOffer requestedChanges, Offer currOffer) throws DbException
    {
        if(!SessionManager.getInstance().isLoggedAs(currSession, UserRole.SIMPLE_USER))
            throw new IllegalCallerException("You must be logged in to request changes on an offer");

        Profile user = SessionManager.getInstance().getProfile(currSession);

        // TODO: Create changes request and send it
        throw new DbException("request changes unimplemented yet!");
    }


    // Retrieves the changes requested by user on the given offer
    public ChangesOnOffer getRequestedChangesOnOffer(Session currSession, Offer currOffer) throws DbException
    {
        // TODO: Add implementation...
        return null;
    }


}
