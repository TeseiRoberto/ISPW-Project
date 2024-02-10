package com.rt.ispwproject.model;

public enum TransportType {

    UNSPECIFIED, AIRPLANE, TRAIN, FERRY, BUS;


    // Converts the transport type to the representation used in the persistence layer
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


    // Converts the given transport type from the representation used in the persistence layer to the system representation
    public static TransportType fromPersistenceType(String type) throws IllegalArgumentException
    {
        return switch (type)
        {
            case "AIRPLANE" ->      TransportType.AIRPLANE;
            case "TRAIN" ->         TransportType.TRAIN;
            case "FERRY" ->         TransportType.FERRY;
            case "BUS" ->           TransportType.BUS;
            case "UNSPECIFIED" ->   TransportType.UNSPECIFIED;
            default ->              throw new IllegalArgumentException("Transport type cannot be converted from persistence to system representation");
        };
    }
}

