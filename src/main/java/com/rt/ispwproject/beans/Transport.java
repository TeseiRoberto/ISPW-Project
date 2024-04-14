package com.rt.ispwproject.beans;


public class Transport {

    private String      type;
    private String      companyName;
    private int         companyId;
    private int         quality;
    private int         numOfTravelers;
    private String      departureLocation;
    private String      arrivalLocation;
    private int         pricePerTraveler;


    public Transport()
    {
        this.type = "";
        this.companyName = "";
        this.companyId = 0;
        this.quality = 0;
        this.numOfTravelers = 0;
        this.departureLocation = "";
        this.arrivalLocation = "";
        this.pricePerTraveler = 0;
    }


    // Constructor used to create transport offer
    public Transport(String type, String companyName, int quality, String departureLocation, String arrivalLocation,
                     int numOfTravelers, int pricePerTraveler) throws IllegalArgumentException
    {
        this.companyId = 0;
        setType(type);
        setCompanyName(companyName);
        setQuality(quality);
        setDepartureLocation(departureLocation);
        setArrivalLocation(arrivalLocation);
        setNumOfTravelers(numOfTravelers);
        setPricePerTraveler(pricePerTraveler);
    }


    // Constructor used to create transport requirements
    public Transport(String type, int quality, String departureLocation, String arrivalLocation, int numOfTravelers) throws IllegalArgumentException
    {
        this.companyId = 0;
        setType(type);
        setQuality(quality);
        setDepartureLocation(departureLocation);
        setArrivalLocation(arrivalLocation);
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

        if(location.equals(arrivalLocation))
            throw new IllegalArgumentException("Departure location cannot be equal to the arrival location!");

        this.departureLocation = location;
    }

    public void setArrivalLocation(String location) throws IllegalArgumentException
    {
        if(location == null || location.isEmpty())
            throw new IllegalArgumentException("Arrival location cannot be empty!");

        if(location.equals(departureLocation))
            throw new IllegalArgumentException("Arrival location cannot be equal to the departure location!");

        this.arrivalLocation = location;
    }

    public void setNumOfTravelers(int numOfTravelers) throws IllegalArgumentException
    {
        if(numOfTravelers <= 0)
            throw new IllegalArgumentException("Number of travelers cannot be negative or zero!");

        this.numOfTravelers = numOfTravelers;
    }

    public void setPricePerTraveler(int price) throws IllegalArgumentException
    {
        if(price < 0)
            throw new IllegalArgumentException("Price per travelers cannot be negative!");

        this.pricePerTraveler = price;
    }


    // Getters
    public String getType()                     { return this.type; }
    public String getCompanyName()              { return this.companyName; }
    public int getCompanyId()                   { return this.companyId; }
    public int getQuality()                     { return this.quality; }
    public String getDepartureLocation()        { return this.departureLocation; }
    public String getArrivalLocation()          { return this.arrivalLocation; }
    public int getNumOfTravelers()              { return this.numOfTravelers; }
    public int getPricePerTraveler()            { return this.pricePerTraveler; }
    public String getPricePerTravelerAsStr()    { return Integer.toString(this.pricePerTraveler) + '€'; }
    public int getPrice()                       { return this.pricePerTraveler * this.numOfTravelers; }

    // Returns all the available transport types
    public static String[] getAvailableTypes()
    {
        return new String[] { "Not specified", "Airplane", "Train", "Ferry", "Bus" };
    }
}
