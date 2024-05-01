package com.rt.ispwproject.model;

import com.rt.ispwproject.beans.Accommodation;

public class AccommodationRequirements {

    private final int               id;
    private final AccommodationType type;
    private final int               quality;
    private final int               numOfRooms;


    public AccommodationRequirements(AccommodationType type, int quality, int numOfRooms)
    {
        this(0, type, quality, numOfRooms);
    }


    public AccommodationRequirements(int id, AccommodationType type, int quality, int numOfRooms)
    {
        this.id = id;
        this.type = type;
        this.quality = quality;
        this.numOfRooms = numOfRooms;
    }


    // Getters
    public int getId()                  { return id; }
    public AccommodationType getType()  { return type; }
    public int getQuality()             { return quality; }
    public int getNumOfRooms()          { return numOfRooms; }


    // Converts an AccommodationRequirements instance into an Accommodation instance (model to bean class conversion)
    Accommodation toAccommodationBean()
    {
        return new Accommodation(type.toViewType(), quality, numOfRooms);
    }

}