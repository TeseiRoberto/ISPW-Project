package com.rt.ispwproject.cmdview.makeofferview;

import com.rt.ispwproject.beans.*;
import com.rt.ispwproject.cmdview.BaseViewCmd;

import java.util.List;

public abstract class BaseMakeOfferViewCmd extends BaseViewCmd {


    // Displays the details of the given announcement
    public void showUserRequest(Announcement announce)
    {
        printSubtitle("User request");
        showAnnouncementDetails(announce, true, false);
        print("\n");
    }


    // Displays the changes requested by the user (uses the original offer to display values of
    // fields for which no change is required)
    public void showUserRequest(ChangesOnOffer changes, Offer originalOffer)
    {
        printSubtitle("User request");
        showChangesOnOfferDetails(changes, originalOffer);
        print("\n");
    }


    // Displays the details of the given offer
    public void showOffer(Offer offer)
    {
        printSubtitle("Your offer");
        showOfferDetails(offer, false, true);
        print("\n");
    }


    // Displays some of the details of the accommodations in the given lists
    public void showAccommodationOffersList(List<Accommodation> offers)
    {
        printSubtitle("Available accommodations");

        for(int i = 0; i < offers.size(); i++)
        {
            Accommodation a = offers.get(i);
            printf("%d] %s - %s - %d stars - %s\n", i + 1, a.getName(), a.getType(), a.getQuality(), a.getPriceAsStr());
        }
    }


    // Displays some of the details of the transports in the given lists
    public void showTransportOffersList(List<Transport> offers)
    {
        printSubtitle("Available accommodations");

        for(int i = 0; i < offers.size(); i++)
        {
            Transport t = offers.get(i);
            printf("%d] %s - %s - %d stars - %s\n", i + 1, t.getCompanyName(), t.getType(), t.getQuality(), t.getPricePerTravelerAsStr());
        }
    }

}
