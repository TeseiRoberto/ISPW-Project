package com.rt.ispwproject.model;

import com.rt.ispwproject.beans.Transport;

public class TransportRequirements {

    private int             id;
    private TransportType   type;
    private int             quality;
    private int             numOfTravelers;
    private Route           fromToLocation;


    public TransportRequirements(TransportType type, int quality, int numOfTravelers, Route fromToLocation)
    {
        this.id = 0;
        this.type = type;
        this.quality = quality;
        this.numOfTravelers = numOfTravelers;
        this.fromToLocation = fromToLocation;
    }

    public TransportRequirements(int id, TransportType type, int quality, int numOfTravelers, Route fromToLocation)
    {
        this.id = id;
        this.type = type;
        this.quality = quality;
        this.numOfTravelers = numOfTravelers;
        this.fromToLocation = fromToLocation;
    }


    // Setters
    public void setType(TransportType type)             { this.type = type; }
    public void setQuality(int quality)                 { this.quality = quality; }
    public void setNumOfTravelers(int num)              { this.numOfTravelers = num; }
    public void setRoute(Route route)                   { this.fromToLocation = route; }


    // Getters
    public int getId()                                  { return this.id; }
    public TransportType getType()                      { return this.type; }
    public int getQuality()                             { return this.quality; }
    public int getNumOfTravelers()                      { return this.numOfTravelers; }
    public Location getDepartureLocation()              { return this.fromToLocation.getDepartureLocation(); }
    public Location getArrivalLocation()                { return this.fromToLocation.getArrivalLocation(); }


    // Converts a TransportRequirements instance into a Transport instance (model to bean class conversion)
    public Transport toTransportBean() throws IllegalArgumentException
    {
        return new Transport(
                this.type.toViewType(),
                this.quality,
                this.fromToLocation.getDepartureLocation().getAddress(),
                this.fromToLocation.getArrivalLocation().getAddress(),
                this.numOfTravelers
        );
    }
}
