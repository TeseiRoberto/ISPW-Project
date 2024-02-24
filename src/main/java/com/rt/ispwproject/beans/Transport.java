package com.rt.ispwproject.beans;


import java.util.List;

public class Transport {

    private String      type;
    private String      companyName;
    private int         companyId;
    private int         quality;
    private int         numOfTravelers;
    private String      departureLocation;
    private int         pricePerTraveler;


    // Constructor used to create transport offer
    public Transport(String type, String companyName, int quality, String departureLocation, int numOfTravelers,
                     int pricePerTraveler) throws IllegalArgumentException
    {
        this.companyId = 0;
        setType(type);
        setCompanyName(companyName);
        setQuality(quality);
        setDepartureLocation(departureLocation);
        setNumOfTravelers(numOfTravelers);
        setPricePerTraveler(pricePerTraveler);
    }


    // Constructor used to create transport requirements
    public Transport(String type, int quality, String departureLocation, int numOfTravelers) throws IllegalArgumentException
    {
        setType(type);
        setQuality(quality);
        setDepartureLocation(departureLocation);
        setNumOfTravelers(numOfTravelers);
    }


    // Setters
    public void setType(String type) throws IllegalArgumentException
    {
        if(type == null || type.isEmpty())
            throw new IllegalArgumentException("Transport type cannot be empty!");

        this.type = type;
    }

    public void setCompanyName(String name) throws IllegalArgumentException
    {
        if(name == null || name.isEmpty())
            throw new IllegalArgumentException("Transport company name cannot be empty!");

        this.companyName = name;
    }

    public void setCompanyId(int id)        { this.companyId = id; }

    public void setQuality(int quality) throws IllegalArgumentException
    {
        if(quality < 1 || quality > 5)
            throw new IllegalArgumentException("Transport quality must be in range [1, 5]!");

        this.quality = quality;
    }

    public void setDepartureLocation(String location) throws IllegalArgumentException
    {
        if(location == null || location.isEmpty())
            throw new IllegalArgumentException("Departure location cannot be empty!");

        this.departureLocation = location;
    }

    public void setNumOfTravelers(int numOfTravelers) throws IllegalArgumentException
    {
        if(numOfTravelers <= 0)
            throw new IllegalArgumentException("Number of travelers cannot be negative or zero!");

        this.numOfTravelers = numOfTravelers;
    }

    public void setPricePerTraveler(int price) throws IllegalArgumentException
    {
        if(price <= 0)
            throw new IllegalArgumentException("Price per travelers cannot be negative or zero!");

        this.pricePerTraveler = price;
    }


    // Getters
    public String getType()                     { return this.type; }
    public String getCompanyName()              { return this.companyName; }
    public int getCompanyId()                   { return this.companyId; }
    public int getQuality()                     { return this.quality; }
    public String getDepartureLocation()        { return this.departureLocation; }
    public int getNumOfTravelers()              { return this.numOfTravelers; }
    public int getPricePerTraveler()            { return this.pricePerTraveler; }
    public String getPricePerTravelerAsStr()    { return Integer.toString(this.pricePerTraveler) + '€'; }
    public int getPrice()                       { return this.pricePerTraveler * this.numOfTravelers; }
    public String getPriceAsStr()               { return Integer.toString(this.getPrice()) + '€'; }

    // Returns all the available transport types
    public static List<String> getAvailableTypes()
    {
        return List.of("Not specified", "Airplane", "Train", "Ferry", "Bus");
    }
}
