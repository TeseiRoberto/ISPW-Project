package com.rt.ispwproject.beans;


import java.time.LocalDate;

public class Offer {

    private int                 id;
    private String              bidderAgencyName;
    private String              offerStatus;
    private String              destination;
    private Duration            duration;
    private int                 price;
    private Accommodation       accommodation;
    private Transport           transport;


    public Offer(String bidderAgencyName, String destination, Duration duration, int price, Accommodation accommodation, Transport transport) throws IllegalArgumentException
    {
        this.offerStatus = "unknown";
        setBidder(bidderAgencyName);
        setDestination(destination);
        setDuration(duration);
        setPrice(price);
        setAccommodationOffer(accommodation);
        setTransportOffer(transport);
    }


    // Setters
    public void setId(int id)   { this.id = id; }

    public void setBidder(String agencyName) throws IllegalArgumentException
    {
        if(agencyName == null || agencyName.isEmpty())
            throw new IllegalArgumentException("Bidder name cannot be empty!");

        this.bidderAgencyName = agencyName;
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


    // Getters
    public int getId()                                  { return this.id; }
    public String getBidder()                           { return this.bidderAgencyName; }
    public String getOfferStatus()                      { return this.offerStatus; }
    public String getDestination()                      { return this.destination; }
    public LocalDate getDepartureDate()                 { return this.duration.getDepartureDate(); }
    public LocalDate getReturnDate()                    { return this.duration.getReturnDate(); }
    public int getPrice()                               { return this.price; }
    public String getPriceAsStr()                       { return Integer.toString(this.price) + '€'; }
    public Accommodation getAccommodationOffer()        { return this.accommodation; }
    public Transport getTransportOffer()                { return this.transport; }

}
