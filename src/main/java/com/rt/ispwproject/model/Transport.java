package com.rt.ispwproject.model;

import java.time.LocalDate;

public class Transport {

    private TransportType   type;
    private String          company;
    private int             quality;
    private int             numOfTravelers;
    private final Route     fromToLocation;
    private int             pricePerTraveler;
    private DateRange       departureAndReturnDates;


    public Transport(TransportType type, String companyName, int quality, Route fromToLocation, int numOfTravelers, int pricePerTraveler, DateRange departureAndReturnDates)
    {
        this.type = type;
        this.company = companyName;
        this.quality = quality;
        this.fromToLocation = fromToLocation;
        this.numOfTravelers = numOfTravelers;
        this.pricePerTraveler = pricePerTraveler;
        this.departureAndReturnDates = departureAndReturnDates;
    }


    // Setters
    public void setType(TransportType type)                                     { this.type = type; }
    public void setCompany(String company)                                      { this.company = company; }
    public void setQuality(int quality)                                         { this.quality = quality; }
    public void setNumOfTravelers(int num)                                      { this.numOfTravelers = num; }
    public void setDepartureLocation(Location location)                         { this.fromToLocation.setDepartureLocation(location); }
    public void setArrivalLocation(Location location)                           { this.fromToLocation.setArrivalLocation(location); }
    public void setDepartureAndReturnDates(DateRange departureAndReturn)        { this.departureAndReturnDates = departureAndReturn; }
    public void setPricePerTraveler(int price)                                  { this.pricePerTraveler = price; }


    // Getters
    public TransportType getType()                      { return this.type; }
    public String getCompany()                          { return this.company; }
    public int getQuality()                             { return this.quality; }
    public int getNumOfTravelers()                      { return this.numOfTravelers; }
    public Location getDepartureLocation()              { return this.fromToLocation.getDepartureLocation(); }
    public Location getArrivalLocation()                { return this.fromToLocation.getArrivalLocation(); }
    public LocalDate getDepartureDate()                 { return this.departureAndReturnDates.getStartDate(); }
    public LocalDate getReturnDate()                    { return this.departureAndReturnDates.getEndDate(); }
    public int getPricePerTraveler()                    { return this.pricePerTraveler; }
}
