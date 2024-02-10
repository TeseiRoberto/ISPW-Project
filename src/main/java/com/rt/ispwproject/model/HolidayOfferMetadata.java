package com.rt.ispwproject.model;


public class HolidayOfferMetadata {

    private int                 offerId;
    private int                 requirementsId;
    private String              bidderAgencyName;
    private int                 bidderAgencyId;
    private HolidayOfferState   offerState;


    public HolidayOfferMetadata(int offerId, int requirementsId, String bidderAgencyName, int bidderAgencyId, HolidayOfferState state)
    {
        this.offerId = offerId;
        this.requirementsId = requirementsId;
        this.bidderAgencyName = bidderAgencyName;
        this.bidderAgencyId = bidderAgencyId;
        this.offerState = state;
    }


    public HolidayOfferMetadata(int requirementsId, String bidderAgencyName, int bidderAgencyId)
    {
        this.offerId = 0;
        this.requirementsId = requirementsId;
        this.bidderAgencyName = bidderAgencyName;
        this.bidderAgencyId = bidderAgencyId;
        this.offerState = HolidayOfferState.PENDING;
    }


    // Setters
    public void setOfferId(int offerId)                         { this.offerId = offerId; }
    public void setRequirementsId(int requirementsId)           { this.requirementsId = requirementsId; }
    public void setBidderAgencyName(String bidderAgencyName)    { this.bidderAgencyName = bidderAgencyName; }
    public void setBidderAgencyId(int id)                       { this.bidderAgencyId = id; }
    public void setOfferState(HolidayOfferState offerState)     { this.offerState = offerState; }


    // Getters
    public int getOfferId()                     { return this.offerId; }
    public int getRequirementsId()              { return this.requirementsId; }
    public String getBidderAgencyName()         { return this.bidderAgencyName; }
    public int getBidderAgencyId()              { return this.bidderAgencyId; }
    public HolidayOfferState getOfferState()    { return this.offerState; }

}
