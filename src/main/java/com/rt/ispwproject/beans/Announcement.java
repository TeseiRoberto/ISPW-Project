package com.rt.ispwproject.beans;


import java.time.LocalDate;

public class Announcement {

    private int                 id;
    private String              ownerUsername;                  // Username of the user that has posted this announcement
    private String              holidayDescription;
    private int                 availableBudget;
    private LocalDate           dateOfPost;
    private Duration            holidayDuration;
    private Accommodation       accommodationReq;
    private Transport           transportReq;
    private int                 numOfViews;
    private int                 numOfOffersReceived;            // Number of offers that this announcement has received


    public Announcement()
    {
        this.id = 0;
        this.ownerUsername = "";
        this.holidayDescription = "";
        this.availableBudget = 0;
        this.dateOfPost = null;
        this.holidayDuration = new Duration();
        this.accommodationReq = new Accommodation();
        this.transportReq = new Transport();
        this.numOfViews = 0;
        this.numOfOffersReceived = 0;
    }


    public Announcement(String ownerUsername, String holidayDescription, int availableBudget,
                        Duration holidayDuration, Accommodation accommodationReq, Transport transportReq) throws IllegalArgumentException
    {
        this.id = 0;
        setOwnerUsername(ownerUsername);
        setHolidayDescription(holidayDescription);
        setAvailableBudget(availableBudget);
        setDateOfPost(LocalDate.now());
        setHolidayDuration(holidayDuration);
        setAccommodationRequirements(accommodationReq);
        setTransportRequirements(transportReq);
        this.numOfViews = 0;
        this.numOfOffersReceived = 0;
    }


    // Setters
    public void setId(int id)               { this.id = id; }

    public void setOwnerUsername(String ownerUsername) throws IllegalArgumentException
    {
        if(ownerUsername == null || ownerUsername.isEmpty())
            throw new IllegalArgumentException("Owner name cannot be empty!");

        this.ownerUsername = ownerUsername;
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

        if(this.holidayDuration != null && this.holidayDuration.getDepartureDate() != null && date.isAfter(this.holidayDuration.getDepartureDate()))
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

    public void setNumOfOffersReceived(int numOfOffers)
    {
        if(numOfOffers < 0)
            throw new IllegalArgumentException("Number of views cannot be negative!");

        this.numOfOffersReceived = numOfOffers;
    }


    // Getters
    public int getId()                                  { return this.id; }
    public String getOwnerUsername()                    { return this.ownerUsername; }
    public String getDestination()                      { return this.transportReq.getArrivalLocation(); }
    public String getHolidayDescription()               { return this.holidayDescription; }
    public int getAvailableBudget()                     { return this.availableBudget; }
    public String getAvailableBudgetAsStr()             { return Integer.toString(this.availableBudget) + '€'; }
    public LocalDate getDateOfPost()                    { return this.dateOfPost; }
    public Duration getHolidayDuration()                { return this.holidayDuration; }
    public Accommodation getAccommodationRequirements() { return this.accommodationReq; }
    public Transport getTransportRequirements()         { return this.transportReq; }
    public int getNumOfViews()                          { return this.numOfViews; }
    public int getNumOfOffersReceived()                 { return this.numOfOffersReceived; }

}
