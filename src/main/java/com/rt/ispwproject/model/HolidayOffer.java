package com.rt.ispwproject.model;

import com.rt.ispwproject.beans.*;

import java.time.LocalDate;

public class HolidayOffer {

    private final HolidayMetadata   metadata;
    private Location                destination;
    protected int                   price;
    private final DateRange         duration;
    private AccommodationOffer      accommodationOffer;
    private TransportOffer          transportOffer;


    public HolidayOffer(HolidayMetadata metadata, Location destination, DateRange duration, int price, AccommodationOffer accommodationOffer, TransportOffer transportOffer)
    {
        this.metadata = metadata;
        this.destination = destination;
        this.price = price;
        this.duration = duration;
        this.accommodationOffer = accommodationOffer;
        this.transportOffer = transportOffer;
    }


    // Setters
    public void setDestination(Location destination)                { this.destination = destination; }
    public void setPrice(int price)                                 { this.price = price; }
    public void setDepartureDate(LocalDate date)                    { this.duration.setStartDate(date); }
    public void setReturnDate(LocalDate date)                       { this.duration.setEndDate(date); }
    public void setAccommodation(AccommodationOffer accommodation)  { this.accommodationOffer = accommodation; }
    public void setTransport(TransportOffer transportOffer)         { this.transportOffer = transportOffer; }


    // Getters
    public HolidayMetadata getMetadata()                        { return this.metadata; }
    public Location getDestination()                            { return this.destination; }
    public int getPrice()                                       { return this.price; }
    public LocalDate getDepartureDate()                         { return this.duration.getStartDate(); }
    public LocalDate getReturnDate()                            { return this.duration.getEndDate(); }
    public AccommodationOffer getAccommodation()                { return this.accommodationOffer; }
    public TransportOffer getTransport()                        { return this.transportOffer; }


    // Converts an HolidayOffer instance into an Offer instance (model to bean class conversion)
    // Note: this creates coupling between the 2 classes but alleviates the burden of conversion from the developer and eliminates code duplication
    public Offer toOffer() throws IllegalArgumentException
    {
        Accommodation accommodation = new Accommodation(
                accommodationOffer.getType(),
                accommodationOffer.getName(),
                accommodationOffer.getLocation().getAddress(),
                accommodationOffer.getQuality(),
                accommodationOffer.getNumOfRooms(),
                accommodationOffer.getPricePerNight(),
                accommodationOffer.getPrice()
        );

        Transport transport = new Transport(
                transportOffer.getType(),
                transportOffer.getCompany(),
                transportOffer.getQuality(),
                transportOffer.getDepartureLocation().getAddress(),
                transportOffer.getNumOfTravelers(),
                transportOffer.getPricePerTraveler()
        );

        return new Offer(
                metadata.getHolidayId(),
                metadata.getOwnerUsername(),
                destination.getAddress(),
                new Duration(duration.getStartDate(), duration.getEndDate()),
                price,
                accommodation,
                transport
        );
    }

}
