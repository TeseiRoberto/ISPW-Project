package com.rt.ispwproject.model;

import com.rt.ispwproject.beans.Accommodation;
import com.rt.ispwproject.beans.ChangesRequest;
import com.rt.ispwproject.beans.Duration;
import com.rt.ispwproject.beans.Transport;

public class HolidayOfferChanges {

    private final HolidayOfferChangesMetadata   metadata;
    private String                              changesDescription = "";
    private Location                            destination = null;
    private DateRange                           duration = null;
    private AccommodationRequirements           accommodationChanges = null;
    private TransportRequirements               transportChanges = null;


    public HolidayOfferChanges(HolidayOfferChangesMetadata metadata)
    {
        this.metadata = metadata;
    }


    // Setters
    public void setChangesDescription(String description)                   { this.changesDescription = description == null ? "No description was given" : description; }
    public void setDestination(Location destination)                        { this.destination = destination; }
    public void setDuration(DateRange duration)                             { this.duration = duration; }
    public void setAccommodationChanges(AccommodationRequirements changes)  { this.accommodationChanges = changes; }
    public void setTransportChanges(TransportRequirements changes)          { this.transportChanges = changes; }


    // Getters
    public HolidayOfferChangesMetadata getMetadata()            { return metadata; }
    public String getChangesDescription()                       { return changesDescription; }
    public Location getDestination()                            { return destination; }
    public DateRange getDuration()                              { return duration; }
    public AccommodationRequirements getAccommodationChanges()  { return accommodationChanges; }
    public TransportRequirements getTransportChanges()          { return transportChanges; }


    // Converts a HolidayOfferChanges instance into a RequestOfChanges instance (model to bean class conversion)
    public ChangesRequest toRequestOfChangesBean() throws IllegalArgumentException
    {
        ChangesRequest newRequest = new ChangesRequest(
                metadata.getId(),
                metadata.getOwner().getUsername(),
                metadata.getRelativeOfferId(),
                metadata.getBidder().getUsername()
        );

        newRequest.setChangesDescription(changesDescription);

        if(destination != null)
            newRequest.setDestination(destination.getAddress());

        if(duration != null)
            newRequest.setDuration( new Duration(duration.getStartDate(), duration.getEndDate()) );

        if(accommodationChanges != null)
        {
            Accommodation changes = new Accommodation(
                    accommodationChanges.getType().toViewType(),
                    accommodationChanges.getQuality(),
                    accommodationChanges.getNumOfRooms()
            );

            newRequest.setAccommodationChanges(changes);
        }

        if(transportChanges != null)
        {
            Transport changes = new Transport(
                    transportChanges.getType().toViewType(),
                    transportChanges.getQuality(),
                    transportChanges.getDepartureLocation().getAddress(),
                    transportChanges.getNumOfTravelers()
            );

            newRequest.setTransportChanges(changes);
        }

        return newRequest;
    }


}