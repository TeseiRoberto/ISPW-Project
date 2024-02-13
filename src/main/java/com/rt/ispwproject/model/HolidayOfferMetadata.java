package com.rt.ispwproject.model;


public class HolidayOfferMetadata {

    private int                 offerId;
    private int                 bidderAgencyId;
    private String              bidderAgencyUsername;
    private int                 relativeRequirementsId;         // Id associated to the holiday requirements for which this offer is intended
    private int                 relativeReqOwnerId;             // Id associated to the user that has posted the holiday requirements for which this offer is intended
    private String              relativeReqOwnerUsername;       // Username of the user that has posted the holiday requirements for which this offer is intended
    private HolidayOfferState   offerState;


    public HolidayOfferMetadata(int offerId, int bidderAgencyId, String bidderAgencyUsername, HolidayOfferState state,
                                int relativeRequirementsId, int relativeRequirementsOwnerId, String relativeRequirementsOwnerUsername)
    {
        this.offerId = offerId;
        this.bidderAgencyId = bidderAgencyId;
        this.bidderAgencyUsername = bidderAgencyUsername;
        this.relativeRequirementsId = relativeRequirementsId;
        this.relativeReqOwnerId = relativeRequirementsOwnerId;
        this.relativeReqOwnerUsername = relativeRequirementsOwnerUsername;
        this.offerState = state;
    }


    public HolidayOfferMetadata(int bidderAgencyId, String bidderAgencyUsername, HolidayOfferState state,
                                int relativeRequirementsId, int relativeRequirementsOwnerId, String relativeRequirementsOwnerUsername)
    {
        this.offerId = 0;
        this.bidderAgencyUsername = bidderAgencyUsername;
        this.bidderAgencyId = bidderAgencyId;
        this.relativeRequirementsId = relativeRequirementsId;
        this.relativeReqOwnerId = relativeRequirementsOwnerId;
        this.relativeReqOwnerUsername = relativeRequirementsOwnerUsername;
        this.offerState = state;
    }


    // Setters
    public void setOfferId(int offerId)                                     { this.offerId = offerId; }
    public void setBidderAgencyId(int id)                                   { this.bidderAgencyId = id; }
    public void setBidderAgencyUsername(String username)                    { this.bidderAgencyUsername = username; }
    public void setRelativeRequirementsId(int id)                           { this.relativeRequirementsId = id;}
    public void setRelativeRequirementOwnerId(int id)                       { this.relativeReqOwnerId = id;}
    public void setRelativeRequirementsOwnerUsername(String username)       { this.relativeReqOwnerUsername = username;}
    public void setOfferState(HolidayOfferState offerState)                 { this.offerState = offerState; }


    // Getters
    public int getOfferId()                                 { return this.offerId; }
    public int getBidderAgencyId()                          { return this.bidderAgencyId; }
    public String getBidderAgencyUsername()                 { return this.bidderAgencyUsername; }
    public int getRelativeRequirementsId()                  { return this.relativeRequirementsId; }
    public int getRelativeRequirementsOwnerId()             { return this.relativeReqOwnerId; }
    public String getRelativeRequirementsOwnerUsername()    { return this.relativeReqOwnerUsername; }
    public HolidayOfferState getOfferState()                { return this.offerState; }

}
