package com.rt.ispwproject.graphiccontrollers.cmdgraphiccontrollers.makeoffercontroller;

import com.rt.ispwproject.beans.Accommodation;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.beans.Transport;
import com.rt.ispwproject.graphiccontrollers.cmdgraphiccontrollers.BaseGfxControllerCmd;

// This controller implements some basic functionalities for the make offer screen, this is specialized
// by other graphic controllers to implement the make offer screen and make counteroffer screen.
public abstract class BaseMakeGfxOfferControllerCmd extends BaseGfxControllerCmd {

    protected boolean           runLoop;
    protected Accommodation     chosenAccommodation = null;     // AccommodationOffer offer chosen using the search window
    protected Transport         chosenTransport = null;         // TransportOffer offer chosen using the search window


    public BaseMakeGfxOfferControllerCmd(Session session)
    {
        super(session);
    }


    protected abstract void menu();

}
