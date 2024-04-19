package com.rt.ispwproject.cmdview;

import com.rt.ispwproject.beans.ChangesOnOffer;
import com.rt.ispwproject.beans.Offer;

public class RequestChangesViewCmd extends BaseViewCmd {

    public void showScreenTitle()
    {
        printTitle(BaseViewCmd.REQUEST_CHANGES_SCREEN_NAME);
    }


    // Displays the details of the given offer
    public void showOffer(Offer offer)
    {
        printSubtitle("Offer");
        showOfferDetails(offer, true, true);
        print("\n");
    }


    // Displays the details of the given changes
    public void showChangesOnOffer(ChangesOnOffer changes)
    {
        printSubtitle("Requested changes");
        showChangesOnOfferDetails(changes);
        print("\n");
    }

}
