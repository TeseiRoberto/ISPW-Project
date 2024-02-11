package com.rt.ispwproject.model;

import com.rt.ispwproject.beans.Transport;

import java.time.LocalDate;

public class TransportOffer {

    private int             id;                                 // Internal identifier used to locate the offer in our persistence layer
    private TransportType   type;
    private String          company;
    private int             companyId;                          // External identifier used by the transport api
    private int             quality;
    private int             numOfTravelers;
    private final Route     fromToLocation;
    private int             pricePerTraveler;
    private DateRange       departureAndReturnDates;


    public TransportOffer(TransportType type, String companyName, int quality, Route fromToLocation, int numOfTravelers, int pricePerTraveler, DateRange departureAndReturnDates)
    {
        this.id = 0;
        this.companyId = 0;
        this.type = type;
        this.company = companyName;
        this.quality = quality;
        this.fromToLocation = fromToLocation;
        this.numOfTravelers = numOfTravelers;
        this.pricePerTraveler = pricePerTraveler;
        this.departureAndReturnDates = departureAndReturnDates;
    }


    // Setters
    public void setId(int id)                                                   { this.id = id; }
    public void setCompanyId(int id)                                            { this.companyId = id; }
    public void setType(TransportType type)                                     { this.type = type; }
    public void setCompany(String company)                                      { this.company = company; }
    public void setQuality(int quality)                                         { this.quality = quality; }
    public void setNumOfTravelers(int num)                                      { this.numOfTravelers = num; }
    public void setDepartureLocation(Location location)                         { this.fromToLocation.setDepartureLocation(location); }
    public void setArrivalLocation(Location location)                           { this.fromToLocation.setArrivalLocation(location); }
    public void setDepartureAndReturnDates(DateRange departureAndReturn)        { this.departureAndReturnDates = departureAndReturn; }
    public void setPricePerTraveler(int price)                                  { this.pricePerTraveler = price; }


    // Getters
    public int getId()                                  { return this.id; }
    public int getCompanyId()                           { return this.companyId; }
    public TransportType getType()                      { return this.type; }
    public String getCompany()                          { return this.company; }
    public int getQuality()                             { return this.quality; }
    public int getNumOfTravelers()                      { return this.numOfTravelers; }
    public Location getDepartureLocation()              { return this.fromToLocation.getDepartureLocation(); }
    public Location getArrivalLocation()                { return this.fromToLocation.getArrivalLocation(); }
    public LocalDate getDepartureDate()                 { return this.departureAndReturnDates.getStartDate(); }
    public LocalDate getReturnDate()                    { return this.departureAndReturnDates.getEndDate(); }
    public int getPricePerTraveler()                    { return this.pricePerTraveler; }
    public int getPrice()                               { return this.pricePerTraveler * this.numOfTravelers; }


    // Converts a TransportOffer instance into a Transport instance (model to bean class conversion)
    public Transport toTransportBean() throws IllegalArgumentException
    {
        return new Transport(type.toViewType(), company, quality, getDepartureLocation().getAddress(), numOfTravelers, pricePerTraveler);
    }
}
