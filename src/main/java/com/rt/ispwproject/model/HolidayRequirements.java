package com.rt.ispwproject.model;

import java.time.LocalDate;

public class HolidayRequirements {

    private int         id = -1;
    private String      owner = "";
    private String      destination = "";
    private String      holidayDescription = "";
    private int         availableBudget = 0;
    private int         numOfTravelers = 1;
    private LocalDate   dateOfPost = LocalDate.now();
    private LocalDate   departureDate = LocalDate.now();
    private LocalDate   returnDate = LocalDate.now();

    private AccommodationType   accommodationType = AccommodationType.UNSPECIFIED;
    private int                 accommodationQuality = 1;
    private int                 numOfRoomsRequired = 1;
    private TransportType       transportType = TransportType.UNSPECIFIED;
    private int                 transportQuality = 1;
    private int                 numOfViews = 0;

    // Setters
    public void setId(int id)                               { this.id = id; }
    public void setOwner(String owner)                      { this.owner = owner; }
    public void setDestination(String destination)          { this.destination = destination; }
    public void setHolidayDescription(String description)   { this.holidayDescription = description; }
    public void setAvailableBudget(int budget)              { this.availableBudget = budget; }
    public void setNumOfTravelers(int numOfTravelers)       { this.numOfTravelers = numOfTravelers; }
    public void setDateOfPost(LocalDate date)               { this.dateOfPost = date; }
    public void setDepartureDate(LocalDate date)            { this.departureDate = date; }
    public void setReturnDate(LocalDate date)               { this.returnDate = date; }

    public void setAccommodationType(AccommodationType type) { this.accommodationType = type; }
    public void setAccommodationQuality(int quality)        { this.accommodationQuality = Math.clamp(quality, 1, 5 ); }
    public void setNumOfRoomsRequired(int numOfRooms)       { this.numOfRoomsRequired = Math.max(numOfRooms, 0); }
    public void setTransportType(TransportType type)        { this.transportType = type; }
    public void setTransportQuality(int quality)            { this.transportQuality = Math.clamp(quality, 1, 5 ); }
    public void setNumOfViews(int num)                      { this.numOfViews = Math.max(0, num); }

    // Getters
    public int getId()                                      { return this.id; }
    public String getOwner()                                { return this.owner; }
    public String getDestination()                          { return this.destination; }
    public String getHolidayDescription()                   { return this.holidayDescription; }
    public int getAvailableBudget()                         { return this.availableBudget; }
    public int getNumOfTravelers()                          { return this.numOfTravelers; }
    public LocalDate getDateOfPost()                        { return this.dateOfPost; }
    public LocalDate getDepartureDate()                     { return this.departureDate; }
    public LocalDate getReturnDate()                        { return this.returnDate; }

    public AccommodationType getAccommodationType()         { return this.accommodationType; }
    public int getAccommodationQuality()                    { return this.accommodationQuality; }
    public int getNumOfRoomsRequired()                      { return this.numOfRoomsRequired; }
    public TransportType getTransportType()                 { return this.transportType; }
    public int getTransportQuality()                        { return this.transportQuality; }
    public int getNumOfViews()                              { return this.numOfViews; }


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
    }

}
