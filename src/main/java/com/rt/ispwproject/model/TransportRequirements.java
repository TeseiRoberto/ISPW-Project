package com.rt.ispwproject.model;

import com.rt.ispwproject.beans.Transport;

public interface TransportRequirements {

    // Setters
    void setId(int id);

    // Getters
    int getId();
    TransportType getType();
    int getQuality();
    int getNumOfTravelers();
    Route getRoute();

    Transport toTransportBean();
}
