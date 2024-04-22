package com.rt.ispwproject.apiboundaries;

import com.rt.ispwproject.beans.Accommodation;
import com.rt.ispwproject.beans.Duration;
import com.rt.ispwproject.exceptions.ApiException;

import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

// This class is responsible for the interaction with the external accommodation API, for now we don't
// use any real API instead we generate accommodations with random properties
public class AccommodationSearcher {

    private static final String         API_NAME = "AccommodationApi";
    private static final int            MAX_OFFERS_NUM = 15;                    // Max number of accommodation offers that can be generated randomly
    private static final int            IMAGES_NUM = 5;                         // Number of hotel images stored on the file system
    private static final ArrayList<URL> availableImages = new ArrayList<>();    // URLs to dummy images that are used for the accommodations


    // Loads random images for the accommodations
    public AccommodationSearcher()
    {
        if(availableImages.isEmpty())
        {
            for(int i = 1; i <= IMAGES_NUM; ++i)                // Load URLs for the dummy accommodation images
            {
                String currUrl = "accommodationImages/hotelRoom" + i + ".jpg";
                availableImages.add(getClass().getResource(currUrl));
            }
        }
    }


    // Returns accommodation offers that matches the given criteria
    public List<Accommodation> searchAccommodationOffers(String destination, Duration checkInOutDates, int numOfRoomsRequired) throws ApiException
    {
        if(destination == null || destination.isBlank())
            throw new ApiException(API_NAME, "Destination has not been specified");

        if(checkInOutDates == null)
            throw new ApiException(API_NAME, "Check-in and check-out dates has not been specified");

        if(numOfRoomsRequired <= 0)
            throw new ApiException(API_NAME, "Number of rooms requires cannot be negative or zero");

        // Here we should query the external accommodation API to get the available accommodation offers.
        // For now, we simulate the existence of the API by generating a list of accommodations with random properties

        return generateRandomAccommodationOffers(checkInOutDates.getDurationInDays(), numOfRoomsRequired);
    }


    // Returns a list of links to images associated to the given accommodation
    public List<URL> getAccommodationImages()
    {
        // Here we should take as parameter an accommodation id and then query the external accommodation API to get
        // the accommodation images URLs associated to the given accommodationId.
        // For now, we simulate the existence of the API by selecting some random URLs that refers to dummy images saved in the file system

        return generateRandomImages();
    }


    // Generates a list of accommodations with random properties
    private List<Accommodation> generateRandomAccommodationOffers(int lengthOfStay, int numOfRooms)
    {
        ArrayList<Accommodation> result = new ArrayList<>();
        SecureRandom random = new SecureRandom();
        int accommodationsNum = random.nextInt(1, MAX_OFFERS_NUM);  // Generate number of accommodations available

        for(int i = 0; i < accommodationsNum; ++i)
        {
            int accommodationId = random.nextInt(0, Integer.MAX_VALUE);
            int quality = random.nextInt(1, 6);
            int pricePerNight = quality * random.nextInt(25, 100);
            int totalPrice = pricePerNight * lengthOfStay * numOfRooms;

            Accommodation a = new Accommodation(
                    "Hotel",
                    "Hotel" + i,
                    "Unreal avenue " + random.nextInt(1, 1000),
                    quality,
                    numOfRooms,
                    totalPrice
            );

            a.setAccommodationId(accommodationId);
            a.setImagesLinks(generateRandomImages());
            result.add(a);
        }

        return result;
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
