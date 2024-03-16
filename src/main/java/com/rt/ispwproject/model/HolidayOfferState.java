package com.rt.ispwproject.model;

public enum HolidayOfferState {

    PENDING,                        // The offer has not been evaluated yet
    REQUESTED_CHANGES,              // Changes has been requested on the offer
    PENDING_WITH_CHANGES,           // The offer has been modified but the changes made have not been evaluated yet
    PENDING_WITH_REJECTED_CHANGES,  // The changes requested on the offer has been rejected but the original offer is still valid
    ACCEPTED,                       // The offer has been accepted
    REJECTED;                       // The offer has been rejected


    // Converts the offer state from the model representation to the view representation
    public String toViewType()
    {
        return switch(this)
        {
            case PENDING ->                         "Offer has not been evaluated yet...";
            case REQUESTED_CHANGES ->               "Changes has been requested on the offer.";
            case PENDING_WITH_CHANGES ->            "Offer has been modified, waiting for evaluation...";
            case PENDING_WITH_REJECTED_CHANGES ->   "Requested changes has been rejected, but the offer is still valid.";
            case ACCEPTED ->                        "Offer has been accepted!";
            case REJECTED ->                        "Offer has been rejected!";
        };
    }


    // Converts the offer state from the model representation to the representation used in the persistence layer
    public String toPersistenceType()
    {
        return switch(this)
        {
            case PENDING ->                         "PENDING";
            case REQUESTED_CHANGES ->               "REQUESTED_CHANGES";
            case PENDING_WITH_CHANGES ->            "PENDING_WITH_CHANGES";
            case PENDING_WITH_REJECTED_CHANGES ->   "PENDING_WITH_REJECTED_CHANGES";
            case ACCEPTED ->                        "ACCEPTED";
            case REJECTED ->                        "REJECTED";
        };
    }


    // Converts the offer state from the representation used in the persistence layer to the model representation
    public static HolidayOfferState fromPersistenceType(String state) throws IllegalArgumentException
    {
        return switch(state)
        {
            case "PENDING" ->                       PENDING;
            case "REQUESTED_CHANGES" ->             REQUESTED_CHANGES;
            case "PENDING_WITH_CHANGES" ->          PENDING_WITH_CHANGES;
            case "PENDING_WITH_REJECTED_CHANGES" -> PENDING_WITH_REJECTED_CHANGES;
            case "ACCEPTED" ->                      ACCEPTED;
            case "REJECTED" ->                      REJECTED;
            default ->                              throw new IllegalArgumentException("Holiday offer state cannot be converted from persistence to system representation");
        };
    }

}
