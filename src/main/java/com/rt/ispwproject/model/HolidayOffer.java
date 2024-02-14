package com.rt.ispwproject.model;

import com.rt.ispwproject.beans.*;

import java.time.LocalDate;

public class HolidayOffer {

    private final HolidayOfferMetadata  metadata;
    private Location                    destination;
    private int                         price;
    private final DateRange             duration;
    private final AccommodationOffer    accommodationOffer;
    private final TransportOffer        transportOffer;


    public HolidayOffer(HolidayOfferMetadata metadata, Location destination, DateRange duration, int price,
                        AccommodationOffer accommodationOffer, TransportOffer transportOffer)
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


    // Getters
    public HolidayOfferMetadata getMetadata()                   { return this.metadata; }
    public Location getDestination()                            { return this.destination; }
    public int getPrice()                                       { return this.price; }
    public LocalDate getDepartureDate()                         { return this.duration.getStartDate(); }
    public LocalDate getReturnDate()                            { return this.duration.getEndDate(); }
    public AccommodationOffer getAccommodation()                { return this.accommodationOffer; }
    public TransportOffer getTransport()                        { return this.transportOffer; }


    // Converts an HolidayOffer instance into an Offer instance (model to bean class conversion)
    // Note: this creates coupling between the 2 classes but alleviates the burden of conversion from the developer and eliminates code duplication
    public Offer toOfferBean() throws IllegalArgumentException
    {
        Accommodation accommodation = accommodationOffer.toAccommodationBean();
        Transport transport = transportOffer.toTransportBean();

        Offer newOffer =  new Offer(
                metadata.getBidderAgency().getUsername(),
                metadata.getRelativeRequirementsOwner().getUsername(),
                destination.getAddress(),
                new Duration(duration.getStartDate(), duration.getEndDate()),
                price,
                accommodation,
                transport
        );

        newOffer.setId(metadata.getOfferId());
        newOffer.setOfferStatus(metadata.getOfferState().toViewType());

        if(metadata.getOfferState() == HolidayOfferState.REQUESTED_CHANGES)
            newOffer.setHasRequestOfChanges(true);

        return newOffer;
    }

}
