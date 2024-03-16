package com.rt.ispwproject.model;

import com.rt.ispwproject.beans.Announcement;
import com.rt.ispwproject.beans.Duration;


public class HolidayRequirements {

    private final HolidayRequirementsMetadata   metadata;
    private String                              holidayDescription;
    protected int                               availableBudget;
    private final DateRange                     holidayDuration;
    private final AccommodationRequirements     accommodationReq;
    private final TransportRequirements         transportReq;


    public HolidayRequirements(HolidayRequirementsMetadata metadata, String description, DateRange duration, int availableBudget,
                               AccommodationRequirements accommodationReq, TransportRequirements transportReq)
    {
        this.metadata = metadata;
        this.holidayDescription = description;
        this.holidayDuration = duration;
        this.availableBudget = availableBudget;
        this.accommodationReq = accommodationReq;
        this.transportReq = transportReq;
    }


    // Getters
    public HolidayRequirementsMetadata getMetadata()                { return this.metadata; }
    public String getHolidayDescription()                           { return this.holidayDescription; }
    public int getAvailableBudget()                                 { return this.availableBudget; }
    public DateRange getHolidayDuration()                           { return this.holidayDuration; }
    public AccommodationRequirements getAccommodationRequirements() { return this.accommodationReq; }
    public TransportRequirements getTransportRequirements()         { return this.transportReq; }


    // Converts an HolidayRequirements instance into an Announcement instance (model to bean class conversion)
    // Note: this creates coupling between the 2 classes but alleviates the burden of conversion from the developer and eliminates code duplication
    public Announcement toAnnouncementBean() throws IllegalArgumentException
    {
        Announcement a = new Announcement(
                this.metadata.getRequirementsOwner().getUsername(),
                this.holidayDescription,
                this.availableBudget,
                new Duration( this.holidayDuration.getStartDate(), this.holidayDuration.getEndDate()),
                this.accommodationReq.toAccommodationBean(),
                this.transportReq.toTransportBean()
        );

        a.setId(this.metadata.getRequirementsId());
        a.setDateOfPost(this.metadata.getDateOfPost());
        a.setNumOfViews(this.getMetadata().getNumOfViews());
        a.setNumOfOffersReceived(this.metadata.getNumOfOffersReceived());
        return a;
    }

}
