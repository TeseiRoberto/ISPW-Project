package com.rt.ispwproject.model;

import java.time.LocalDate;

public class Accommodation {

    private AccommodationType type;
    private String name;
    private String address;
    private int quality;
    private int numOfRooms;
    private LocalDate checkIn;
    private LocalDate checkOut;

    public Accommodation(AccommodationType type, String name, String address, int quality, int numOfRooms, LocalDate checkIn, LocalDate checkOut)
    {
        this.type = type;
        this.name = name;
        this.address = address;
        this.quality = quality;
        this.numOfRooms = numOfRooms;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }


    public Accommodation()
    {
        this.type = AccommodationType.UNSPECIFIED;
        this.name = "";
        this.address = "";
        this.quality = 0;
        this.numOfRooms = 0;
        this.checkIn = null;
        this.checkOut = null;
    }


    // Setters
    public void setType(AccommodationType type)     { this.type = type; }
    public void setName(String name)                { this.name = name; }
    public void setAddress(String address)          { this.address = address; }
    public void setQuality(int quality)             { this.quality = quality; }
    public void setNumOfRooms(int num)              { this.numOfRooms = num; }
    public void setCheckInDate(LocalDate date)      { this.checkIn = date; }
    public void setCheckOutDate(LocalDate date)     { this.checkOut = date; }


    // Getters
    public AccommodationType getType()              { return this.type; }
    public String getName()                         { return this.name; }
    public String getAddress()                      { return this.address; }
    public int getQuality()                         { return this.quality; }
    public int getNumOfRooms()                      { return this.numOfRooms; }
    public LocalDate getCheckInDate()               { return this.checkIn; }
    public LocalDate getCheckOutDate()              { return this.checkOut; }
}
