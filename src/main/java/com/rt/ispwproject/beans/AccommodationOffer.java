package com.rt.ispwproject.beans;

import com.rt.ispwproject.model.AccommodationType;

public class AccommodationOffer {

    private AccommodationType   type;
    private String              name;
    private String              address;
    private int                 quality;
    private int                 numOfRooms;
    private int                 pricePerNight;
    private int                 totalPrice;

    public AccommodationOffer(AccommodationType type, String name, String address, int quality, int numOfRooms, int pricePerNight, int totalPrice) throws IllegalArgumentException
    {
        setType(type);
        setName(name);
        setAddress(address);
        setQuality(quality);
        setNumOfRooms(numOfRooms);
        setPricePerNight(pricePerNight);
        setPrice(totalPrice);
    }


    // Setters
    public void setType(AccommodationType type) throws IllegalArgumentException
    {
        if(type == null)
            throw new IllegalArgumentException("Accommodation type name cannot be empty!");

        this.type = type;
    }

    public void setName(String name) throws IllegalArgumentException
    {
        if(name == null || name.isEmpty())
            throw new IllegalArgumentException("Accommodation name cannot be empty!");

        this.name = name;
    }

    public void setAddress(String address) throws IllegalArgumentException
    {
        if(address == null || address.isEmpty())
            throw new IllegalArgumentException("Accommodation address cannot be empty!");

        this.address = address;
    }

    public void setQuality(int quality) throws IllegalArgumentException
    {
        if(quality < 1 || quality > 5)
            throw new IllegalArgumentException("Accommodation quality must be in range [1, 5]!");

        this.quality = quality;
    }

    public void setNumOfRooms(int numOfRooms) throws IllegalArgumentException
    {
        if(numOfRooms <= 0)
            throw new IllegalArgumentException("Number of rooms cannot be negative or zero!");

        this.numOfRooms = numOfRooms;
    }

    public void setPricePerNight(int price) throws IllegalArgumentException
    {
        if(price <= 0)
            throw new IllegalArgumentException("Price per night cannot be negative or zero!");

        this.pricePerNight = price;
    }

    public void setPrice(int price) throws IllegalArgumentException
    {
        if(price <= 0)
            throw new IllegalArgumentException("Price cannot be negative or zero!");

        this.totalPrice = price;
    }

    // Getters
    public AccommodationType getType()     { return this.type; }
    public String getName()                { return this.name; }
    public String getAddress()             { return this.address; }
    public int getQuality()                { return this.quality; }
    public int getNumOfRooms()             { return this.numOfRooms; }
    public int getPricePerNight()          { return this.pricePerNight; }
    public int getPrice()                  { return this.totalPrice; }
}
