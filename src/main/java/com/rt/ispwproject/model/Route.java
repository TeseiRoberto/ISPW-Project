package com.rt.ispwproject.model;


public class Route {

    private String departureLocation;
    private String arrivalLocation;


    public Route(String departureFrom, String arrivalTo) throws IllegalArgumentException
    {
        setDepartureLocation(departureFrom);
        setArrivalLocation(arrivalTo);
    }


    public void setDepartureLocation(String location) throws IllegalArgumentException
    {
        if(location == null || location.isEmpty())
            throw new IllegalArgumentException("Departure location cannot be empty!");

        if(location.equals(this.arrivalLocation))
            throw new IllegalArgumentException("Departure and arrival location cannot be the same!");

        this.departureLocation = location;
    }

    public void setArrivalLocation(String location) throws IllegalArgumentException
    {
        if(location == null || location.isEmpty())
            throw new IllegalArgumentException("Arrival location cannot be empty!");

        if(location.equals(this.departureLocation))
            throw new IllegalArgumentException("Departure and arrival location cannot be the same!");

        this.arrivalLocation = location;
    }

    public String getDepartureLocation()    { return this.departureLocation; }
    public String getArrivalLocation()      { return this.arrivalLocation; }
}
