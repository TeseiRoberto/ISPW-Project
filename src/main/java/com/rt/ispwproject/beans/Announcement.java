package com.rt.ispwproject.beans;

import com.rt.ispwproject.model.Accommodation;
import com.rt.ispwproject.model.AccommodationType;
import com.rt.ispwproject.model.TransportType;

import java.time.LocalDate;

public class Announcement {

    private int                 id;
    private String              owner;
    private String              destination;
    private String              holidayDescription;
    private int                 availableBudget;
    private int                 numOfTravelers;
    private LocalDate           dateOfPost;
    private LocalDate           departureDate;
    private LocalDate           returnDate;
    private AccommodationType   accommodationType;
    private int                 accommodationQuality;
    private int                 numOfRoomsRequired;
    private TransportType       transportType;
    private int                 transportQuality;
    private String              departureLocation;
    private int                 numOfViews;


    public Announcement()
    {
        this.id = -1;
        this.owner = "";
        this.destination = "";
        this.holidayDescription = "";
        this.availableBudget = -1;
        this.numOfTravelers = -1;
        this.dateOfPost = LocalDate.now();
        this.departureDate = LocalDate.now();
        this.returnDate = LocalDate.now();
        this.accommodationType = AccommodationType.UNSPECIFIED;
        this.accommodationQuality = -1;
        this.numOfRoomsRequired = -1;
        this.transportType = TransportType.UNSPECIFIED;
        this.transportQuality = -1;
        this.departureLocation = "";
        this.numOfViews = -1;
    }


    // Setters
    public void setId(int id)                                   { this.id = id; }
    public void setOwner(String owner)                          { this.owner = owner; }
    public void setDestination(String destination)              { this.destination = destination; }
    public void setHolidayDescription(String description)       { this.holidayDescription = description; }
    public void setAvailableBudget(int budget)                  { this.availableBudget = Math.max(0, budget); }
    public void setNumOfTravelers(int numOfTravelers)           { this.numOfTravelers = Math.max(0, numOfTravelers); }
    public void setDateOfPost(LocalDate date)                   { this.dateOfPost = date; }
    public void setDepartureDate(LocalDate date)                { this.departureDate = date; }
    public void setReturnDate(LocalDate date)                   { this.returnDate = date; }
    public void setAccommodationType(AccommodationType type)    { this.accommodationType = type; }
    public void setAccommodationQuality(int quality)            { this.accommodationQuality = Math.clamp(quality, 1, 5); }
    public void setNumOfRoomsRequired(int numOfRooms)           { this.numOfRoomsRequired = numOfRooms; }
    public void setTransportType(TransportType type)            { this.transportType = type; }
    public void setTransportQuality(int quality)                { this.transportQuality = Math.clamp(quality, 1, 5); }
    public void setDepartureLocation(String location)           { this.departureLocation = location; }
    public void setNumOfViews(int numOfViews)                   { this.numOfViews = Math.max(0, numOfViews); }


    // Getters
    public int getId()                                  { return this.id; }
    public String getOwner()                            { return this.owner; }
    public String getDestination()                      { return this.destination; }
    public String getHolidayDescription()               { return this.holidayDescription; }
    public int getAvailableBudget()                     { return this.availableBudget; }
    public int getNumOfTravelers()                      { return this.numOfTravelers; }
    public LocalDate getDateOfPost()                    { return this.dateOfPost; }
    public LocalDate getDepartureDate()                 { return this.departureDate; }
    public LocalDate getReturnDate()                    { return this.returnDate; }
    public AccommodationType getAccommodationType()     { return this.accommodationType; }
    public int getAccommodationQuality()                { return this.accommodationQuality; }
    public int getNumOfRoomsRequired()                  { return this.numOfRoomsRequired; }
    public TransportType getTransportType()             { return this.transportType; }
    public int getTransportQuality()                    { return this.transportQuality; }
    public String getDepartureLocation()                { return this.departureLocation; }
    public int getNumOfViews()                          { return this.numOfViews; }

}
