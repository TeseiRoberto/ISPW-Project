package com.rt.ispwproject.model;

import com.rt.ispwproject.beans.Accommodation;

public class AccommodationRequirements {

    private int                 id;
    private AccommodationType   type;
    private int                 quality;
    private int                 numOfRooms;


    public AccommodationRequirements(AccommodationType type, int quality, int numOfRooms)
    {
        this.type = type;
        this.quality =  quality;
        this.numOfRooms = numOfRooms;
    }

    public AccommodationRequirements(int id, AccommodationType type, int quality, int numOfRooms)
    {
        this.id = id;
        this.type = type;
        this.quality =  quality;
        this.numOfRooms = numOfRooms;
    }


    // Setters
    public void setType(AccommodationType type)     { this.type = type; }
    public void setQuality(int quality)             { this.quality = quality; }
    public void setNumOfRooms(int numOfRooms)       { this.numOfRooms = numOfRooms; }


    // Getters
    public int getId()                              { return this.id; }
    public AccommodationType getType()              { return this.type; }
    public int getQuality()                         { return this.quality; }
    public int getNumOfRooms()                      { return this.numOfRooms; }


    // Converts an AccommodationRequirements instance into an Accommodation instance (model to bean class conversion)
    public Accommodation toAccommodationBean() throws IllegalArgumentException
    {
        return new Accommodation(type.toViewType(), quality, numOfRooms);
    }

}
