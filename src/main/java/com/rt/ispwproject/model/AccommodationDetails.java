package com.rt.ispwproject.model;

import com.rt.ispwproject.beans.Accommodation;

public class AccommodationDetails implements AccommodationRequirements, AccommodationOffer, AccommodationChangesRequest {

    private int                     id;                             // Internal identifier used to locate the offer in our persistence layer
    private AccommodationType       type;
    private String                  name;
    private int                     accommodationId;                // External identifier used by the accommodation api
    private int                     quality;
    private Location                location;
    private int                     numOfRooms;
    private DateRange               lengthOfStay;
    private int                     price;


    public AccommodationDetails(AccommodationType type, String name, int quality, Location location, int numOfRooms, DateRange lengthOfStay, int price)
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
    public void setId(int id)                               { this.id = id; }
    public void setType(AccommodationType type)             { this.type = type; }
    public void setName(String name)                        { this.name = name; }
    public void setAccommodationId(int id)                  { this.accommodationId = id; }
    public void setQuality(int quality)                     { this.quality = quality; }
    public void setLocation(Location loc)                   { this.location = loc; }
    public void setNumOfRooms(int num)                      { this.numOfRooms = num; }
    public void setLengthOfStay(DateRange length)           { this.lengthOfStay = length; }
    public void setPrice(int price)                         { this.price = price; }


    // Getters
    public int getId()                          { return this.id; }
    public AccommodationType getType()          { return this.type; }
    public String getName()                     { return this.name; }
    public int getAccommodationId()             { return this.accommodationId; }
    public int getQuality()                     { return this.quality; }
    public Location getLocation()               { return this.location; }
    public int getNumOfRooms()                  { return this.numOfRooms; }
    public DateRange getLengthOfStay()          { return this.lengthOfStay; }
    public int getPrice()                       { return this.price; }


    // Converts an AccommodationDetails instance into an Accommodation instance (model to bean class conversion)
    public Accommodation toAccommodationBean() throws IllegalArgumentException
    {
        Accommodation a = new Accommodation(
                this.type.toViewType(),
                this.name.isEmpty() ? "unknown" : this.name,
                this.location == null ? "unknown" : this.location.getAddress(),
                this.quality,
                this.numOfRooms,
                getPrice()
        );

        a.setAccommodationId(this.accommodationId);
        return a;
    }
}
