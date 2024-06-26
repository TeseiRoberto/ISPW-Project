package com.rt.ispwproject.beans;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Accommodation {

    private String      type;
    private String      name;
    private int         accommodationId;
    private String      address;
    private int         quality;
    private int         numOfRooms;
    private int         totalPrice;
    private List<URL>   imagesLinks;


    public Accommodation()
    {
        this.type = "";
        this.name = "";
        this.accommodationId = 0;
        this.address = "";
        this.quality = 0;
        this.numOfRooms = 0;
        this.totalPrice = 0;
        this.imagesLinks = List.of();
    }


    // Copy constructor
    public Accommodation(Accommodation other)
    {
        this.type = other.getType();
        this.name = other.getName();
        this.accommodationId = other.getAccommodationId();
        this.address = other.getAddress();
        this.quality = other.getQuality();
        this.numOfRooms = other.getNumOfRooms();
        this.totalPrice = other.getPrice();

        if(other.imagesLinks != null)
        {
            this.imagesLinks = new ArrayList<>();
            this.imagesLinks.addAll(other.imagesLinks);
        }
    }


    public Accommodation(String type, String name, String address, int quality, int numOfRooms, int totalPrice) throws IllegalArgumentException
    {
        setType(type);
        setName(name);
        setAddress(address);
        setQuality(quality);
        setNumOfRooms(numOfRooms);
        setPrice(totalPrice);
        imagesLinks = List.of();
        this.accommodationId = 0;
    }


    public Accommodation(String type, int quality, int numOfRooms) throws IllegalArgumentException
    {
        this.name = "";
        this.accommodationId = 0;
        this.address = "";
        this.totalPrice = 0;
        this.imagesLinks = null;
        setType(type);
        setQuality(quality);
        setNumOfRooms(numOfRooms);
    }


    // Setters
    public void setType(String type) throws IllegalArgumentException
    {
        if(type == null || type.isEmpty())
            throw new IllegalArgumentException("Accommodation type name cannot be empty!");

        this.type = type;
    }

    public void setName(String name) throws IllegalArgumentException
    {
        if(name == null || name.isEmpty())
            throw new IllegalArgumentException("Accommodation name cannot be empty!");

        this.name = name;
    }

    public void setAccommodationId(int id)      { this.accommodationId = id; }

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

    public void setPrice(int price) throws IllegalArgumentException
    {
        if(price < 0)
            throw new IllegalArgumentException("Price cannot be negative!");

        this.totalPrice = price;
    }

    public void setImagesLinks(List<URL> links)
    {
        this.imagesLinks = links;
    }


    // Getters
    public String getType()                 { return this.type; }
    public String getName()                 { return this.name; }
    public int getAccommodationId()         { return this.accommodationId; }
    public String getAddress()              { return this.address; }
    public int getQuality()                 { return this.quality; }
    public int getNumOfRooms()              { return this.numOfRooms; }
    public int getPrice()                   { return this.totalPrice; }
    public String getPriceAsStr()           { return Integer.toString(this.totalPrice) + '€'; }
    public List<URL> getImagesLinks()       { return this.imagesLinks; }

    // Returns all the available accommodation types
    public static List<String> getAvailableTypes()
    {
        return List.of( "Not specified", "Hotel" );
    }


    public boolean isEqual(Accommodation other)
    {
        if(!type.equals(other.type))
            return false;

        if(!name.equals(other.name))
            return false;

        if(accommodationId != other.accommodationId)
            return false;

        if(!address.equals(other.address))
            return false;

        if(quality != other.quality)
            return false;

        if(numOfRooms != other.numOfRooms)
            return false;

        return totalPrice == other.totalPrice;
    }
}
