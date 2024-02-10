package com.rt.ispwproject.model;

public enum HolidayOfferState {

    PENDING, REQUESTED_CHANGES, ACCEPTED, REJECTED;


    // Converts the offer state from the system representation to the representation used in the persistence layer
    public String toPersistenceType()
    {
        return switch(this)
        {
            case PENDING ->             "PENDING";
            case REQUESTED_CHANGES ->   "REQUESTED_CHANGES";
            case ACCEPTED ->            "ACCEPTED";
            case REJECTED ->            "REJECTED";
        };
    }


    // Converts the offer state from the representation used in the persistence layer to the system representation
    public static HolidayOfferState fromPersistenceType(String state) throws IllegalArgumentException
    {
        return switch(state)
        {
            case "PENDING" ->           HolidayOfferState.PENDING;
            case "REQUESTED_CHANGES" -> HolidayOfferState.REQUESTED_CHANGES;
            case "ACCEPTED" ->          HolidayOfferState.ACCEPTED;
            case "REJECTED" ->          HolidayOfferState.REJECTED;
            default ->                  throw new IllegalArgumentException("Transport type cannot be converted from persistence to system representation");
        };
    }

}
