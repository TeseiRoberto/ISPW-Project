package com.rt.ispwproject.model;

import com.rt.ispwproject.beans.Accommodation;


public class AccommodationOffer implements AccommodationChanges {

    private int                     id;                             // Internal identifier used to locate the offer in our persistence layer
    private final AccommodationType type;
    private final String            name;
    private int                     accommodationId;                // External identifier used by the accommodation api
    private final int               quality;
    private final Location          location;
    private final int               numOfRooms;
    private final DateRange         lengthOfStay;
    private final int               price;


    public AccommodationOffer(AccommodationType type, String name, int quality, Location location, int numOfRooms, DateRange lengthOfStay, int price)
    {
        this.id = 0;
        this.type = type;
        this.name = name;
        this.accommodationId = 0;
        this.quality = quality;
        this.location = location;
        this.numOfRooms = numOfRooms;
        this.lengthOfStay = lengthOfStay;
        this.price = price;
    }


    // Setters
    public void setId(int id)               { this.id = id; }
    public void setAccommodationId(int id)  { this.accommodationId = id; }


    // Getters
    public int getId()                  { return id;}
    public AccommodationType getType()  { return type;}
    public String getName()             { return name;}
    public int getAccommodationId()     { return accommodationId;}
    public int getQuality()             { return quality;}
    public Location getLocation()       { return location;}
    public int getNumOfRooms()          { return numOfRooms;}
    public DateRange getLengthOfStay()  { return lengthOfStay;}
    public int getPrice()               { return price;}


    public Accommodation toAccommodationBean()
    {
        Accommodation a = new Accommodation(type.toViewType(), quality, numOfRooms);

        if(!name.isBlank())
            a.setName(name);

        if(location != null)
            a.setAddress(location.getAddress());

        a.setPrice(price);

        return a;
    }

}
