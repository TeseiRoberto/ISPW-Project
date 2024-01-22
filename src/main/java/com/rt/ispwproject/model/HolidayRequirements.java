package com.rt.ispwproject.model;

import com.rt.ispwproject.beans.Announcement;
import com.rt.ispwproject.beans.Duration;


public class HolidayRequirements extends Holiday {

    private String  holidayDescription;


    public HolidayRequirements(HolidayMetadata metadata, String destination, String description, DateRange duration, int availableBudget, Accommodation accommodation, Transport transport)
    {
        super(metadata, destination, availableBudget, duration, accommodation, transport);
        this.holidayDescription = description;
    }


    // Setters
    public void setHolidayDescription(String description)   { this.holidayDescription = description; }
    public void setAvailableBudget(int budget)              { this.price = budget; }


    // Getters
    public String getHolidayDescription()       { return this.holidayDescription; }
    public int getAvailableBudget()             { return this.price; }


    // Converts an HolidayRequirements instance into an Announcement instance (model to bean class conversion)
    // Note: this creates coupling between the 2 classes but alleviates the burden of conversion from the developer and eliminates code duplication
    public Announcement toAnnouncement() throws IllegalArgumentException
    {
        Announcement a = new Announcement();
        a.setId(this.getMetadata().getHolidayId());
        a.setOwner(this.getMetadata().getOwnerUsername());
        a.setNumOfViews(this.getMetadata().getNumOfViews());
        a.setDestination(this.getDestination());
        a.setHolidayDescription(this.getHolidayDescription());
        a.setHolidayDuration( new Duration(this.getDepartureDate(), this.getReturnDate()) );
        a.setAvailableBudget(this.getAvailableBudget());
        a.setDateOfPost(this.getMetadata().getDateOfPost());
        a.setAccommodationType(this.getAccommodation().getType());
        a.setAccommodationQuality(this.getAccommodation().getQuality());
        a.setNumOfRoomsRequired(this.getAccommodation().getNumOfRooms());
        a.setTransportType(this.getTransport().getType());
        a.setTransportQuality(this.getTransport().getQuality());
        a.setDepartureLocation(this.getTransport().getDepartureLocation());
        a.setNumOfTravelers(this.getTransport().getNumOfTravelers());

        return a;
    }

}
