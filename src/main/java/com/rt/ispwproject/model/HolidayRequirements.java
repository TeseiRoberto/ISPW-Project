package com.rt.ispwproject.model;

import com.rt.ispwproject.beans.Announcement;
import com.rt.ispwproject.beans.Duration;

import java.time.LocalDate;

public class HolidayRequirements {

    private int             id;
    private String          owner;
    private String          holidayDescription;
    private LocalDate       dateOfPost;
    private int             numOfViews;
    private boolean         satisfied;
    private final Holiday   requirements;


    public HolidayRequirements()
    {
        this.id = -1;
        this.owner = "";
        this.holidayDescription = "";
        this.dateOfPost = LocalDate.now();
        this.numOfViews = 0;
        this.satisfied = false;
        this.requirements = new Holiday("", 0, LocalDate.now(), LocalDate.now(), new Accommodation(), new Transport());
    }


    // Setters
    public void setId(int id)                               { this.id = id; }
    public void setOwner(String owner)                      { this.owner = owner; }
    public void setHolidayDescription(String description)   { this.holidayDescription = description; }
    public void setDateOfPost(LocalDate date)               { this.dateOfPost = date; }
    public void setNumOfViews(int num)                      { this.numOfViews = Math.max(0, num); }
    public void setDestination(String destination)          { this.requirements.setDestination(destination); }
    public void setAvailableBudget(int budget)              { this.requirements.setPrice(budget); }
    public void setNumOfTravelers(int numOfTravelers)       { this.requirements.getTransport().setNumOfTravelers(numOfTravelers); }
    public void setDepartureDate(LocalDate date)            { this.requirements.setStartDate(date); }
    public void setReturnDate(LocalDate date)               { this.requirements.setEndDate(date); }
    public void setAccommodationType(AccommodationType type) { this.requirements.getAccommodation().setType(type); }
    public void setAccommodationQuality(int quality)        { this.requirements.getAccommodation().setQuality(quality); }
    public void setNumOfRoomsRequired(int numOfRooms)       { this.requirements.getAccommodation().setNumOfRooms(numOfRooms); }
    public void setTransportType(TransportType type)        { this.requirements.getTransport().setType(type); }
    public void setTransportQuality(int quality)            { this.requirements.getTransport().setQuality(quality); }
    public void setDepartureLocation(String location)       { this.requirements.getTransport().setDepartureLocation(location); }
    public void setSatisfied(boolean value)                 { this.satisfied = value; }


    // Getters
    public int getId()                                      { return this.id; }
    public String getOwner()                                { return this.owner; }
    public String getHolidayDescription()                   { return this.holidayDescription; }
    public LocalDate getDateOfPost()                        { return this.dateOfPost; }
    public int getNumOfViews()                              { return this.numOfViews; }
    public String getDestination()                          { return this.requirements.getDestination(); }
    public int getAvailableBudget()                         { return this.requirements.getPrice(); }
    public int getNumOfTravelers()                          { return this.requirements.getTransport().getNumOfTravelers(); }
    public LocalDate getDepartureDate()                     { return this.requirements.getStartDate(); }
    public LocalDate getReturnDate()                        { return this.requirements.getEndDate(); }
    public AccommodationType getAccommodationType()         { return this.requirements.getAccommodation().getType(); }
    public int getAccommodationQuality()                    { return this.requirements.getAccommodation().getQuality(); }
    public int getNumOfRoomsRequired()                      { return this.requirements.getAccommodation().getNumOfRooms(); }
    public TransportType getTransportType()                 { return this.requirements.getTransport().getType(); }
    public int getTransportQuality()                        { return this.requirements.getTransport().getQuality(); }
    public String getDepartureLocation()                    { return this.requirements.getTransport().getDepartureLocation(); }
    public boolean isSatisfied()                            { return this.satisfied; }


    // Converts an HolidayRequirements instance into an Announcement instance (model to bean class conversion)
    // Note: this creates coupling between the 2 classes but alleviates the burden of conversion from the developer and eliminates code duplication
    public Announcement toAnnouncement() throws IllegalArgumentException
    {
        Announcement a = new Announcement();
        a.setId(this.getId());
        a.setOwner(this.getOwner());
        a.setDestination(this.getDestination());
        a.setHolidayDescription(this.getHolidayDescription());
        a.setAvailableBudget(this.getAvailableBudget());
        a.setDateOfPost(this.getDateOfPost());
        a.setHolidayDuration( new Duration(this.getDepartureDate(), this.getReturnDate()) );
        a.setAccommodationType(this.getAccommodationType());
        a.setAccommodationQuality(this.getAccommodationQuality());
        a.setNumOfRoomsRequired(this.getNumOfRoomsRequired());
        a.setTransportType(this.getTransportType());
        a.setTransportQuality(this.getTransportQuality());
        a.setDepartureLocation(this.getDepartureLocation());
        a.setNumOfTravelers(this.getNumOfTravelers());
        a.setNumOfViews(this.getNumOfViews());

        return a;
    }

}
