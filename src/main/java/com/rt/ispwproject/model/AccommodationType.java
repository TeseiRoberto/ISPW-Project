package com.rt.ispwproject.model;

public enum AccommodationType {

    UNSPECIFIED,
    HOTEL;

    // Converts the accommodation type from the model representation to the view representation
    public String toViewType()
    {
        return switch (this)
        {
            case HOTEL ->       "Hotel";
            case UNSPECIFIED -> "Not specified";
        };
    }


    // Converts the accommodation type from the view representation to the model representation
    public static AccommodationType fromViewType(String type) throws IllegalArgumentException
    {
        return switch (type)
        {
            case "Hotel" ->             HOTEL;
            case "Not specified" ->     UNSPECIFIED;
            default ->                  throw new IllegalArgumentException("Accommodation type cannot be converted from view to model representation");
        };
    }


    // Converts the accommodation type from the model representation to the representation used in the persistence layer
    public String toPersistenceType()
    {
        return switch (this)
        {
            case HOTEL ->       "HOTEL";
            case UNSPECIFIED -> "UNSPECIFIED";
        };
    }


    // Converts the given accommodation type from the representation used in the persistence layer to the model representation
    public static AccommodationType fromPersistenceType(String type) throws IllegalArgumentException
    {
        return switch (type)
        {
            case "HOTEL" ->         AccommodationType.HOTEL;
            case "UNSPECIFIED" ->   AccommodationType.UNSPECIFIED;
            default ->              throw new IllegalArgumentException("Accommodation type cannot be converted from persistence to model representation");
        };
    }

}