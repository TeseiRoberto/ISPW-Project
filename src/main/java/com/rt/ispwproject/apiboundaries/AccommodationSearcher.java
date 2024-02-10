package com.rt.ispwproject.apiboundaries;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.rt.ispwproject.beans.Accommodation;
import com.rt.ispwproject.beans.Duration;
import com.rt.ispwproject.exceptions.ApiException;

import java.lang.reflect.Type;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

// This class is responsible for the interaction with the external accommodation API, for now we don't
// use any real API instead we generate accommodations with random properties
public class AccommodationSearcher {

    private static final int    MAX_ACCOMMODATIONS_NUM = 15;    // Max number of accommodations that can be generated randomly
    private static List<URL>    availableImages = null;         // URLs to dummy images that are used for the accommodations
    private static final int    IMAGES_NUM = 5;                 // Number of hotel images stored on the file system


    // Loads random images for the accommodations
    public AccommodationSearcher()
    {
        if(availableImages == null)
        {
            availableImages = new ArrayList<>();
            for(int i = 1; i <= IMAGES_NUM; ++i)                // Load URLs for the dummy accommodation images
            {
                String currUrl = "accommodationImages/hotelRoom" + i + ".jpg";
                availableImages.add(getClass().getResource(currUrl));
            }
        }
    }


    // Returns the available accommodations that matches the given criteria as a json string
    public List<Accommodation> searchAccommodations(String destination, Duration checkInOutDates, int numOfRoomsRequired) throws ApiException
    {
        if(destination == null || checkInOutDates == null || numOfRoomsRequired <= 0)
            return null;

        // Here we should make a query to the external accommodation API to get the available accommodations.
        // For now, we simulate the existence of the API by generating a json object that contains
        // accommodation offers with random properties

        String apiResults = generateRandomAccommodationsJson(checkInOutDates.getDurationInDays(), numOfRoomsRequired);
        List<Accommodation> offers = null;

        // Parse json content and create list of bean classes
        try {
            Type collectionType = new TypeToken<List<Accommodation>>(){}.getType();
            Gson gson = new Gson();
            offers = gson.fromJson(apiResults, collectionType);
        } catch(JsonSyntaxException e)
        {
            throw new ApiException("AccommodationApi", "Failed to parse the json content returned by the API");
        }

        return offers;
    }


    // Returns a list of links to images associated to the given accommodation
    public List<URL> getAccommodationImages(int accommodationId)
    {
        // Here we should make a query to the external accommodation API to get the accommodation images URLs
        // associated to the given accommodationId.
        // For now, we simulate the existence of the API by selecting some random URLs that refers to dummy images in the file system

        return generateRandomImages();
    }


    // Generates a json that contains accommodation offers with random properties
    private String generateRandomAccommodationsJson(int numOfNights, int numOfRooms)
    {
        StringBuilder jsonResult = new StringBuilder("[\n");
        SecureRandom random = new SecureRandom();
        int accommodationsNum = random.nextInt(1, MAX_ACCOMMODATIONS_NUM);  // Generate number of accommodations available

        for(int i = 0; i < accommodationsNum; ++i)                          // Generate all the accommodations
        {
            int accommodationId = random.nextInt();
            String type = "HOTEL";
            String name = "Hotel" + i;
            int quality = random.nextInt(1, 5);
            String address = "unreal avenue " + random.nextInt(1, 1000);
            int pricePerNight = quality * random.nextInt(25, 100);
            int totalPrice = pricePerNight * numOfNights * numOfRooms;

            // Let's build the current json entry
            jsonResult.append("\t{\n");
            jsonResult.append("\t\t'accommodationId': '").append(accommodationId).append("',\n");
            jsonResult.append("\t\t'type': '").append(type).append("',\n");
            jsonResult.append("\t\t'name': '").append(name).append("',\n");
            jsonResult.append("\t\t'quality': ").append(quality).append(",\n");
            jsonResult.append("\t\t'address': '").append(address).append("',\n");
            jsonResult.append("\t\t'numOfRooms': '").append(numOfRooms).append("',\n");
            jsonResult.append("\t\t'pricePerNight': ").append(pricePerNight).append(",\n");
            jsonResult.append("\t\t'totalPrice': ").append(totalPrice).append(",\n");

            // Select some random images for the accommodation and put it in the json

            List<URL> images = generateRandomImages();
            if(images.isEmpty())
            {
                jsonResult.append("\t\t'imagesLinks': []\n}");
            } else {
                jsonResult.append("\t\t'imagesLinks': [\n");
                for(URL img : images)
                    jsonResult.append("\t\t\t'").append(img).append("',\n");

                jsonResult.replace(jsonResult.length() - 2, jsonResult.length() - 1, "]\n\t}");
            }

            jsonResult.append(",\n");
        }

        jsonResult.replace(jsonResult.length() - 2, jsonResult.length() - 1, "\n]");
        return jsonResult.toString();
    }


    // Selects some random images from the available ones in the file system
    private List<URL> generateRandomImages()
    {
        SecureRandom random = new SecureRandom();
        int imagesNum = random.nextInt(0, 4);
        ArrayList<URL> urls = new ArrayList<>();

        for(int i = 0; i < imagesNum; ++i)
        {
            int imgIndex = random.nextInt(0, IMAGES_NUM);
            urls.add(availableImages.get(imgIndex));
        }

        return urls;
    }

}
