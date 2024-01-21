package com.rt.ispwproject.beans;

import com.rt.ispwproject.model.*;

import java.time.LocalDate;

public class Announcement {

    private int                 id;
    private String              owner;
    private String              destination;
    private String              holidayDescription;
    private int                 availableBudget;
    private LocalDate           dateOfPost;
    private Duration            holidayDuration;
    private AccommodationType   accommodationType;
    private int                 accommodationQuality;
    private int                 numOfRoomsRequired;
    private TransportType       transportType;
    private int                 transportQuality;
    private int                 numOfTravelers;
    private String              departureLocation;
    private int                 numOfViews;


    public Announcement()
    {
        this.id = -1;
        this.owner = "";
        this.destination = "";
        this.holidayDescription = "";
        this.availableBudget = -1;
        this.dateOfPost = LocalDate.now();
        this.holidayDuration = new Duration(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
        this.accommodationType = AccommodationType.UNSPECIFIED;
        this.accommodationQuality = -1;
        this.numOfRoomsRequired = -1;
        this.transportType = TransportType.UNSPECIFIED;
        this.transportQuality = -1;
        this.departureLocation = "";
        this.numOfTravelers = -1;
        this.numOfViews = -1;
    }


    // Setters
    public void setId(int id)                                   { this.id = id; }

    public void setOwner(String owner) throws IllegalArgumentException
    {
        if(owner == null || owner.isEmpty())
            throw new IllegalArgumentException("Owner name cannot be empty!");

        this.owner = owner;
    }

    public void setDestination(String destination) throws IllegalArgumentException
    {
        if(destination == null || destination.isEmpty())
            throw new IllegalArgumentException("Destination cannot be empty!");

        this.destination = destination;
    }

    public void setHolidayDescription(String description) throws IllegalArgumentException
    {
        this.holidayDescription = description;
    }

    public void setAvailableBudget(int availableBudget) throws IllegalArgumentException
    {
        if(availableBudget <= 0)
            throw new IllegalArgumentException("Available budget cannot be negative or zero!");

        this.availableBudget = availableBudget;
    }

    public void setDateOfPost(LocalDate date)
    {
        if(date == null)
            throw new IllegalArgumentException("Date of post cannot be empty!");

        if(this.holidayDuration != null && date.isAfter(this.holidayDuration.getStartDate()))
            throw new IllegalArgumentException("Date of post cannot be after the departure date of the holiday!");

        this.dateOfPost = date;
    }

    public void setHolidayDuration(Duration duration)
    {
        if(duration == null)
            throw new IllegalArgumentException("Holiday duration cannot be empty!");

        if(this.dateOfPost != null && duration.getStartDate().isBefore(this.dateOfPost))
            throw new IllegalArgumentException("Departure date cannot be before the date of post of the announcement!");

        this.holidayDuration = duration;
    }

    public void setAccommodationType(AccommodationType type) throws IllegalArgumentException
    {
        if(type == null)
            throw new IllegalArgumentException("Accommodation type cannot be empty!");

        this.accommodationType = type;
    }

    public void setAccommodationQuality(int quality) throws IllegalArgumentException
    {
        if(quality < 1 || quality > 5)
            throw new IllegalArgumentException("Accommodation quality must be in range [1, 5]!");

        this.accommodationQuality = quality;
    }

    public void setNumOfRoomsRequired(int numOfRooms) throws IllegalArgumentException
    {
        if(numOfRooms <= 0)
            throw new IllegalArgumentException("Number of rooms required cannot be negative or zero!");

        this.numOfRoomsRequired = numOfRooms;
    }

    public void setTransportType(TransportType type) throws IllegalArgumentException
    {
        if(type == null)
            throw new IllegalArgumentException("Transport type cannot be empty!");

        this.transportType = type;
    }

    public void setTransportQuality(int quality) throws IllegalArgumentException
    {
        if(quality < 1 || quality > 5)
            throw new IllegalArgumentException("Transport quality must be in range [1, 5]!");

        this.transportQuality = quality;
    }

    public void setNumOfTravelers(int numOfTravelers) throws IllegalArgumentException
    {
        if(numOfTravelers <= 0)
            throw new IllegalArgumentException("Number of travelers cannot be negative or zero!");

        this.numOfTravelers = numOfTravelers;
    }

    public void setDepartureLocation(String location) throws IllegalArgumentException
    {
        if(location == null || location.isEmpty())
            throw new IllegalArgumentException("Departure location cannot be empty!");

        this.departureLocation = location;
    }

    public void setNumOfViews(int numOfViews)
    {
        if(numOfViews < 0)
            throw new IllegalArgumentException("Number of views cannot be negative!");

        this.numOfViews = numOfViews;
    }


    // Getters
    public int getId()                                  { return this.id; }
    public String getOwner()                            { return this.owner; }
    public String getDestination()                      { return this.destination; }
    public String getHolidayDescription()               { return this.holidayDescription; }
    public int getAvailableBudget()                     { return this.availableBudget; }
    public LocalDate getDateOfPost()                    { return this.dateOfPost; }
    public Duration getHolidayDuration()                { return this.holidayDuration; }
    public AccommodationType getAccommodationType()     { return this.accommodationType; }
    public int getAccommodationQuality()                { return this.accommodationQuality; }
    public int getNumOfRoomsRequired()                  { return this.numOfRoomsRequired; }
    public TransportType getTransportType()             { return this.transportType; }
    public int getTransportQuality()                    { return this.transportQuality; }
    public int getNumOfTravelers()                      { return this.numOfTravelers; }
    public String getDepartureLocation()                { return this.departureLocation; }
    public int getNumOfViews()                          { return this.numOfViews; }

}
