package com.rt.ispwproject.model;

import com.rt.ispwproject.beans.Accommodation;

public interface AccommodationChangesRequest {

    // Setters
    void setId(int id);


    // Getters
    int getId();
    AccommodationType getType();
    int getQuality();
    int getNumOfRooms();

    Accommodation toAccommodationBean();
}
