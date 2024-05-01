package com.rt.ispwproject.model;

import com.rt.ispwproject.beans.Accommodation;

public interface AccommodationChanges {

    int getId();
    AccommodationType getType();
    int getQuality();
    int getNumOfRooms();

    Accommodation toAccommodationBean();
}
