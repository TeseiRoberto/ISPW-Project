package com.rt.ispwproject.model;

public enum HolidayOfferState {

    PENDING,                // Simple user has not decided yet what to do with the holiday offer
    REQUESTED_CHANGES,      // Simple user has requested changes on the holiday offer to the travel agency
    PENDING_WITH_CHANGES,   // Changes were requested by the user and has been made by the travel agency, now we are waiting for the user evaluation
    ACCEPTED,               // Simple user has accepted the holiday offer made by the travel agency
    REJECTED;               // Simple user has rejected the holiday offer made by the travel agency


    // Converts the offer state from the model representation to the representation used in the persistence layer
    public String toPersistenceType()
    {
        return switch(this)
        {
            case PENDING ->                 "PENDING";
            case REQUESTED_CHANGES ->       "REQUESTED_CHANGES";
            case PENDING_WITH_CHANGES ->    "PENDING_WITH_CHANGES";
            case ACCEPTED ->                "ACCEPTED";
            case REJECTED ->                "REJECTED";
        };
    }


    // Converts the offer state from the representation used in the persistence layer to the model representation
    public static HolidayOfferState fromPersistenceType(String state) throws IllegalArgumentException
    {
        return switch(state)
        {
            case "PENDING" ->               HolidayOfferState.PENDING;
            case "REQUESTED_CHANGES" ->     HolidayOfferState.REQUESTED_CHANGES;
            case "PENDING_WITH_CHANGES" ->  HolidayOfferState.PENDING_WITH_CHANGES;
            case "ACCEPTED" ->              HolidayOfferState.ACCEPTED;
            case "REJECTED" ->              HolidayOfferState.REJECTED;
            default ->                      throw new IllegalArgumentException("Transport type cannot be converted from persistence to system representation");
        };
    }


    // Converts the offer state from the model representation to the view representation
    public String toViewType()
    {
        return switch(this)
        {
            case PENDING ->                 "Offer has not been evaluated yet...";
            case REQUESTED_CHANGES ->       "Changes has been requested on the offer.";
            case PENDING_WITH_CHANGES ->    "Waiting for the user to evaluate the proposed changes";
            case ACCEPTED ->                "Offer has been accepted!";
            case REJECTED ->                "Offer has been rejected!";
        };
    }

}
