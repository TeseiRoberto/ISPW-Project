package com.rt.ispwproject.model;

public enum TransportType {

    UNSPECIFIED, AIRPLANE, TRAIN, FERRY, BUS;


    // Converts the transport type from the model representation to the representation used in the persistence layer
    public String toPersistenceType()
    {
        return switch (this)
        {
            case AIRPLANE ->    "AIRPLANE";
            case TRAIN ->       "TRAIN";
            case FERRY ->       "FERRY";
            case BUS ->         "BUS";
            case UNSPECIFIED -> "UNSPECIFIED";
        };
    }


    // Converts the given transport type from the representation used in the persistence layer to the model representation
    public static TransportType fromPersistenceType(String type) throws IllegalArgumentException
    {
        return switch (type)
        {
            case "AIRPLANE" ->      AIRPLANE;
            case "TRAIN" ->         TRAIN;
            case "FERRY" ->         FERRY;
            case "BUS" ->           BUS;
            case "UNSPECIFIED" ->   UNSPECIFIED;
            default ->              throw new IllegalArgumentException("Transport type cannot be converted from persistence to model representation");
        };
    }


    // Converts the transport type from the model representation to the view representation
    public String toViewType()
    {
        return switch (this)
        {
            case AIRPLANE ->        "Airplane";
            case TRAIN ->           "Train";
            case FERRY ->           "Ferry";
            case BUS ->             "Bus";
            case UNSPECIFIED ->     "Not specified";
        };
    }


    // Converts the transport type from the view representation to the model representation
    public static TransportType fromViewType(String type) throws IllegalArgumentException
    {
        return switch (type)
        {
            case "Airplane" ->      AIRPLANE;
            case "Train" ->         TRAIN;
            case "Ferry" ->         FERRY;
            case "Bus" ->           BUS;
            case "Not specified" -> UNSPECIFIED;
            default ->              throw new IllegalArgumentException("Transport type cannot be converted from view to model representation");
        };
    }

}

