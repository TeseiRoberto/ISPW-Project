package com.rt.ispwproject.model;

public enum AccommodationType {

    UNSPECIFIED, HOTEL;

    // Converts the accommodation type from the system representation to the representation used in the persistence layer
    public String toPersistenceType()
    {
        return switch (this)
        {
            case HOTEL ->       "HOTEL";
            case UNSPECIFIED -> "UNSPECIFIED";
        };
    }


    // Converts the given accommodation type from the representation used in the persistence layer to the system representation
    public static AccommodationType fromPersistenceType(String type) throws IllegalArgumentException
    {
        return switch (type)
        {
            case "HOTEL" ->       AccommodationType.HOTEL;
            case "UNSPECIFIED" -> AccommodationType.UNSPECIFIED;
            default ->              throw new IllegalArgumentException("Transport type cannot be converted from persistence to system representation");
        };
    }


}