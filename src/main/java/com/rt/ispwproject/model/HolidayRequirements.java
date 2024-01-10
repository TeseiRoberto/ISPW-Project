package com.rt.ispwproject.model;

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

    /* TODO: This should be moved into the bean class
    // Verify that the holiday requirements are logically valid, if they are not an exception is thrown
    public void checkValidity() throws RuntimeException
    {
        boolean areValid = true;
        String errorMsg = "Holiday requirements are not valid:";

        if(this.owner.isEmpty())
        {
            areValid = false;
            errorMsg += " owner cannot be empty,";
        }

        if(this.destination.isEmpty())
        {
            areValid = false;
            errorMsg += " destination cannot be empty,";
        }

        if(this.availableBudget <= 0)
        {
            areValid = false;
            errorMsg += " available budget cannot be negative,";
        }

        if(this.numOfTravelers <= 0)
        {
            areValid = false;
            errorMsg += " number of travelers cannot be minor than 1,";
        }

        if(this.numOfRoomsRequired <= 0)
        {
            areValid = false;
            errorMsg += " number of rooms cannot be minor than 1,";
        }

        if(!this.departureDate.isBefore(this.returnDate))
        {
            areValid = false;
            errorMsg += " the return date cannot be earlier than the departure date,";
        }

        if(!this.dateOfPost.isBefore(departureDate))
        {
            areValid = false;
            errorMsg += " the departure date cannot be earlier than the date of post,";
        }

        if(!areValid)
        {
            errorMsg = errorMsg.substring(0, errorMsg.length() - 1) + '.';  // Replace last comma with a dot
            throw new RuntimeException(errorMsg);
        }
    }*/

}
