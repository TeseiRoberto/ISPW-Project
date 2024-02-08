package com.rt.ispwproject.model;

import java.time.LocalDate;
import java.time.Period;

public class AccommodationOffer {

    private int                 id;                             // Internal identifier used to locate the offer in our persistence layer
    private AccommodationType   type;
    private String              name;
    private int                 accommodationId;                // External identifier used by the accommodation api
    private Location            location;
    private int                 quality;
    private int                 numOfRooms;
    private final DateRange     checkInOutDates;
    private int                 pricePerNight;


    public AccommodationOffer(AccommodationType type, String name, Location address, int quality, int numOfRooms, DateRange checkInOutDates, int pricePerNight)
    {
        this.id = 0;
        this.accommodationId = 0;
        this.type = type;
        this.name = name;
        this.location = address;
        this.quality = quality;
        this.numOfRooms = numOfRooms;
        this.checkInOutDates = checkInOutDates;
        this.pricePerNight = pricePerNight;
    }


    // Setters
    public void setId(int id)                       { this.id = id; }
    public void setAccommodationId(int id)          { this.accommodationId = id; }
    public void setType(AccommodationType type)     { this.type = type; }
    public void setName(String name)                { this.name = name; }
    public void setLocation(Location address)       { this.location = address; }
    public void setQuality(int quality)             { this.quality = quality; }
    public void setNumOfRooms(int num)              { this.numOfRooms = num; }
    public void setCheckInDate(LocalDate date)      { this.checkInOutDates.setStartDate(date); }
    public void setCheckOutDate(LocalDate date)     { this.checkInOutDates.setEndDate(date); }
    public void setPricePerNight(int price)         { this.pricePerNight = price; }


    // Getters
    public int getId()                              { return this.id; }
    public int getAccommodationId()                 { return this.accommodationId; }
    public AccommodationType getType()              { return this.type; }
    public String getName()                         { return this.name; }
    public Location getLocation()                   { return this.location; }
    public int getQuality()                         { return this.quality; }
    public int getNumOfRooms()                      { return this.numOfRooms; }
    public LocalDate getCheckInDate()               { return this.checkInOutDates.getStartDate(); }
    public LocalDate getCheckOutDate()              { return this.checkInOutDates.getEndDate(); }
    public int getPricePerNight()                   { return this.pricePerNight; }

    public int getPrice()
    {
        if(this.checkInOutDates == null)
            return 0;

        int numOfNights = Period.between(checkInOutDates.getStartDate(), checkInOutDates.getEndDate()).getDays();
        return numOfNights * pricePerNight * numOfRooms;
    }
}
