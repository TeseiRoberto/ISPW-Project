package com.rt.ispwproject.config;

public enum UserRole { UNKNOWN(-1), SIMPLE_USER(1), TRAVEL_AGENCY(2);

    private final int value;

    UserRole(int value)
    {
        if(value == 1 || value == 2)
            this.value = value;
        else
            this.value = -1;
    }


    public static UserRole fromInt(int value)
    {
        return switch (value) {
            case 1 -> SIMPLE_USER;
            case 2 -> TRAVEL_AGENCY;
            default -> UNKNOWN;
        };
    }
}
