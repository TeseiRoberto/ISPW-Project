package com.rt.ispwproject.model;

public class TransportRequirements {

    private int             id;
    private TransportType   type;
    private int             quality;
    private int             numOfTravelers;
    private Location        departureLocation;


    public TransportRequirements(TransportType type, int quality, int numOfTravelers, Location departureLocation)
    {
        this.type = type;
        this.quality = quality;
        this.numOfTravelers = numOfTravelers;
        this.departureLocation = departureLocation;
    }

    public TransportRequirements(int id, TransportType type, int quality, int numOfTravelers, Location departureLocation)
    {
        this.id = id;
        this.type = type;
        this.quality = quality;
        this.numOfTravelers = numOfTravelers;
        this.departureLocation = departureLocation;
    }


    // Setters
    public void setType(TransportType type)                                     { this.type = type; }
    public void setQuality(int quality)                                         { this.quality = quality; }
    public void setNumOfTravelers(int num)                                      { this.numOfTravelers = num; }
    public void setDepartureLocation(Location location)                         { this.departureLocation = location; }


    // Getters
    public int getId()                                  { return this.id; }
    public TransportType getType()                      { return this.type; }
    public int getQuality()                             { return this.quality; }
    public int getNumOfTravelers()                      { return this.numOfTravelers; }
    public Location getDepartureLocation()              { return this.departureLocation; }
}
