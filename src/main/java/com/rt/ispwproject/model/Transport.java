package com.rt.ispwproject.model;

import java.time.LocalDate;

public class Transport {

    private TransportType   type;
    private String          company;
    private int             quality;
    private int             numOfTravelers;
    private final Route     fromToLocation;
    private DateRange       departureAndReturnDates;


    public Transport(TransportType type, String companyName, int quality, Route fromToLocation, int numOfTravelers, DateRange departureAndReturnDates)
    {
        this.type = type;
        this.company = companyName;
        this.quality = quality;
        this.fromToLocation = fromToLocation;
        this.numOfTravelers = numOfTravelers;
        this.departureAndReturnDates = departureAndReturnDates;
    }


    public Transport(TransportType type, int quality, Route fromToLocation, int numOfTravelers)
    {
        this.type = type;
        this.company = "";
        this.quality = quality;
        this.fromToLocation = fromToLocation;
        this.numOfTravelers = numOfTravelers;
        this.departureAndReturnDates = null;
    }


    // Setters
    public void setType(TransportType type)                                     { this.type = type; }
    public void setCompany(String company)                                      { this.company = company; }
    public void setQuality(int quality)                                         { this.quality = quality; }
    public void setNumOfTravelers(int num)                                      { this.numOfTravelers = num; }
    public void setDepartureLocation(String location)                           { this.fromToLocation.setDepartureLocation(location); }
    public void setArrivalLocation(String location)                             { this.fromToLocation.setArrivalLocation(location); }
    public void setDepartureAndReturnDates(DateRange departureAndReturn)        { this.departureAndReturnDates = departureAndReturn; }


    // Getters
    public TransportType getType()                      { return this.type; }
    public String getCompany()                          { return this.company; }
    public int getQuality()                             { return this.quality; }
    public int getNumOfTravelers()                      { return this.numOfTravelers; }
    public String getDepartureLocation()                { return this.fromToLocation.getDepartureLocation(); }
    public String getArrivalLocation()                  { return this.fromToLocation.getArrivalLocation(); }
    public LocalDate getDepartureDate()                 { return this.departureAndReturnDates.getStartDate(); }
    public LocalDate getReturnDate()                    { return this.departureAndReturnDates.getEndDate(); }
}
