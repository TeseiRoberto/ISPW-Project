package com.rt.ispwproject.model;

public class ChangesRequestMetadata {

    private final int       requestId;
    private final Profile   requestOwner;
    private final int       relativeOfferId;
    private final Profile   relativeOfferOwner;


    public ChangesRequestMetadata(Profile requestOwner, int relativeOfferId, Profile relativeOfferOwner) throws IllegalArgumentException
    {
        this(0, requestOwner, relativeOfferId, relativeOfferOwner);
    }


    public ChangesRequestMetadata(int requestId, Profile requestOwner, int relativeOfferId, Profile relativeOfferOwner) throws IllegalArgumentException
    {
        if(requestOwner == null)
            throw new IllegalArgumentException("Changes request owner must be specified");

        if(relativeOfferOwner == null)
            throw new IllegalArgumentException("Owner of the offer to which changes are requested must be specified");

        this.requestId = requestId;
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