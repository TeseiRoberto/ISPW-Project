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


    public AccommodationDetails()
    {
        this.id = 0;
        this.type = AccommodationType.UNSPECIFIED;
        this.name = "";
        this.accommodationId = 0;
        this.quality = 0;
        this.location = null;
        this.numOfRooms = 0;
        this.lengthOfStay = null;
        this.price = 0;
    }


    // Setters
    public void setId(int id)
    {
        this.id = id;
    }

    public void setType(AccommodationType type) throws IllegalArgumentException
    {
        if(type == null)
            throw new IllegalArgumentException("An accommodation type must be specified");

        this.type = type;
    }

    public void setName(String name) throws IllegalArgumentException
    {
        if(name == null || name.isBlank())
            throw new IllegalArgumentException("An accommodation name must be specified");

        this.name = name;
    }

    public void setAccommodationId(int accommodationId)
    {
        this.accommodationId = accommodationId;
    }

    public void setQuality(int quality) throws IllegalArgumentException
    {
        if(quality < 1 || quality > 5)
            throw new IllegalArgumentException("Accommodation quality must be an integer between 1 and 5");

        this.quality = quality;
    }

    public void setLocation(Location location) throws IllegalArgumentException
    {
        if(location == null)
            throw new IllegalArgumentException("The location of an Accommodation must be specified");

        this.location = location;
    }

    public void setNumOfRooms(int numOfRooms) throws IllegalArgumentException
    {
        if(numOfRooms <= 0)
            throw new IllegalArgumentException("The number of rooms of an accommodation must be equal to (or greater than) 1");

        this.numOfRooms = numOfRooms;
    }

    public void setLengthOfStay(DateRange lengthOfStay) throws IllegalArgumentException
    {
        if(lengthOfStay == null)
            throw new IllegalArgumentException("The length of stay in an accommodation must be specified");

        this.lengthOfStay = lengthOfStay;
    }

    public void setPrice(int price) throws IllegalArgumentException
    {
        if (price <= 0)
            throw new IllegalArgumentException("The price of an accommodation must be equal to (or greater than) 1");

        this.price = price;
    }


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
