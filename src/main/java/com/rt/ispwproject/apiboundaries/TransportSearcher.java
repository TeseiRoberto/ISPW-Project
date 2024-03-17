package com.rt.ispwproject.apiboundaries;

import com.rt.ispwproject.beans.Duration;
import com.rt.ispwproject.beans.Transport;
import com.rt.ispwproject.exceptions.ApiException;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

// This class is responsible for the interaction with the external transport API but for now we don't
// use any real API instead we generate transports with random properties
public class TransportSearcher {

    // List of the available transport types
    private static final String         API_NAME = "TransportApi";
    private static final List<String>   availableTransportTypes = List.of("Airplane", "Train", "Ferry", "Bus");
    private static final int            MAX_OFFERS_NUM = 15;    // Max number of transport offers that can be generated randomly


    // Queries the API for transports tht
    public List<Transport> searchTransportOffers(String departureLocation, String arrivalLocation,
                                                 Duration departureAnsReturnDates, int numOfTravelers) throws ApiException
    {
        if(departureLocation == null || arrivalLocation == null)
            throw new ApiException(API_NAME, "Departure and/or arrival location has not been specified");

        if(departureAnsReturnDates == null)
            throw new ApiException(API_NAME, "Departure and return dates has not been specified");

        if(numOfTravelers <= 0)
            throw new ApiException(API_NAME, "Number of travelers cannot be negative or zero");

        // Here we should query the external transports API to get the available transport offers.
        // For now, we simulate the existence of the API by generating a list of transports with random properties

        return generateRandomTransportOffers(departureLocation, arrivalLocation, numOfTravelers);
    }


    // Generates a list of transports with random properties
    private List<Transport> generateRandomTransportOffers(String departureLocation, String arrivalLocation, int numOfTravelers)
    {
        ArrayList<Transport> result = new ArrayList<>();
        SecureRandom random = new SecureRandom();
        int transportsNum = random.nextInt(1, MAX_OFFERS_NUM);          // Generate number of transports available

        for(int i = 0; i < transportsNum; ++i)                          // Generate all the transports
        {
            String type = availableTransportTypes.get(random.nextInt(0, availableTransportTypes.size() - 1));
            String companyName = "Company " + i;
            int companyId = random.nextInt(0, Integer.MAX_VALUE);
            int quality = random.nextInt(1, 5);
            int pricePerTraveler = switch (type)
            {
                case "Train" -> quality * random.nextInt(40, 300);
                case "Ferry" -> quality * random.nextInt(100, 350);
                case "Bus" -> quality * random.nextInt(20, 100);
                default -> quality * random.nextInt(100, 1000);
            };

            Transport t = new Transport(type, companyName, quality, departureLocation, arrivalLocation, numOfTravelers, pricePerTraveler);
            t.setCompanyId(companyId);
            result.add(t);
        }

        return result;
    }

}
