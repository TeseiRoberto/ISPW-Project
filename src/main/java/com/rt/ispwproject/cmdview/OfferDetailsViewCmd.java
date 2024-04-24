package com.rt.ispwproject.cmdview;

import com.rt.ispwproject.beans.ChangesOnOffer;
import com.rt.ispwproject.beans.Offer;

public class OfferDetailsViewCmd extends BaseViewCmd {


    public void showScreenTitle()
    {
        printTitle(BaseViewCmd.OFFER_DETAILS_SCREEN_NAME);
    }


    // Displays the details of the given offer
    public void showOffer(Offer offer)
    {
        printSubtitle("Your offer");
        showOfferDetails(offer, false, false);
        print("\n");
    }


    // Displays the details of the request of changes
    public void showRequestedChangesOnOffer(ChangesOnOffer changes)
    {
        if(changes != null)
        {
            printSubtitle("Requested changes");
            showChangesOnOfferDetails(changes);
            print("\n");
        }
    }

}
