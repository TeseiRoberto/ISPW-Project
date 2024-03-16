package com.rt.ispwproject.model;

import com.rt.ispwproject.beans.Transport;

public class TransportDetails implements TransportRequirements, TransportOffer, TransportChangesRequest {

    private int             id;                                 // Internal identifier used to locate the offer in our persistence layer
    private TransportType   type;
    private String          companyName;
    private int             companyId;                          // External identifier used by the transport api
    private int             quality;
    private int             numOfTravelers;
    private int             pricePerTraveler;
    private Route           fromToLocation;
    private DateRange       departureAndReturnDates;


    public TransportDetails(TransportType type, String companyName, int quality, Route fromToLocation, int numOfTravelers, int pricePerTraveler, DateRange departureAndReturnDates)
    {
        this.id = 0;
        this.type = type;
        this.companyName = companyName;
        this.companyId = 0;
        this.quality = quality;
        this.fromToLocation = fromToLocation;
        this.numOfTravelers = numOfTravelers;
        this.pricePerTraveler = pricePerTraveler;
        this.departureAndReturnDates = departureAndReturnDates;
    }


    // Setters
    public void setId(int id)                               { this.id = id; }
    public void setType(TransportType type)                 { this.type = type; }
    public void setCompanyName(String companyName)          { this.companyName = companyName; }
    public void setCompanyId(int companyId)                 { this.companyId = companyId; }
    public void setQuality(int quality)                     { this.quality = quality; }
    public void setNumOfTravelers(int numOfTravelers)       { this.numOfTravelers = numOfTravelers; }
    public void setPricePerTraveler(int pricePerTraveler)   { this.pricePerTraveler = pricePerTraveler; }
    public void setRoute(Route fromToLocation)              { this.fromToLocation = fromToLocation; }
    public void setDepartureAndReturnDates(DateRange dates) { this.departureAndReturnDates = dates; }


    // Getters
    public int getId()                              { return id; }
    public TransportType getType()                  { return type; }
    public String getCompanyName()                  { return companyName; }
    public int getCompanyId()                       { return companyId; }
    public int getQuality()                         { return quality; }
    public int getNumOfTravelers()                  { return numOfTravelers; }
    public int getPricePerTraveler()                { return pricePerTraveler; }
    public Route getRoute()                         { return this.fromToLocation; }
    public DateRange getDepartureAndReturnDates()   { return this.departureAndReturnDates; }


    // Converts a TransportOffer instance into a Transport instance (model to bean class conversion)
    public Transport toTransportBean() throws IllegalArgumentException
    {
        Transport t = new Transport(
                this.type.toViewType(),
                this.companyName.isEmpty() ? "unknown" : this.companyName,
                this.quality,
                this.fromToLocation.getDepartureLocation().getAddress(),
                this.fromToLocation.getArrivalLocation().getAddress(),
                this.numOfTravelers,
                this.pricePerTraveler
        );

        t.setCompanyId(this.companyId);
        return t;
    }

}
