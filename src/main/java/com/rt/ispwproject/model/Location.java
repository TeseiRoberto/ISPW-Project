package com.rt.ispwproject.model;

public class Location {

    private final String    address;


    public Location(String address) throws IllegalArgumentException
    {
        if(address == null || address.isEmpty())
            throw new IllegalArgumentException("Location address cannot be empty!");

        this.address = address;
    }


    // Getters
    public String getAddress()      { return this.address; }

    // Returns true if this instance has the same address of the given instance
    public boolean isEqual(Location other)
    {
        if(other == null)
            return false;

        return this.address.equals(other.address);
    }
}
