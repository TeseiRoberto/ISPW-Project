package com.rt.ispwproject.factories;

import com.rt.ispwproject.model.*;

// This class is responsible for the creation of AccommodationDetails instances
public class AccommodationFactory {

    private static AccommodationFactory instance;

    private AccommodationFactory() {}


    public static AccommodationFactory getInstance()
    {
        if(instance == null)
            instance = new AccommodationFactory();

        return instance;
    }


    // Creates an AccommodationDetails instance that will be treated as an AccommodationRequirements
    public AccommodationRequirements createRequirements(AccommodationType type, int quality, int numOfRooms)
    {
        return new AccommodationDetails(type, "", quality, null, numOfRooms, null, 0);
    }


    // Creates an AccommodationDetails instance that will be treated as an AccommodationOffer
    public AccommodationOffer createOffer(AccommodationType type, String accommodationName, Location location, DateRange lengthOfStay,
                                          int quality, int numOfRooms, int price)
    {
        return new AccommodationDetails(type, accommodationName, quality, location, numOfRooms, lengthOfStay, price);
    }


    // Creates an AccommodationDetails instance that will be treated as an AccommodationChangesRequest
    public AccommodationChangesRequest createChangesRequest(AccommodationType newType, int newQuality, int newNumOfRooms)
    {
        return new AccommodationDetails(newType, "", newQuality, null, newNumOfRooms, null, 0);
    }


}
