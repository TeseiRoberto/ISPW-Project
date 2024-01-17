package com.rt.ispwproject.beans;


public class Offer {

    private int                 id;
    private String              bidderAgencyName;
    private int                 price;
    private String              destination;
    private Duration            duration;
    private AccommodationOffer  accommodation;
    private TransportOffer      transport;


    public Offer(int id, String agencyName, int price, String destination, Duration duration, AccommodationOffer accommodationOffer, TransportOffer transportOffer) throws IllegalArgumentException
    {
        setId(id);
        setBidder(agencyName);
        setPrice(price);
        setDestination(destination);
        setDuration(duration);
        this.duration = duration;
        setTransportOffer(transportOffer);
    }


    // Setters
    public void setId(int id)   { this.id = id; }

    public void setBidder(String agencyName) throws IllegalArgumentException
    {
        if(agencyName == null || agencyName.isEmpty())
            throw new IllegalArgumentException("Bidder name cannot be empty!");

        this.bidderAgencyName = agencyName;
    }

    public void setPrice(int price) throws IllegalArgumentException
    {
        if(price <= 0)
            throw new IllegalArgumentException("Price cannot be negative or zero!");

        this.price = price;
    }

    public void setDestination(String destination) throws IllegalArgumentException
    {
        if(destination == null || destination.isEmpty())
            throw new IllegalArgumentException("Destination cannot be empty!");

        this.destination = destination;
    }

    public void setDuration(Duration duration)
    {
        if(duration == null)
            throw new IllegalArgumentException("Duration cannot be empty!");

        this.duration = duration;
    }

    public void setAccommodationOffer(AccommodationOffer offer) throws IllegalArgumentException
    {
        if(offer == null)
            throw new IllegalArgumentException("Accommodation offer cannot be empty!");

        this.accommodation = offer;
    }

    public void setTransportOffer(TransportOffer offer) throws IllegalArgumentException
    {
        if(transport == null)
            throw new IllegalArgumentException("Transport offer cannot be empty!");

        this.transport = offer;
    }


    // Getters
    public int getId()                                  { return this.id; }
    public String getBidder()                           { return this.bidderAgencyName; }
    public int getPrice()                               { return this.price; }
    public String getDestination()                      { return this.destination; }
    public Duration getDuration()                       { return this.duration; }
    public AccommodationOffer getAccommodationOffer()   { return this.accommodation; }
    public TransportOffer getTransportOffer()           { return this.transport; }

}
