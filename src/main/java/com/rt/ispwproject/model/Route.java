package com.rt.ispwproject.model;


public class Route {

    private Location departureLocation;
    private Location arrivalLocation;


    public Route(Location departureFrom, Location arrivalTo) throws IllegalArgumentException
    {
        setDepartureLocation(departureFrom);
        setArrivalLocation(arrivalTo);
    }


    public void setDepartureLocation(Location location) throws IllegalArgumentException
    {
        if(location == null)
            throw new IllegalArgumentException("Departure location cannot be empty!");

        if(location.isEqual(this.arrivalLocation))
            throw new IllegalArgumentException("Departure and arrival location cannot be the same!");

        this.departureLocation = location;
    }

    public void setArrivalLocation(Location location) throws IllegalArgumentException
    {
        if(location == null)
            throw new IllegalArgumentException("Arrival location cannot be empty!");

        if(location.isEqual(this.departureLocation))
            throw new IllegalArgumentException("Departure and arrival location cannot be the same!");

        this.arrivalLocation = location;
    }

    public Location getDepartureLocation()    { return this.departureLocation; }
    public Location getArrivalLocation()      { return this.arrivalLocation; }
}
