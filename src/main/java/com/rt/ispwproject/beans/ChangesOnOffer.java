package com.rt.ispwproject.beans;


// This class represents the changes that a user requests on an offer received from a travel agency, note that if a field is
// null, negative or an empty string then no change is required on such field.
// Before calling a getter method you should check that a change is requested for the value that you are getting using the has*Change methods
public class ChangesOnOffer {

    private final int                   id;
    private final String                ownerUsername;
    private final int                   relativeOfferId;
    private final String                bidderUsername;
    private String                      changesDescription = "";
    private String                      destination = "";
    private Duration                    duration = null;
    private int                         price = -1;                     // A negative value means that no change is required
    private Accommodation               accommodationChanges = null;
    private Transport                   transportChanges = null;


    public ChangesOnOffer(int id, String ownerUsername, int relativeOfferId, String bidderUsername)
    {
        this.id = id;
        this.ownerUsername = ownerUsername;
        this.relativeOfferId = relativeOfferId;
        this.bidderUsername = bidderUsername;
    }


    public ChangesOnOffer(int relativeOfferId, String bidderUsername)
    {
        this.id = -1;
        this.ownerUsername = "";
        this.relativeOfferId = relativeOfferId;
        this.bidderUsername = bidderUsername;
    }


    // Setters
    public void setChangesDescription(String description)                   { this.changesDescription = description == null ? "" : description; }
    public void setDestination(String destination)                          { this.destination = destination == null ? "" : destination; }
    public void setDuration(Duration duration)                              { this.duration = duration; }
    public void setPrice(int price)                                         { this.price = price; }
    public void setAccommodationChanges(Accommodation changes)              { this.accommodationChanges = changes; }
    public void setTransportChanges(Transport changes)                      { this.transportChanges = changes; }


    // Getters
    public int getId()                              { return this.id; }
    public String getOwnerUsername()                { return this.ownerUsername; }
    public int getRelativeOfferId()                 { return this.relativeOfferId; }
    public String getBidderUsername()               { return this.bidderUsername; }
    public String getChangesDescription()           { return this.changesDescription; }
    public String getDestination()                  { return this.destination; }
    public Duration getDuration()                   { return this.duration; }
    public int getPrice()                           { return this.price; }
    public String getPriceAsStr()                   { return this.price < 0 ? "0€" : this.price + "€"; }
    public Accommodation getAccommodationChanges()  { return this.accommodationChanges; }
    public Transport getTransportChanges()          { return this.transportChanges; }


    public boolean isDestinationChangeRequired()    { return this.destination != null && !this.destination.isEmpty(); }
    public boolean isDurationChangeRequired()       { return this.duration != null; }
    public boolean isPriceChangeRequired()          { return this.price > 0; }
    public boolean isAccommodationChangeRequired()  { return this.accommodationChanges != null; }
    public boolean isTransportChangeRequired()      { return this.transportChanges != null; }
}
