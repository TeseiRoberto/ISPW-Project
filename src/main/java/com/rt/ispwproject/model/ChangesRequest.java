package com.rt.ispwproject.model;

import com.rt.ispwproject.beans.Accommodation;
import com.rt.ispwproject.beans.ChangesOnOffer;
import com.rt.ispwproject.beans.Duration;
import com.rt.ispwproject.beans.Transport;

public class ChangesRequest {

    private final ChangesRequestMetadata    metadata;
    private final String                    changesDescription;
    private DateRange                       requiredHolidayDuration = null;
    private int                             requiredPrice = 0;
    private AccommodationChangesRequest     accommodationChanges = null;
    private TransportChangesRequest         transportChanges = null;


    public ChangesRequest(ChangesRequestMetadata metadata, String changesDescription)
    {
        this.metadata = metadata;
        this.changesDescription = changesDescription;
    }


    public boolean isHolidayDurationChangeRequired()    { return this.requiredHolidayDuration != null; }
    public boolean isAccommodationChangeRequired()      { return this.accommodationChanges != null; }
    public boolean isTransportChangeRequired()          { return this.transportChanges != null; }
    public boolean isPriceChangeRequired()              { return this.requiredPrice > 0; }


    // Setters
    public void setHolidayDuration(DateRange duration)                          { this.requiredHolidayDuration = duration; }
    public void setAccommodationChanges(AccommodationChangesRequest changes)    { this.accommodationChanges = changes; }
    public void setTransportChanges(TransportChangesRequest changes)            { this.transportChanges = changes; }
    public void setPrice(int price)                                             { this.requiredPrice = price; }


    // Getters
    public ChangesRequestMetadata getMetadata()                     { return this.metadata; }
    public String getChangesDescription()                           { return this.changesDescription; }
    public DateRange getHolidayDuration()                           { return this.requiredHolidayDuration; }
    public AccommodationChangesRequest getAccommodationChanges()    { return this.accommodationChanges; }
    public TransportChangesRequest getTransportChanges()            { return this.transportChanges; }
    public int getPrice()                                           { return this.requiredPrice; }


    // Converts this instance into an OfferChanges instance (model to bean class conversion)
    public ChangesOnOffer toChangesOnOfferBean() throws IllegalArgumentException
    {
        ChangesOnOffer beanRequest = new ChangesOnOffer(
                this.metadata.getRequestId(),
                this.metadata.getRequestOwner().getUsername(),
                this.metadata.getRelativeOfferId(),
                this.metadata.getRelativeOfferOwner().getUsername()
        );

        // Insert the required changes in the bean class
        beanRequest.setChangesDescription(changesDescription);

        if(isHolidayDurationChangeRequired())
        {
            beanRequest.setHolidayDuration(
                    new Duration(
                            this.requiredHolidayDuration.getStartDate(),
                            this.requiredHolidayDuration.getEndDate()
                    )
            );
        }

        if(isPriceChangeRequired())
            beanRequest.setPrice(this.requiredPrice);

        if(isAccommodationChangeRequired())
        {
            beanRequest.setAccommodationChanges(
                    new Accommodation(
                            this.accommodationChanges.getType().toViewType(),
                            this.accommodationChanges.getQuality(),
                            this.accommodationChanges.getNumOfRooms()
                    )
            );
        }

        if(isTransportChangeRequired())
        {
            beanRequest.setTransportChanges(
                    new Transport(
                        transportChanges.getType().toViewType(),
                        transportChanges.getQuality(),
                        transportChanges.getRoute().getDepartureLocation().getAddress(),
                        transportChanges.getRoute().getArrivalLocation().getAddress(),
                        transportChanges.getNumOfTravelers()
                    )
            );
        }

        return beanRequest;
    }

}