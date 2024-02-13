package com.rt.ispwproject.beans;


import java.time.LocalDate;

public class Offer {

    private int                 id;
    private String              bidderAgencyUsername;
    private String              relativeReqOwnerUsername;
    private String              offerStatus;
    private String              destination;
    private Duration            duration;
    private int                 price;
    private Accommodation       accommodation;
    private Transport           transport;
    private boolean             hasRequestOfChanges;


    public Offer(String bidderAgencyUsername, String relativeReqOwnerUsername,String destination,
                 Duration duration, int price, Accommodation accommodation, Transport transport) throws IllegalArgumentException
    {
        this.offerStatus = "unknown";
        setBidderUsername(bidderAgencyUsername);
        setRelativeRequirementsOwnerUsername(relativeReqOwnerUsername);
        setDestination(destination);
        setDuration(duration);
        setPrice(price);
        setAccommodationOffer(accommodation);
        setTransportOffer(transport);
        this.hasRequestOfChanges = false;
    }


    // Setters
    public void setId(int id)   { this.id = id; }

    public void setBidderUsername(String agencyUsername) throws IllegalArgumentException
    {
        if(agencyUsername == null || agencyUsername.isEmpty())
            throw new IllegalArgumentException("Bidder agency username cannot be empty!");

        this.bidderAgencyUsername = agencyUsername;
    }

    public void setRelativeRequirementsOwnerUsername(String username) throws IllegalArgumentException
    {
        if(username == null || username.isEmpty())
            throw new IllegalArgumentException("relative requirements owner username cannot be empty!");

        this.relativeReqOwnerUsername = username;
    }

    public void setOfferStatus(String status) throws IllegalArgumentException
    {
        if(status == null || status.isEmpty())
            throw new IllegalArgumentException("Offer status cannot be empty!");

        this.offerStatus = status;
    }

    public void setDestination(String destination) throws IllegalArgumentException
    {
        if(destination == null || destination.isEmpty())
            throw new IllegalArgumentException("Destination cannot be empty!");

        this.destination = destination;
    }

    public void setDuration(Duration duration) throws IllegalArgumentException
    {
        if(duration == null)
            throw new IllegalArgumentException("Duration cannot be empty!");

        if(!duration.getDepartureDate().isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Offer departure date cannot be before (or equal to) the current date!");

        this.duration = duration;
    }

    public void setPrice(int price) throws IllegalArgumentException
    {
        if(price <= 0)
            throw new IllegalArgumentException("Offer price cannot be negative or zero!");

        this.price = price;
    }

    public void setAccommodationOffer(Accommodation offer) throws IllegalArgumentException
    {
        if(offer == null)
            throw new IllegalArgumentException("Accommodation offer cannot be empty!");

        this.accommodation = offer;
    }

    public void setTransportOffer(Transport offer) throws IllegalArgumentException
    {
        if(offer == null)
            throw new IllegalArgumentException("Transport offer cannot be empty!");

        this.transport = offer;
    }

    public void setHasRequestOfChanges(boolean value)   { this.hasRequestOfChanges = value; }


    // Getters
    public int getId()                                      { return this.id; }
    public String getBidderUsername()                       { return this.bidderAgencyUsername; }
    public String getRelativeRequirementsOwnerUsername()    { return this.relativeReqOwnerUsername; }
    public String getOfferStatus()                          { return this.offerStatus; }
    public String getDestination()                          { return this.destination; }
    public LocalDate getDepartureDate()                     { return this.duration.getDepartureDate(); }
    public LocalDate getReturnDate()                        { return this.duration.getReturnDate(); }
    public int getPrice()                                   { return this.price; }
    public String getPriceAsStr()                           { return Integer.toString(this.price) + '€'; }
    public Accommodation getAccommodationOffer()            { return this.accommodation; }
    public Transport getTransportOffer()                    { return this.transport; }
    public boolean hasRequestOfChanges()                    { return this.hasRequestOfChanges; }
}
