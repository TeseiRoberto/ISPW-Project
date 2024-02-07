package com.rt.ispwproject.apiboundaries;

import com.rt.ispwproject.beans.AccommodationOffer;
import com.rt.ispwproject.exceptions.ApiException;

// This class interacts with the external accommodation API to book accommodations
public class AccommodationBooker {

    public void bookAccommodation(AccommodationOffer offer) throws ApiException
    {
        if(isAccommodationAvailable(offer))
        {
            throw new ApiException("AccommodationApi", "Accommodation is available but booking is not implemented yet");
            // TODO: Add implementation
        }

        throw new ApiException("AccommodationApi", "Accommodation is not available anymore");
    }


    public boolean isAccommodationAvailable(AccommodationOffer offer)
    {
        // Here we should query the external accommodation API to ensure that the given offer ,that was made
        // by the travel agency, is still valid (has rooms left to book).
        // For now we don't use any real external API, so we assume that all the offers will be valid (if not null)

        return offer != null;
    }
}
