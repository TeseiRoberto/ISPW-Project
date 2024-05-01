package com.rt.ispwproject.model;

import com.rt.ispwproject.beans.ChangesOnOffer;
import com.rt.ispwproject.beans.Duration;

// In the system we use the adapter patter to represent a request of changes made by a user on an offer made by a travel agency.
// In particular, we can represent a request of changes using a couple of holiday offers instances, the first is the
// one made by the travel agency and the second one is the offer that the user would like to receive.
// Comparing the fields of those offers we can obtain the requested changes
public class HolidayOfferAdapter implements ChangesRequest {


    private final HolidayOffer  agencyOffer;
    private final HolidayOffer  userDesiredOffer;
    private final String        changesDescription;


    public HolidayOfferAdapter(HolidayOffer agencyOffer, HolidayOffer userDesiredOffer, String changesDescription)
    {
        this.agencyOffer = agencyOffer;
        this.userDesiredOffer = userDesiredOffer;
        this.changesDescription = changesDescription;
    }


    public boolean isDestinationChangeRequired()        { return !agencyOffer.getDestination().isEqual(userDesiredOffer.getDestination()); }
    public boolean isHolidayDurationChangeRequired()    { return userDesiredOffer.getHolidayDuration() != null && !agencyOffer.getHolidayDuration().isEqual(userDesiredOffer.getHolidayDuration()); }
    public boolean isPriceChangeRequired()              { return agencyOffer.getPrice() != userDesiredOffer.getPrice(); }
    public boolean isAccommodationChangeRequired()      { return userDesiredOffer.getAccommodationOffer() != null && agencyOffer.getAccommodationOffer() != userDesiredOffer.getAccommodationOffer(); }
    public boolean isTransportChangeRequired()          { return userDesiredOffer.getTransportOffer() != null && agencyOffer.getTransportOffer() != userDesiredOffer.getTransportOffer(); }


    // Getters
    public int getRequestId()                               { return userDesiredOffer.getMetadata().getOfferId(); }
    public int getRelativeOfferId()                         { return agencyOffer.getMetadata().getOfferId(); }
    public Profile getRequestOwner()                        { return userDesiredOffer.getMetadata().getOfferOwner(); }
    public Profile getRelativeOfferOwner()                  { return agencyOffer.getMetadata().getOfferOwner(); }
    public String getChangesDescription()                   { return changesDescription; }
    public Location getDestinationChange()                  { return userDesiredOffer.getDestination(); }
    public DateRange getHolidayDurationChange()             { return userDesiredOffer.getHolidayDuration(); }
    public int getPriceChange()                             { return userDesiredOffer.getPrice(); }
    public AccommodationChanges getAccommodationChanges()   { return userDesiredOffer.getAccommodationOffer(); }
    public TransportChanges getTransportChanges()           { return userDesiredOffer.getTransportOffer(); }


    // Converts this instance into an OfferChanges instance (model to bean class conversion)
    public ChangesOnOffer toChangesOnOfferBean() throws IllegalArgumentException
    {
        ChangesOnOffer c = new ChangesOnOffer(
                userDesiredOffer.getMetadata().getOfferId(),
                userDesiredOffer.getMetadata().getOfferOwner().getUsername(),
                agencyOffer.getMetadata().getOfferId(),
                agencyOffer.getMetadata().getOfferOwner().getUsername()
        );

        c.setChangesDescription(changesDescription);

        if(isHolidayDurationChangeRequired())
        {
            c.setHolidayDuration(
                    new Duration(
                            userDesiredOffer.getHolidayDuration().getStartDate(),
                            userDesiredOffer.getHolidayDuration().getEndDate()
                    )
            );
        }

        if(isPriceChangeRequired())
            c.setPrice(userDesiredOffer.getPrice());

        if(isAccommodationChangeRequired())
            c.setAccommodationChanges(getAccommodationChanges().toAccommodationBean());

        if(isTransportChangeRequired())
            c.setTransportChanges(getTransportChanges().toTransportBean());

        return c;
    }
}
