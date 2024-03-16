package com.rt.ispwproject.model;

import com.rt.ispwproject.beans.Accommodation;

public interface AccommodationOffer {
    // Setters
    void setId(int id);
    void setAccommodationId(int id);


    // Getters
    int getId();
    AccommodationType getType();
    String getName();
    int getAccommodationId();
    int getQuality();
    Location getLocation();
    int getNumOfRooms();
    DateRange getLengthOfStay();
    int getPrice();

    Accommodation toAccommodationBean();
}
