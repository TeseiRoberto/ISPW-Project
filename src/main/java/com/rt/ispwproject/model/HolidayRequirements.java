package com.rt.ispwproject.model;

import com.rt.ispwproject.beans.Announcement;
import com.rt.ispwproject.beans.Duration;

import java.time.LocalDate;


public class HolidayRequirements {

    private final HolidayMetadata       metadata;
    private String                      holidayDescription;
    private String                      destination;
    protected int                       budget;
    private final DateRange             duration;
    private AccommodationRequirements   accommodation;
    private TransportRequirements       transport;


    public HolidayRequirements(HolidayMetadata metadata, String destination, String description, DateRange duration, int budget,
                               AccommodationRequirements accommodationReq, TransportRequirements transportReq)
    {
        this.metadata = metadata;
        this.destination = destination;
        this.holidayDescription = description;
        this.duration = duration;
        this.budget = budget;
        this.accommodation = accommodationReq;
        this.transport = transportReq;
    }


    // Setters
    public void setHolidayDescription(String description)       { this.holidayDescription = description; }
    public void setDestination(String destination)              { this.destination = destination; }
    public void setBudget(int budget)                           { this.budget = budget; }
    public void setDepartureDate(LocalDate date)                { this.duration.setStartDate(date); }
    public void setReturnDate(LocalDate date)                   { this.duration.setEndDate(date); }
    public void setAccommodation(AccommodationRequirements req) { this.accommodation = req; }
    public void setTransport(TransportRequirements req)         { this.transport = req; }


    // Getters
    public HolidayMetadata getMetadata()                    { return this.metadata; }
    public String getHolidayDescription()                   { return this.holidayDescription; }
    public String getDestination()                          { return this.destination; }
    public int getBudget()                                  { return this.budget; }
    public LocalDate getDepartureDate()                     { return this.duration.getStartDate(); }
    public LocalDate getReturnDate()                        { return this.duration.getEndDate(); }
    public AccommodationRequirements getAccommodation()     { return this.accommodation; }
    public TransportRequirements getTransport()             { return this.transport; }


    // Converts an HolidayRequirements instance into an Announcement instance (model to bean class conversion)
    // Note: this creates coupling between the 2 classes but alleviates the burden of conversion from the developer and eliminates code duplication
    public Announcement toAnnouncement() throws IllegalArgumentException
    {
        Announcement a = new Announcement();
        a.setId(this.metadata.getHolidayId());
        a.setOwner(this.metadata.getOwnerUsername());
        a.setNumOfViews(this.metadata.getNumOfViews());
        a.setDestination(this.destination);
        a.setHolidayDescription(this.holidayDescription);
        a.setHolidayDuration( new Duration(this.getDepartureDate(), this.getReturnDate()) );
        a.setAvailableBudget(this.budget);
        a.setDateOfPost(this.metadata.getDateOfPost());
        a.setAccommodationType(this.accommodation.getType());
        a.setAccommodationQuality(this.accommodation.getQuality());
        a.setNumOfRoomsRequired(this.accommodation.getNumOfRooms());
        a.setTransportType(this.transport.getType());
        a.setTransportQuality(this.transport.getQuality());
        a.setDepartureLocation(this.transport.getDepartureLocation());
        a.setNumOfTravelers(this.transport.getNumOfTravelers());

        return a;
    }

}
