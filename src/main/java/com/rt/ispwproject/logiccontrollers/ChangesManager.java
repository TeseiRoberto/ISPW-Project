package com.rt.ispwproject.logiccontrollers;

import com.rt.ispwproject.beans.Offer;
import com.rt.ispwproject.beans.ChangesRequest;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.exceptions.DbException;

public class ChangesManager {


    // Sends the given request of changes to the travel agency that made the given offer
    public void requestChangesOnOffer(Session currSession, ChangesRequest changes, Offer currOffer) throws DbException
    {
        // TODO: Add implementation...
    }


    // Retrieves the changes requested by user on the given offer
    public ChangesRequest getRequestedChangesOnOffer(Session currSession, Offer currOffer) throws DbException
    {
        // TODO: Add implementation...
        return null;
    }


}
