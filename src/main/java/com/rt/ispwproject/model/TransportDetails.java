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


    public TransportDetails()
    {
        this.id = 0;
        this.type = TransportType.UNSPECIFIED;
        this.companyName = "";
        this.companyId = 0;
        this.quality = 0;
        this.numOfTravelers = 0;
        this.pricePerTraveler = 0;
        this.fromToLocation = null;
        this.departureAndReturnDates = null;
    }


    // Setters
    public void setId(int id)
    {
        this.id = id;
    }

    public void setType(TransportType type) throws IllegalArgumentException
    {
        if(type == null)
            throw new IllegalArgumentException("A transport type must be specified");

        this.type = type;
    }

    public void setCompanyName(String companyName) throws IllegalArgumentException
    {
        if(companyName == null || companyName.isBlank())
            throw new IllegalArgumentException("The transport company name must be specified");

        this.companyName = companyName;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public void setQuality(int quality) throws IllegalArgumentException
    {
        if(quality < 1 || quality > 5)
            throw new IllegalArgumentException("Transport quality must be an integer between 1 and 5");

        this.quality = quality;
    }

    public void setNumOfTravelers(int numOfTravelers) throws IllegalArgumentException
    {
        if(numOfTravelers <= 0)
            throw new IllegalArgumentException("The number of travelers must be equal to (or greater than) 1");

        this.numOfTravelers = numOfTravelers;
    }

    public void setPricePerTraveler(int pricePerTraveler) throws IllegalArgumentException
    {
        if(pricePerTraveler <= 0)
            throw new IllegalArgumentException("The price per traveler must be equal to (or greater than) 1");

        this.pricePerTraveler = pricePerTraveler;
    }

    public void setFromToLocation(Route fromToLocation) throws IllegalArgumentException
    {
        if(fromToLocation == null)
            throw new IllegalArgumentException("The route of the transport must be specified");

        this.fromToLocation = fromToLocation;
    }

    public void setDepartureAndReturnDates(DateRange departureAndReturnDates) throws IllegalArgumentException
    {
        if(departureAndReturnDates == null)
            throw new IllegalArgumentException("The departure and return dates of the transport must be specified");

        this.departureAndReturnDates = departureAndReturnDates;
    }


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
