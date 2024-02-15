package com.rt.ispwproject.model;


public class HolidayOfferMetadata {

    private final int           offerId;
    private final Profile       bidderAgency;                   // Travel agency that made the offer
    private final int           relativeRequirementsId;         // Identifier of the holiday requirements for which this offer is intended
    private final Profile       relativeRequirementsOwner;      // User owner of the holiday requirements for which this holiday offer is intended
    private HolidayOfferState   offerState;


    public HolidayOfferMetadata(int offerId, Profile bidderAgency, HolidayOfferState state, int relativeRequirementsId, Profile relativeRequirementsOwner)
    {
        this.offerId = offerId;
        this.bidderAgency = bidderAgency;
        this.relativeRequirementsId = relativeRequirementsId;
        this.relativeRequirementsOwner = relativeRequirementsOwner;
        this.offerState = state;
    }


    public HolidayOfferMetadata(Profile bidderAgency, HolidayOfferState state, int relativeRequirementsId, Profile relativeRequirementsOwner)
    {
        this.offerId = -1;
        this.bidderAgency = bidderAgency;
        this.relativeRequirementsId = relativeRequirementsId;
        this.relativeRequirementsOwner = relativeRequirementsOwner;
        this.offerState = state;
    }


    // Setters
    public void setOfferState(HolidayOfferState offerState) { this.offerState = offerState; }


    // Getters
    public int getOfferId()                                 { return this.offerId; }
    public Profile getBidderAgency()                        { return this.bidderAgency; }
    public int getRelativeRequirementsId()                  { return this.relativeRequirementsId; }
    public Profile getRelativeRequirementsOwner()           { return this.relativeRequirementsOwner; }
    public HolidayOfferState getOfferState()                { return this.offerState; }

}
