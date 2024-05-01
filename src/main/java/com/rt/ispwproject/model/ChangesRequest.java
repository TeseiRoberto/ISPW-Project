package com.rt.ispwproject.model;

import com.rt.ispwproject.beans.ChangesOnOffer;

public interface ChangesRequest {

    boolean isHolidayDurationChangeRequired();

    boolean isAccommodationChangeRequired();

    boolean isTransportChangeRequired();

    boolean isPriceChangeRequired();


    // Getters
    int getRequestId();
    int getRelativeOfferId();
    Profile getRequestOwner();
    Profile getRelativeOfferOwner();

    String getChangesDescription();

    Location getDestinationChange();

    DateRange getHolidayDurationChange();

    int getPriceChange();

    AccommodationChanges getAccommodationChanges();

    TransportChanges getTransportChanges();


    ChangesOnOffer toChangesOnOfferBean() throws IllegalArgumentException;
}