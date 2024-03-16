package com.rt.ispwproject.model;

public class ChangesRequestMetadata {

    private final int       requestId;
    private final Profile   requestOwner;
    private final int       relativeOfferId;
    private final Profile   relativeOfferOwner;


    public ChangesRequestMetadata(int requestId, Profile requestOwner, int relativeOfferId, Profile relativeOfferOwner)
    {
        this.requestId = requestId;
        this.requestOwner = requestOwner;
        this.relativeOfferId = relativeOfferId;
        this.relativeOfferOwner = relativeOfferOwner;
    }


    public ChangesRequestMetadata(Profile requestOwner, int relativeOfferId, Profile relativeOfferOwner)
    {
        this.requestId = 0;
        this.requestOwner = requestOwner;
        this.relativeOfferId = relativeOfferId;
        this.relativeOfferOwner = relativeOfferOwner;
    }


    // Getters
    public int getRequestId()               { return this.requestId; }
    public Profile getRequestOwner()        { return this.requestOwner; }
    public int getRelativeOfferId()         { return this.relativeOfferId; }
    public Profile getRelativeOfferOwner()  { return this.relativeOfferOwner; }

}