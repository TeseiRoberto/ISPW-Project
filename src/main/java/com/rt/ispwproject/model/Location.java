package com.rt.ispwproject.model;

import java.security.SecureRandom;

public class Location {

    private final String    address;
    private double          latitude;
    private double          longitude;


    public Location(String address, double latitude, double longitude) throws IllegalArgumentException
    {
        checkLocationValidity(address, latitude, longitude);

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


    // Check if the given name, latitude and longitude actually identifies a location on the earth
    private void checkLocationValidity(String name, double latitude, double longitude) throws IllegalArgumentException
    {
        if(name == null)
            throw new IllegalArgumentException("Location address cannot be null!");

        if(Math.abs(latitude) > 90 || Math.abs(longitude) > 90)
            throw new IllegalArgumentException("Geographical coordinates of Location must be in range [-90, 90]");

        // Here we should check that the given address exists on the earth and corresponds to the given geographical coordinates.
        // To do so we should introduce a class responsible for querying an external system/API,
        // For now we assume that all locations will exist (because we are not using any external system/API).
    }


    // Ensures that a location with the given address exists on the earth and retrieves the latitude and longitude of such location
    private void retrieveGeographicalCoordinates(String address) throws IllegalArgumentException
    {
        if(address == null)
            throw new IllegalArgumentException("Location address cannot be null!");

        // Here we should check that the given address exists on the earth, and we should obtain the
        // geographical coordinates of it.
        // To do so we should introduce a class responsible for querying an external system/API,
        // for now we simply generate some random coordinate values (because we are not using any external system/API).
        SecureRandom random = new SecureRandom();
        this.latitude = -90.0 + random.nextDouble() * (90.0 + 90.0);
        this.longitude = -90.0 + random.nextDouble() * (90.0 + 90.0);
    }

}
