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
    public TransportRequirements createRequirements(TransportType type, int quality, Route fromToLocation, int numOfTravelers)
    {
        return new TransportDetails(type, "", quality, fromToLocation, numOfTravelers, 0, null);
    }


    // Creates a TransportDetails instance that will be treated as a TransportOffer
    public TransportOffer createOffer(TransportType type, String companyName, Route fromToLocation,
                                      DateRange departureAndReturnDates, int quality, int numOfTravelers, int pricePerTraveler)
    {
        return new TransportDetails(type, companyName, quality, fromToLocation, numOfTravelers, pricePerTraveler, departureAndReturnDates);
    }


    // Creates a TransportDetails instance that will be treated as a TransportChangesRequest
    public TransportChangesRequest createChangesRequest(TransportType type, int quality, Route fromToLocation, int numOfTravelers)
    {
        return new TransportDetails(type, "", quality, fromToLocation, numOfTravelers, 0, null);
    }

}
