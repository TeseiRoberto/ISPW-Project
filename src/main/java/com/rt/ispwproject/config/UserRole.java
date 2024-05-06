package com.rt.ispwproject.config;


// Available roles for users of the system
public enum UserRole {

    SIMPLE_USER,
    TRAVEL_AGENCY;

    // Converts the user role from the system representation to the representation used in the persistence layer
    public String toPersistenceType()
    {
        return switch(this)
        {
            case SIMPLE_USER ->     "SIMPLE_USER";
            case TRAVEL_AGENCY ->   "TRAVEL_AGENCY";
        };
    }


    // Converts the user role from the representation used in the persistence layer to the system representation
    public static UserRole fromPersistenceType(String role) throws IllegalArgumentException
    {
        return switch(role)
        {
            case "SIMPLE_USER" ->   SIMPLE_USER;
            case "TRAVEL_AGENCY" -> TRAVEL_AGENCY;
            default ->              throw new IllegalArgumentException("User role cannot be converted from persistence to system representation");
        };
    }

}
