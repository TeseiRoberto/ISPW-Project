package com.rt.ispwproject.model;

import com.rt.ispwproject.beans.Transport;

public class TransportOffer implements TransportChanges {

    private int                 id;                                 // Internal identifier used to locate the offer in our persistence layer
    private final TransportType type;
    private final String        companyName;
    private final int           companyId;                          // External identifier used by the transport api
    private final int           quality;
    private final int           numOfTravelers;
    private final int           pricePerTraveler;
    private final Route         fromToLocation;
    private final DateRange     departureAndReturnDates;


    public TransportOffer(TransportType type, String companyName, int companyId, int quality, int numOfTravelers,
                          int pricePerTraveler, Route fromToLocation, DateRange departureAndReturnDates)
    {
        this(0, type, companyName, companyId, quality, numOfTravelers, pricePerTraveler, fromToLocation, departureAndReturnDates);
    }


    public TransportOffer(int id, TransportType type, String companyName, int companyId, int quality, int numOfTravelers,
                          int pricePerTraveler, Route fromToLocation, DateRange departureAndReturnDates)
    {
        this.id = id;
        this.type = type;
        this.companyName = companyName;
        this.companyId = companyId;
        this.quality = quality;
        this.numOfTravelers = numOfTravelers;
        this.pricePerTraveler = pricePerTraveler;
        this.fromToLocation = fromToLocation;
        this.departureAndReturnDates = departureAndReturnDates;
    }


    // Setters
    public void setId(int id)           { this.id = id; }


    // Getters
    public int getId()                              { return id; }
    public TransportType getType()                  { return type; }
    public String getCompanyName()                  { return companyName; }
    public int getCompanyId()                       { return companyId; }
    public int getQuality()                         { return quality; }
    public int getNumOfTravelers()                  { return numOfTravelers; }
    public int getPricePerTraveler()                { return pricePerTraveler; }
    public Route getRoute()                         { return fromToLocation; }
    public DateRange getDepartureAndReturnDates()   { return departureAndReturnDates; }


    public Transport toTransportBean()
    {
        Transport t = new Transport(
                type.toViewType(),
                quality,
                fromToLocation.getDepartureLocation().getAddress(),
                fromToLocation.getArrivalLocation().getAddress(),
                numOfTravelers
        );

        if(!companyName.isBlank())
            t.setCompanyName(companyName);

        t.setPricePerTraveler(pricePerTraveler);

        return t;
    }

}