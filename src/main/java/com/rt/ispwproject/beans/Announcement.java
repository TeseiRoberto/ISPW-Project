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
    private Accommodation       accommodationReq;
    private Transport           transportReq;
    private int                 numOfViews;


    public Announcement()
    {
        this.id = -1;
        this.owner = "";
        this.destination = "";
        this.holidayDescription = "";
        this.availableBudget = 0;
        this.dateOfPost = LocalDate.now();
        this.holidayDuration = new Duration(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
        this.accommodationReq = new Accommodation(AccommodationType.UNSPECIFIED, 1, 1);
        this.transportReq = new Transport(TransportType.UNSPECIFIED, 1, "unknown", 1);
        this.numOfViews = 0;
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

        if(this.holidayDuration != null && date.isAfter(this.holidayDuration.getDepartureDate()))
            throw new IllegalArgumentException("Date of post cannot be after the departure date of the holiday!");

        this.dateOfPost = date;
    }

    public void setHolidayDuration(Duration duration)
    {
        if(duration == null)
            throw new IllegalArgumentException("Holiday duration cannot be empty!");

        if(this.dateOfPost != null && duration.getDepartureDate().isBefore(this.dateOfPost))
            throw new IllegalArgumentException("Departure date cannot be before the date of post of the announcement!");

        this.holidayDuration = duration;
    }


    public void setAccommodationRequirements(Accommodation accommodationReq) throws IllegalArgumentException
    {
        if(accommodationReq == null)
            throw new IllegalArgumentException("Accommodation requirements cannot be empty!");

        this.accommodationReq = accommodationReq;
    }

    public void setTransportRequirements(Transport transportReq) throws IllegalArgumentException
    {
        if(transportReq == null)
            throw new IllegalArgumentException("Transport requirements cannot be empty!");

        this.transportReq = transportReq;
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
    public String getAvailableBudgetAsStr()             { return Integer.toString(this.availableBudget) + '€'; }
    public LocalDate getDateOfPost()                    { return this.dateOfPost; }
    public Duration getHolidayDuration()                { return this.holidayDuration; }
    public Accommodation getAccommodationRequirements() { return this.accommodationReq; }
    public Transport getTransportRequirements()         { return this.transportReq; }
    public int getNumOfViews()                          { return this.numOfViews; }

}
