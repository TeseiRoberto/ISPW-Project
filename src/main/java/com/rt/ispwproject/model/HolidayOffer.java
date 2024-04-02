package com.rt.ispwproject.model;

import com.rt.ispwproject.beans.*;

public class HolidayOffer {

    private HolidayOfferMetadata        metadata;
    private Location                    destination;
    private int                         price;
    private DateRange                   holidayDuration;
    private AccommodationOffer          accommodationOffer;
    private TransportOffer              transportOffer;


    public HolidayOffer(HolidayOfferMetadata metadata, Location destination, DateRange duration, int price,
                        AccommodationOffer accommodationOffer, TransportOffer transportOffer)
    {
        this.metadata = metadata;
        this.destination = destination;
        this.price = price;
        this.holidayDuration = duration;
        this.accommodationOffer = accommodationOffer;
        this.transportOffer = transportOffer;
    }


    // Setters
    public void setMetadata(HolidayOfferMetadata metadata)          { this.metadata = metadata; }
    public void setDestination(Location destination)                { this.destination = destination; }
    public void setHolidayDuration(DateRange duration)              { this.holidayDuration = duration; }
    public void setAccommodationOffer(AccommodationOffer offer)     { this.accommodationOffer = offer; }
    public void setTransportOffer(TransportOffer offer)             { this.transportOffer = offer; }
    public void setPrice(int price)                                 { this.price = price; }


    // Getters
    public HolidayOfferMetadata getMetadata()                   { return this.metadata; }
    public Location getDestination()                            { return this.destination; }
    public DateRange getHolidayDuration()                       { return this.holidayDuration; }
    public AccommodationOffer getAccommodationOffer()           { return this.accommodationOffer; }
    public TransportOffer getTransportOffer()                   { return this.transportOffer; }
    public int getPrice()                                       { return this.price; }


    // Converts an HolidayOffer instance into an Offer instance (model to bean class conversion)
    // Note: this creates coupling between the 2 classes but alleviates the burden of conversion from the developer and eliminates code duplication
    public Offer toOfferBean() throws IllegalArgumentException
    {

        Offer newOffer =  new Offer(
                this.metadata.getOfferOwner().getUsername(),
                this.metadata.getRelativeRequirementsOwner().getUsername(),
                this.destination.getAddress(),
                new Duration(this.holidayDuration.getStartDate(), this.holidayDuration.getEndDate()),
                price,
                this.accommodationOffer.toAccommodationBean(),
                this.transportOffer.toTransportBean()
        );

        newOffer.setId(this.metadata.getOfferId());
        newOffer.setOfferStatus(this.metadata.getOfferState().toViewType());

        if(this.metadata.getOfferState() == HolidayOfferState.REQUESTED_CHANGES)
            newOffer.setHasRequestOfChanges(true);

        return newOffer;
    }

}
