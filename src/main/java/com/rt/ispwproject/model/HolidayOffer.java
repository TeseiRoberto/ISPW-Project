package com.rt.ispwproject.model;

import java.time.LocalDate;

public class HolidayOffer {

    private final HolidayMetadata   metadata;
    private String                  destination;
    protected int                   price;
    private final DateRange         duration;
    private Accommodation           accommodation;
    private Transport               transport;


    public HolidayOffer(HolidayMetadata metadata, String destination, DateRange duration, int price, Accommodation accommodation, Transport transport)
    {
        this.metadata = metadata;
        this.destination = destination;
        this.price = price;
        this.duration = duration;
        this.accommodation = accommodation;
        this.transport = transport;
    }


    // Setters
    public void setDestination(String destination)              { this.destination = destination; }
    public void setPrice(int price)                             { this.price = price; }
    public void setDepartureDate(LocalDate date)                { this.duration.setStartDate(date); }
    public void setReturnDate(LocalDate date)                   { this.duration.setEndDate(date); }
    public void setAccommodation(Accommodation accommodation)   { this.accommodation = accommodation; }
    public void setTransport(Transport transport)               { this.transport = transport; }


    // Getters
    public HolidayMetadata getMetadata()                        { return this.metadata; }
    public String getDestination()                              { return this.destination; }
    public int getPrice()                                       { return this.price; }
    public LocalDate getDepartureDate()                         { return this.duration.getStartDate(); }
    public LocalDate getReturnDate()                            { return this.duration.getEndDate(); }
    public Accommodation getAccommodation()                     { return this.accommodation; }
    public Transport getTransport()                             { return this.transport; }


}
