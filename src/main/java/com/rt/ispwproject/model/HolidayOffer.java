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
        setMetadata(metadata);
        setDestination(destination);
        setHolidayDuration(duration);
        setPrice(price);
        setAccommodationOffer(accommodationOffer);
        setTransportOffer(transportOffer);
    }


    // Setters
    public void setMetadata(HolidayOfferMetadata metadata) throws IllegalArgumentException
    {
        if(metadata == null)
            throw new IllegalArgumentException("Metadata must be specified for the holiday offer");

        this.metadata = metadata;
    }

    public void setDestination(Location destination) throws IllegalArgumentException
    {
        this.destination = destination;
    }

    public void setPrice(int price) throws IllegalArgumentException
    {
        if(price <= 0)
            throw new IllegalArgumentException("The price of the holiday offer must be equal to (or greater than) 1");

        this.price = price;
    }

    public void setHolidayDuration(DateRange holidayDuration) throws IllegalArgumentException
    {
        if(holidayDuration == null)
            throw new IllegalArgumentException("A duration of the holiday must be specified");

        this.holidayDuration = holidayDuration;
    }

    public void setAccommodationOffer(AccommodationOffer offer) throws IllegalArgumentException
    {
        if(offer == null)
            throw new IllegalArgumentException("An accommodation offer must be specified for the holiday offer");

        this.accommodationOffer = offer;
    }

    public void setTransportOffer(TransportOffer offer) throws IllegalArgumentException
    {
        if(offer == null)
            throw new IllegalArgumentException("A transport offer must be specified for the holiday offer");

        this.transportOffer = offer;
    }


    // Getters
    public HolidayOfferMetadata getMetadata()                   { return this.metadata; }
    public Location getDestination()                            { return this.destination; }
    public DateRange getHolidayDuration()                       { return this.holidayDuration; }
    public int getPrice()                                       { return this.price; }
    public AccommodationOffer getAccommodationOffer()           { return this.accommodationOffer; }
    public TransportOffer getTransportOffer()                   { return this.transportOffer; }


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
