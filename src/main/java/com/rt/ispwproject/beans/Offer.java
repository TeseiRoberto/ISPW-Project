package com.rt.ispwproject.beans;


import java.time.LocalDate;

public class Offer {

    private int                 id;
    private String              bidderAgencyUsername;
    private String              relativeAnnouncementOwnerUsername;
    private String              offerStatus;
    private String              destination;
    private Duration            holidayDuration;
    private int                 price;
    private Accommodation       accommodation;
    private Transport           transport;
    private boolean             hasRequestOfChanges;


    public Offer()
    {
        this.id = 0;
        this.bidderAgencyUsername = "";
        this.relativeAnnouncementOwnerUsername = "";
        this.offerStatus = "";
        this.destination = "";
        this.holidayDuration = null;
        this.price = 0;
        this.accommodation = new Accommodation();
        this.transport = new Transport();
        this.hasRequestOfChanges = false;
    }


    public Offer(String bidderAgencyUsername, String relativeReqOwnerUsername, String destination,
                 Duration holidayDuration, int price, Accommodation accommodation, Transport transport) throws IllegalArgumentException
    {
        this.id = 0;
        setBidderUsername(bidderAgencyUsername);
        setRelativeAnnouncementOwnerUsername(relativeReqOwnerUsername);
        setDestination(destination);
        setHolidayDuration(holidayDuration);
        setPrice(price);
        setAccommodationOffer(accommodation);
        setTransportOffer(transport);
        this.offerStatus = "";
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

    public void setRelativeAnnouncementOwnerUsername(String username) throws IllegalArgumentException
    {
        if(username == null || username.isEmpty())
            throw new IllegalArgumentException("relative requirements owner username cannot be empty!");

        this.relativeAnnouncementOwnerUsername = username;
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

    public void setHolidayDuration(Duration holidayDuration) throws IllegalArgumentException
    {
        if(holidayDuration == null)
            throw new IllegalArgumentException("Duration cannot be empty!");

        if(!holidayDuration.getDepartureDate().isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Offer departure date cannot be before (or equal to) the current date!");

        this.holidayDuration = holidayDuration;
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
    public String getRelativeAnnouncementOwnerUsername()    { return this.relativeAnnouncementOwnerUsername; }
    public String getOfferStatus()                          { return this.offerStatus; }
    public String getDestination()                          { return this.destination; }
    public Duration getHolidayDuration()                    { return this.holidayDuration; }
    public int getPrice()                                   { return this.price; }
    public String getPriceAsStr()                           { return Integer.toString(this.price) + '€'; }
    public Accommodation getAccommodationOffer()            { return this.accommodation; }
    public Transport getTransportOffer()                    { return this.transport; }
    public boolean hasRequestOfChanges()                    { return this.hasRequestOfChanges; }
}
