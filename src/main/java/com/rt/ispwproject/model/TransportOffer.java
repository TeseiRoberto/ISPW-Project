package com.rt.ispwproject.model;

import com.rt.ispwproject.beans.Transport;

public interface TransportOffer {

    // Setters
    void setId(int id);
    void setCompanyId(int id);

    // Getters
    int getId();
    TransportType getType();
    String getCompanyName();
    int getCompanyId();
    int getQuality();
    int getNumOfTravelers();
    int getPricePerTraveler();
    Route getRoute();
    DateRange getDepartureAndReturnDates();


    Transport toTransportBean();
}