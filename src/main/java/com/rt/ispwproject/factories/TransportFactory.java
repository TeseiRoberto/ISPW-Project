package com.rt.ispwproject.factories;

import com.rt.ispwproject.model.*;

// This class is responsible for the creation of TransportDetails instances
public class TransportFactory {

    private static TransportFactory instance;

    private TransportFactory() {}


    public static TransportFactory getInstance()
    {
        if(instance == null)
            instance = new TransportFactory();

        return instance;
    }


    // Creates a TransportDetails instance that will be treated as a TransportRequirements
    public TransportRequirements createRequirements(TransportType type, int quality, Route fromToLocation, int numOfTravelers) throws IllegalArgumentException
    {
        TransportDetails newReq = new TransportDetails();
        newReq.setType(type);
        newReq.setQuality(quality);
        newReq.setFromToLocation(fromToLocation);
        newReq.setNumOfTravelers(numOfTravelers);

        return newReq;
    }


    // Creates a TransportDetails instance that will be treated as a TransportOffer
    public TransportOffer createOffer(TransportType type, String companyName, Route fromToLocation,
                                      DateRange departureAndReturnDates, int quality, int numOfTravelers, int pricePerTraveler) throws IllegalArgumentException
    {
        TransportDetails newOffer = new TransportDetails();
        newOffer.setType(type);
        newOffer.setCompanyName(companyName);
        newOffer.setFromToLocation(fromToLocation);
        newOffer.setDepartureAndReturnDates(departureAndReturnDates);
        newOffer.setQuality(quality);
        newOffer.setNumOfTravelers(numOfTravelers);
        newOffer.setPricePerTraveler(pricePerTraveler);

        return newOffer;
    }


    // Creates a TransportDetails instance that will be treated as a TransportChangesRequest
    public TransportChangesRequest createChangesRequest(TransportType type, int quality, Route fromToLocation, int numOfTravelers) throws IllegalArgumentException
    {
        TransportDetails changes = new TransportDetails();
        changes.setType(type);
        changes.setQuality(quality);
        changes.setFromToLocation(fromToLocation);
        changes.setNumOfTravelers(numOfTravelers);

        return changes;
    }

}
