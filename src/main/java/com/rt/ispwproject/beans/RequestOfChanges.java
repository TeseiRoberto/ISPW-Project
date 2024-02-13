package com.rt.ispwproject.beans;


public class RequestOfChanges {

    private Offer    originalOffer;
    private Offer    desiredOffer;

    // TODO: Add implementation...

    public RequestOfChanges(Offer originalOffer, Offer desiredOffer) throws IllegalArgumentException
    {
        this.originalOffer = originalOffer;
        this.desiredOffer = desiredOffer;
    }





}
