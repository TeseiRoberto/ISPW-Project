package com.rt.ispwproject.beans;


// This class represents the changes that a user has requested on an offer received from a travel agency, note that if a field is
// null, zero or an empty string then no change is required on such field.
// Before calling a getter method you should check that a change is requested for the value that you are getting using the is*ChangeRequired method
public class ChangesOnOffer {

    private final int                   id;
    private final String                ownerUsername;
    private final int                   relativeOfferId;
    private final String                bidderUsername;
    private String                      changesDescription = "";
    private Duration                    holidayDuration = null;
    private int                         requiredPrice = 0;
    private Accommodation               accommodationChanges = null;
    private Transport                   transportChanges = null;


    public ChangesOnOffer(int id, String ownerUsername, int relativeOfferId, String bidderUsername)
    {
        this.id = id;
        this.ownerUsername = ownerUsername;
        this.relativeOfferId = relativeOfferId;
        this.bidderUsername = bidderUsername;
    }


    public ChangesOnOffer(String ownerUsername, int relativeOfferId, String bidderUsername)
    {
        this.id = 0;
        this.ownerUsername = ownerUsername;
        this.relativeOfferId = relativeOfferId;
        this.bidderUsername = bidderUsername;
    }


    // Setters
    public void setChangesDescription(String description)                   { this.changesDescription = description == null ? "" : description; }
    public void setHolidayDuration(Duration duration)                       { this.holidayDuration = duration; }
    public void setPrice(int price)                                         { this.requiredPrice = price; }
    public void setAccommodationChanges(Accommodation changes)              { this.accommodationChanges = changes; }
    public void setTransportChanges(Transport changes)                      { this.transportChanges = changes; }


    // Getters
    public int getId()                              { return this.id; }
    public String getOwnerUsername()                { return this.ownerUsername; }
    public int getRelativeOfferId()                 { return this.relativeOfferId; }
    public String getBidderUsername()               { return this.bidderUsername; }
    public String getChangesDescription()           { return this.changesDescription; }
    public Duration getHolidayDuration()            { return this.holidayDuration; }
    public int getPrice()                           { return this.requiredPrice; }
    public String getPriceAsStr()                   { return this.requiredPrice < 0 ? "0€" : this.requiredPrice + "€"; }
    public Accommodation getAccommodationChanges()  { return this.accommodationChanges; }
    public Transport getTransportChanges()          { return this.transportChanges; }


    public boolean isDurationChangeRequired()       { return this.holidayDuration != null; }
    public boolean isPriceChangeRequired()          { return this.requiredPrice > 0; }
    public boolean isAccommodationChangeRequired()  { return this.accommodationChanges != null; }
    public boolean isTransportChangeRequired()      { return this.transportChanges != null; }
}
