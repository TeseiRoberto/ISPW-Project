package com.rt.ispwproject.model;

import java.security.SecureRandom;

public class Location {

    private final String    address;
    private double          latitude;
    private double          longitude;


    public Location(String address, double latitude, double longitude) throws IllegalArgumentException
    {
        checkCoordinatesValidity(latitude, longitude);
        checkAddressValidity(address);

        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public Location(String address) throws IllegalArgumentException
    {
        retrieveGeographicalCoordinates(address);
        this.address = address;
    }


    // Getters
    public String getAddress()      { return this.address; }
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


    // Checks if the given address actually identify a location on the earth
    private void checkAddressValidity(String name) throws IllegalArgumentException
    {
        if(name == null)
            throw new IllegalArgumentException("Location address cannot be null!");

        // Here we should query an external system to be sure that a location with the given address exists on the earth,
        // for now we assume that all locations will exist
    }


    // Ensures that a location with the given address exists on the earth and retrieves the latitude and longitude of such location
    private void retrieveGeographicalCoordinates(String address) throws IllegalArgumentException
    {
        checkAddressValidity(address);

        // Here we should query an external system to obtain the latitude and longitude values for the location with the given address,
        // for now we simply generate some random values
        SecureRandom random = new SecureRandom();
        this.latitude = -90.0 + random.nextDouble() * (90.0 + 90.0);
        this.longitude = -90.0 + random.nextDouble() * (90.0 + 90.0);
    }

}
