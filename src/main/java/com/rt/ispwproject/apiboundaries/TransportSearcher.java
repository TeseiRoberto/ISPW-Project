package com.rt.ispwproject.apiboundaries;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.rt.ispwproject.beans.Duration;
import com.rt.ispwproject.beans.Transport;
import com.rt.ispwproject.exceptions.ApiException;

import java.lang.reflect.Type;
import java.security.SecureRandom;
import java.util.List;

// This class is responsible for the interaction with the external transport API but for now we don't
// use any real API instead we generate transports with random properties
public class TransportSearcher {

    // List of the available transport types
    private static final List<String>   availableTransportTypes = List.of("Airplane", "Train", "Ferry", "Bus");
    private static final int            MAX_TRANSPORTS_NUM = 15;    // Max number of transports that can be generated randomly


    public List<Transport> searchTransports(String departureLocation, String arrivalLocation,
                                            Duration departureAnsReturnDates, int numOfTravelers) throws ApiException
    {
        if(departureLocation == null || arrivalLocation == null || departureAnsReturnDates == null || numOfTravelers <= 0)
            return null;

        // Here we should make a query to the external transports API to get the available transports.
        // For now, we simulate the existence of the API by generating a json object that contains
        // transport offers with random properties

        String apiResults = generateRandomTransportsJson(departureLocation, numOfTravelers);
        List<Transport> offers = null;

        // Parse json content and create list of bean classes
        try {
            Type collectionType = new TypeToken<List<Transport>>(){}.getType();
            Gson gson = new Gson();
            offers = gson.fromJson(apiResults, collectionType);
        } catch(JsonSyntaxException e)
        {
            throw new ApiException("TransportApi", "Failed to parse the json content returned by the API");
        }

        return offers;
    }


    // Generates a json that contains transport offers with random properties
    private String generateRandomTransportsJson(String departureLocation, int numOfTravelers)
    {
        StringBuilder jsonResult = new StringBuilder("[\n");
        SecureRandom random = new SecureRandom();
        int transportsNum = random.nextInt(1, MAX_TRANSPORTS_NUM);      // Generate number of transports available

        for(int i = 0; i < transportsNum; ++i)                          // Generate all the transports
        {
            String type = availableTransportTypes.get(random.nextInt(0, availableTransportTypes.size() - 1));
            String companyName = "Company " + i;
            int companyId = random.nextInt();
            int quality = random.nextInt(1, 5);

            int pricePerTraveler = switch (type) {
                case "Tai" -> quality * random.nextInt(40, 300);
                case "Ferry" -> quality * random.nextInt(100, 350);
                case "Bus" -> quality * random.nextInt(20, 100);
                default -> quality * random.nextInt(100, 1000);
            };

            // Let's build the current json entry
            jsonResult.append("\t{\n");
            jsonResult.append("\t\t'type': '").append(type).append("',\n");
            jsonResult.append("\t\t'companyName': '").append(companyName).append("',\n");
            jsonResult.append("\t\t'companyId': '").append(companyId).append("',\n");
            jsonResult.append("\t\t'quality': ").append(quality).append(",\n");
            jsonResult.append("\t\t'departureLocation': '").append(departureLocation).append("',\n");
            jsonResult.append("\t\t'pricePerTraveler': ").append(pricePerTraveler).append(",\n");
            jsonResult.append("\t\t'numOfTravelers': ").append(numOfTravelers);

            jsonResult.append("\n\t},\n");
        }

        jsonResult.replace(jsonResult.length() - 2, jsonResult.length() - 1, "\n]");
        return jsonResult.toString();
    }

}
