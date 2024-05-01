package com.rt.ispwproject.model;

import com.rt.ispwproject.beans.Transport;

public interface TransportChanges {

    int getId();
    TransportType getType();
    int getQuality();
    int getNumOfTravelers();
    Route getRoute();

    Transport toTransportBean();
}
