package com.rt.ispwproject.model;

public class HolidayOfferChangesMetadata {

    private final int       id;                     // Identifier of the changes requested
    private final Profile   owner;                  // User that requests the changes
    private final int       relativeOfferId;        // Identifier associated to the holiday offer for which changes are required
    private final Profile   bidder;                 // User that has made the offer


    public HolidayOfferChangesMetadata(int changesRequestId, Profile requestOwner, int relativeOfferId, Profile offerOwner)
    {
        this.id = changesRequestId;
        this.owner = requestOwner;
        this.relativeOfferId = relativeOfferId;
        this.bidder = offerOwner;
    }


    public HolidayOfferChangesMetadata(Profile requestOwner, int relativeOfferId, Profile offerOwner)
    {
        this.id = -1;
        this.owner = requestOwner;
        this.relativeOfferId = relativeOfferId;
        this.bidder = offerOwner;
    }


    // Getters
    public int getId()                      { return id; }
    public Profile getOwner()               { return owner; }
    public int getRelativeOfferId()         { return relativeOfferId; }
    public Profile getBidder()              { return bidder; }
}
