package com.rt.ispwproject.beans;


import java.time.LocalDate;

public class Offer {

    private int                 id;
    private String              bidderAgencyName;
    private String              destination;
    private Duration            duration;
    private int                 price;
    private AccommodationOffer  accommodation;
    private TransportOffer      transport;


    public Offer(int id, String agencyName, String destination, Duration duration, int price, AccommodationOffer accommodationOffer, TransportOffer transportOffer) throws IllegalArgumentException
    {
        setId(id);
        setBidder(agencyName);
        setDestination(destination);
        setDuration(duration);
        setPrice(price);
        setAccommodationOffer(accommodationOffer);
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

        if(!duration.getStartDate().isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Offer departure date cannot be before (or equal to) the current date!");

        this.duration = duration;
    }

    public void setPrice(int price) throws IllegalArgumentException
    {
        if(price <= 0)
            throw new IllegalArgumentException("Offer price cannot be negative or zero!");

        this.price = price;
    }

    public void setAccommodationOffer(AccommodationOffer offer) throws IllegalArgumentException
    {
        if(offer == null)
            throw new IllegalArgumentException("Accommodation offer cannot be empty!");

        this.accommodation = offer;
    }

    public void setTransportOffer(TransportOffer offer) throws IllegalArgumentException
    {
        if(offer == null)
            throw new IllegalArgumentException("Transport offer cannot be empty!");

        this.transport = offer;
    }


    // Getters
    public int getId()                                  { return this.id; }
    public String getBidder()                           { return this.bidderAgencyName; }
    public String getDestination()                      { return this.destination; }
    public LocalDate getDepartureDate()                 { return this.duration.getStartDate(); }
    public LocalDate getReturnDate()                    { return this.duration.getEndDate(); }
    public int getPrice()                               { return this.price; }
    public String getPriceAsStr()                       { return Integer.toString(this.price) + '€'; }
    public AccommodationOffer getAccommodationOffer()   { return this.accommodation; }
    public TransportOffer getTransportOffer()           { return this.transport; }

}
