package com.rt.ispwproject.model;

import com.rt.ispwproject.beans.Accommodation;
import com.rt.ispwproject.beans.Announcement;
import com.rt.ispwproject.beans.Duration;
import com.rt.ispwproject.beans.Transport;

import java.time.LocalDate;


public class HolidayRequirements {

    private final HolidayMetadata       metadata;
    private String                      holidayDescription;
    private Location                    destination;
    protected int                       budget;
    private final DateRange             duration;
    private AccommodationRequirements   accommodationReq;
    private TransportRequirements       transportReq;


    public HolidayRequirements(HolidayMetadata metadata, Location destination, String description, DateRange duration, int budget,
                               AccommodationRequirements accommodationReq, TransportRequirements transportReq)
    {
        this.metadata = metadata;
        this.destination = destination;
        this.holidayDescription = description;
        this.duration = duration;
        this.budget = budget;
        this.accommodationReq = accommodationReq;
        this.transportReq = transportReq;
    }


    // Setters
    public void setHolidayDescription(String description)       { this.holidayDescription = description; }
    public void setDestination(Location destination)            { this.destination = destination; }
    public void setBudget(int budget)                           { this.budget = budget; }
    public void setDepartureDate(LocalDate date)                { this.duration.setStartDate(date); }
    public void setReturnDate(LocalDate date)                   { this.duration.setEndDate(date); }
    public void setAccommodation(AccommodationRequirements req) { this.accommodationReq = req; }
    public void setTransport(TransportRequirements req)         { this.transportReq = req; }


    // Getters
    public HolidayMetadata getMetadata()                    { return this.metadata; }
    public String getHolidayDescription()                   { return this.holidayDescription; }
    public Location getDestination()                        { return this.destination; }
    public int getBudget()                                  { return this.budget; }
    public LocalDate getDepartureDate()                     { return this.duration.getStartDate(); }
    public LocalDate getReturnDate()                        { return this.duration.getEndDate(); }
    public AccommodationRequirements getAccommodation()     { return this.accommodationReq; }
    public TransportRequirements getTransport()             { return this.transportReq; }


    // Converts an HolidayRequirements instance into an Announcement instance (model to bean class conversion)
    // Note: this creates coupling between the 2 classes but alleviates the burden of conversion from the developer and eliminates code duplication
    public Announcement toAnnouncement() throws IllegalArgumentException
    {
        Accommodation accommodation = new Accommodation(
                accommodationReq.getType(),
                accommodationReq.getQuality(),
                accommodationReq.getNumOfRooms()
        );

        Transport transport = new Transport(
            transportReq.getType(),
            transportReq.getQuality(),
            transportReq.getDepartureLocation().getAddress(),
            transportReq.getNumOfTravelers()
        );

        Announcement a = new Announcement();
        a.setId(this.metadata.getHolidayId());
        a.setOwner(this.metadata.getOwnerUsername());
        a.setNumOfViews(this.metadata.getNumOfViews());
        a.setDestination(this.destination.getAddress());
        a.setHolidayDescription(this.holidayDescription);
        a.setHolidayDuration( new Duration(this.getDepartureDate(), this.getReturnDate()) );
        a.setAvailableBudget(this.budget);
        a.setDateOfPost(this.metadata.getDateOfPost());
        a.setAccommodationRequirements(accommodation);
        a.setTransportRequirements(transport);

        return a;
    }

}
