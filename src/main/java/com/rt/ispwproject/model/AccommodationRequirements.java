package com.rt.ispwproject.model;

public class AccommodationRequirements {

    private AccommodationType   type;
    private int                 quality;
    private int                 numOfRooms;


    public AccommodationRequirements(AccommodationType type, int quality, int numOfRooms)
    {
        this.type = type;
        this.quality =  quality;
        this.numOfRooms = numOfRooms;
    }


    // Setters
    public void setType(AccommodationType type)     { this.type = type; }
    public void setQuality(int quality)             { this.quality = quality; }
    public void setNumOfRooms(int numOfRooms)       { this.numOfRooms = numOfRooms; }


    // Getters
    public AccommodationType getType()              { return this.type; }
    public int getQuality()                         { return this.quality; }
    public int getNumOfRooms()                      { return this.numOfRooms; }

}