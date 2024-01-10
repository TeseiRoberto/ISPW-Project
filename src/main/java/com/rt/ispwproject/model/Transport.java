package com.rt.ispwproject.model;

import java.time.LocalDate;

public class Transport {

    private TransportType type;
    private String company;
    private int quality;
    private int numOfTravelers;
    private String departureLocation;
    private String arrivalLocation;
    private LocalDate departureDate;
    private LocalDate returnDate;


    // Setters
    public void setType(TransportType type)             { this.type = type; }
    public void setCompany(String company)              { this.company = company; }
    public void setQuality(int quality)                 { this.quality = quality; }
    public void setNumOfTravelers(int num)              { this.numOfTravelers = num; }
    public void setDepartureLocation(String location)   { this.departureLocation = location; }
    public void setArrivalLocation(String location)     { this.arrivalLocation = location; }
    public void setDepartureDate(LocalDate date)        { this.departureDate = date; }
    public void setReturnDate(LocalDate date)           { this.returnDate = date; }


    // Getters
    public TransportType getType()                      { return this.type; }
    public String getCompany()                          { return this.company; }
    public int getQuality()                             { return this.quality; }
    public int getNumOfTravelers()                      { return this.numOfTravelers; }
    public String getDepartureLocation()                { return this.departureLocation; }
    public String getArrivalLocation()                  { return this.arrivalLocation; }
    public LocalDate getDepartureDate()                 { return this.departureDate; }
    public LocalDate getReturnDate()                    { return this.returnDate; }
}
