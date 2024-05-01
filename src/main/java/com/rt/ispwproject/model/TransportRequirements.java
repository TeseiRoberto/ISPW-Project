package com.rt.ispwproject.model;

import com.rt.ispwproject.beans.Transport;

public class TransportRequirements {

    private final int           id;
    private final TransportType type;
    private final int           quality;
    private final int           numOfTravelers;
    private final Route         fromToLocation;


    public TransportRequirements(TransportType type, int quality, int numOfTravelers, Route fromToLocation)
    {
        this(0, type, quality, numOfTravelers, fromToLocation);
    }


    public TransportRequirements(int id, TransportType type, int quality, int numOfTravelers, Route fromToLocation)
    {
        this.id = id;
        this.type = type;
        this.quality = quality;
        this.numOfTravelers =numOfTravelers ;
        this.fromToLocation = fromToLocation;
    }


    // Getters
    public int getId()                  { return id; }
    public TransportType getType()      { return type; }
    public int getQuality()             { return quality; }
    public int getNumOfTravelers()      { return numOfTravelers; }
    public Route getRoute()             { return fromToLocation; }


    // Converts a TransportRequirements instance into a Transport instance (model to bean class conversion)
    Transport toTransportBean()
    {
        return new Transport(
                type.toViewType(),
                quality,
                fromToLocation.getDepartureLocation().getAddress(),
                fromToLocation.getArrivalLocation().getAddress(),
                numOfTravelers
        );
    }
}
