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
    public AccommodationRequirements createRequirements(AccommodationType type, int quality, int numOfRooms) throws IllegalArgumentException
    {
        AccommodationDetails newReq = new AccommodationDetails();
        newReq.setType(type);
        newReq.setQuality(quality);
        newReq.setNumOfRooms(numOfRooms);

        return newReq;
    }


    // Creates an AccommodationDetails instance that will be treated as an AccommodationOffer
    public AccommodationOffer createOffer(AccommodationType type, String accommodationName, Location location, DateRange lengthOfStay,
                                          int quality, int numOfRooms, int price) throws IllegalArgumentException
    {
        AccommodationDetails newOffer = new AccommodationDetails();
        newOffer.setType(type);
        newOffer.setName(accommodationName);
        newOffer.setLocation(location);
        newOffer.setLengthOfStay(lengthOfStay);
        newOffer.setQuality(quality);
        newOffer.setNumOfRooms(numOfRooms);
        newOffer.setPrice(price);

        return newOffer;
    }


    // Creates an AccommodationDetails instance that will be treated as an AccommodationChangesRequest
    public AccommodationChangesRequest createChangesRequest(AccommodationType newType, int newQuality, int newNumOfRooms) throws IllegalArgumentException
    {
        AccommodationDetails changes = new AccommodationDetails();
        changes.setType(newType);
        changes.setQuality(newQuality);
        changes.setNumOfRooms(newNumOfRooms);

        return changes;
    }


}
