package com.rt.ispwproject.model;

import java.time.LocalDate;

public class Holiday {

    private String destination;
    private int price;
    private LocalDate startDate;
    private LocalDate endDate;
    private Accommodation accommodation;
    private Transport transport;


    public Holiday(String destination, int price, LocalDate startDate, LocalDate endDate, Accommodation accommodation, Transport transport)
    {
        this.destination = destination;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.accommodation = accommodation;
        this.transport = transport;
    }


    // Setters
    public void setDestination(String destination)              { this.destination = destination; }
    public void setStartDate(LocalDate date)                    { this.startDate = date; }
    public void setEndDate(LocalDate date)                      { this.endDate = date; }
    public void setPrice(int price)                             { this.price = price; }
    public void setAccommodation(Accommodation accommodation)   { this.accommodation = accommodation; }
    public void setTransport(Transport transport)               { this.transport = transport; }

    // Getters
    public String getDestination()                              { return this.destination; }
    public int getPrice()                                       { return this.price; }
    public LocalDate getStartDate()                             { return this.startDate; }
    public LocalDate getEndDate()                               { return this.endDate; }
    public Accommodation getAccommodation()                     { return this.accommodation; }
    public Transport getTransport()                             { return this.transport; }
}
