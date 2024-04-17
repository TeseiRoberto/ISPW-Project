package com.rt.ispwproject.cmdview;

import com.rt.ispwproject.beans.Offer;

public class AnnouncementDetailsViewCmd extends BaseViewCmd {

    public void showScreenTitle()
    {
        printTitle(BaseViewCmd.ANNOUNCEMENT_DETAILS_SCREEN_NAME);
    }


    // Displays the details of the given offer
    public void showOffer(Offer offer)
    {
        printSubtitle("Offer");
        showOfferDetails(offer, true, false);
        print("\n");
    }

}
