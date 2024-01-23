package com.rt.ispwproject.model;

public class TransportRequirements {

    private TransportType type;
    private int quality;
    private int numOfTravelers;
    private String departureLocation;


    public TransportRequirements(TransportType type, int quality, int numOfTravelers, String departureLocation)
    {
        this.type = type;
        this.quality = quality;
        this.numOfTravelers = numOfTravelers;
        this.departureLocation = departureLocation;
    }


    // Setters
    public void setType(TransportType type)                                     { this.type = type; }
    public void setQuality(int quality)                                         { this.quality = quality; }
    public void setNumOfTravelers(int num)                                      { this.numOfTravelers = num; }
    public void setDepartureLocation(String location)                           { this.departureLocation = location; }


    // Getters
    public TransportType getType()                      { return this.type; }
    public int getQuality()                             { return this.quality; }
    public int getNumOfTravelers()                      { return this.numOfTravelers; }
    public String getDepartureLocation()                { return this.departureLocation; }
}
