package com.rt.ispwproject.beans;

import com.rt.ispwproject.model.AccommodationType;
import com.rt.ispwproject.model.TransportType;

import java.time.LocalDate;

public class Announcement {

    private final int         id;
    private final String      owner;
    private final String      destination;
    private final String      holidayDescription;
    private final int         availableBudget;
    private final int         numOfTravelers;
    private final LocalDate   dateOfPost;
    private final LocalDate   departureDate;
    private final LocalDate   returnDate;

    private final AccommodationType     accommodationType;
    private final int                   accommodationQuality;
    private final int                   numOfRoomsRequired;
    private final TransportType         transportType;
    private final int                   transportQuality;
    private final int                   numOfViews;


    public Announcement(int id, String owner, String destination, String description, int budget,
                int numOfTravelers, LocalDate dateOfPost, LocalDate departureDate, LocalDate returnDate,
                AccommodationType accommodationType, int accommodationQuality, int numOfRoomsRequired,
                TransportType transportType, int transportQuality, int numOfViews)
    {
        this.id = id;
        this.owner = owner;
        this.destination = destination;
        this.holidayDescription = description;
        this.availableBudget = budget;
        this.numOfTravelers = numOfTravelers;
        this.dateOfPost = dateOfPost;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.accommodationType = accommodationType;
        this.accommodationQuality = accommodationQuality;
        this.numOfRoomsRequired = numOfRoomsRequired;
        this.transportType = transportType;
        this.transportQuality = transportQuality;
        this.numOfViews = numOfViews;
    }


    public int getId()                      { return this.id; }
    public String getOwner()                { return this.owner; }
    public String getDestination()          { return this.destination; }
    public String getHolidayDescription()   { return this.holidayDescription; }
    public int getAvailableBudget()         { return this.availableBudget; }
    public int getNumOfTravelers()          { return this.numOfTravelers; }
    public LocalDate getDateOfPost()        { return this.dateOfPost; }
    public LocalDate getDepartureDate()     { return this.departureDate; }
    public LocalDate getReturnDate()        { return this.returnDate; }

    public AccommodationType getAccommodationType()     { return this.accommodationType; }
    public int getAccommodationQuality()                { return this.accommodationQuality; }
    public int getNumOfRoomsRequired()                  { return this.numOfRoomsRequired; }
    public TransportType getTransportType()             { return this.transportType; }
    public int getTransportQuality()                    { return this.transportQuality; }
    public int getNumOfViews()                          { return this.numOfViews; }

}
