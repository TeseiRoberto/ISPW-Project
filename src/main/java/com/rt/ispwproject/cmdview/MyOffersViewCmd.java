package com.rt.ispwproject.cmdview;

import com.rt.ispwproject.beans.Offer;

import java.util.List;

public class MyOffersViewCmd extends BaseViewCmd {


    public void showScreenTitle()
    {
        printTitle(BaseViewCmd.MY_OFFERS_SCREEN_NAME);
    }


    // Displays the offers in the given list
    public void showOffers(List<Offer> offers)
    {
        if(offers == null || offers.isEmpty())
            print("No offer has been made yet...");
        else
            showOffersList(offers);

        print("\n");
    }
}
