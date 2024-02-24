package com.rt.ispwproject.model;

import com.rt.ispwproject.beans.Accommodation;
import com.rt.ispwproject.beans.Announcement;
import com.rt.ispwproject.beans.Duration;
import com.rt.ispwproject.beans.Transport;

import java.time.LocalDate;


public class HolidayRequirements {

    private final HolidayRequirementsMetadata   metadata;
    private String                              holidayDescription;
    private Location                            destination;
    protected int                               budget;
    private final DateRange                     duration;
    private final AccommodationRequirements     accommodationReq;
    private final TransportRequirements         transportReq;


    public HolidayRequirements(HolidayRequirementsMetadata metadata, Location destination, String description, DateRange duration, int budget,
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
    public void setHolidayDescription(String description)           { this.holidayDescription = description; }
    public void setDestination(Location destination)                { this.destination = destination; }
    public void setBudget(int budget)                               { this.budget = budget; }
    public void setDepartureDate(LocalDate date)                    { this.duration.setStartDate(date); }
    public void setReturnDate(LocalDate date)                       { this.duration.setEndDate(date); }


    // Getters
    public HolidayRequirementsMetadata getMetadata()                { return this.metadata; }
    public String getHolidayDescription()                           { return this.holidayDescription; }
    public Location getDestination()                                { return this.destination; }
    public int getBudget()                                          { return this.budget; }
    public LocalDate getDepartureDate()                             { return this.duration.getStartDate(); }
    public LocalDate getReturnDate()                                { return this.duration.getEndDate(); }
    public AccommodationRequirements getAccommodationRequirements() { return this.accommodationReq; }
    public TransportRequirements getTransportRequirements()         { return this.transportReq; }


    // Converts an HolidayRequirements instance into an Announcement instance (model to bean class conversion)
    // Note: this creates coupling between the 2 classes but alleviates the burden of conversion from the developer and eliminates code duplication
    public Announcement toAnnouncementBean() throws IllegalArgumentException
    {
        Accommodation accommodation = accommodationReq.toAccommodationBean();
        Transport transport = transportReq.toTransportBean();

        Announcement a = new Announcement();
        a.setId(this.metadata.getRequirementsId());
        a.setOwnerUsername(this.metadata.getRequirementsOwner().getUsername());
        a.setNumOfOffersReceived(this.metadata.getNumOfOffersReceived());
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
