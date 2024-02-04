package com.rt.ispwproject.model;

public class Location {

    private final String    name;
    private double          latitude;
    private double          longitude;


    public Location(String name, double latitude, double longitude) throws IllegalArgumentException
    {
        checkCoordinatesValidity(latitude, longitude);
        checkNameValidity(name);

        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public Location(String name) throws IllegalArgumentException
    {
        retrieveGeographicalCoordinates(name);
        this.name = name;
    }


    // Getters
    public String getName()         { return this.name; }
    public double getLatitude()     { return this.latitude; }
    public double getLongitude()    { return this.longitude; }


    // Checks if the given coordinates actually identify a location on the earth
    private void checkCoordinatesValidity(double latitude, double longitude) throws IllegalArgumentException
    {
        if(Math.abs(latitude) > 90 || Math.abs(longitude) > 90)
            throw new IllegalArgumentException("Geographical coordinates of Location must be in range [-90, 90]");

        // Here we should query an external system to be sure that a location with the given coordinates exists on the earth,
        // for now we assume that all locations will exist
    }


    // Checks if the given name actually identify a location on the earth
    private void checkNameValidity(String name) throws IllegalArgumentException
    {
        for(int i = 0; i < name.length(); ++i)
        {
            if(Character.isDigit(name.charAt(i)))
                throw new IllegalArgumentException("Name of Location cannot contain digits");
        }

        // Here we should query an external system to be sure that a location with the given name exists on the earth,
        // for now we assume that all locations will exist
    }


    // Ensures that a location with the given name exists on the earth and retrieves the latitude and longitude of such location
    private void retrieveGeographicalCoordinates(String name)
    {
        checkNameValidity(name);

        // Here we should query an external system to obtain the latitude and longitude values for the location with the given name,
        // for now we simply generate some random values
        this.latitude = -90f + Math.random() * (90f + 90f);
        this.longitude = -90f + Math.random() * (90f + 90f);
    }

}
