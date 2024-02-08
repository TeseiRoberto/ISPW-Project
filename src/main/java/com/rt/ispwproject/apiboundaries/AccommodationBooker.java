package com.rt.ispwproject.apiboundaries;

import com.rt.ispwproject.beans.Accommodation;
import com.rt.ispwproject.exceptions.ApiException;

// This class interacts with the external accommodation API to book accommodations
public class AccommodationBooker {

    public void bookAccommodation(Accommodation offer) throws ApiException
    {
        if(isAccommodationAvailable(offer))
        {
            throw new ApiException("AccommodationApi", "AccommodationOffer is available but booking is not implemented yet");
            // TODO: Add implementation
        }

        throw new ApiException("AccommodationApi", "AccommodationOffer is not available anymore");
    }


    public boolean isAccommodationAvailable(Accommodation offer)
    {
        // Here we should query the external accommodation API to ensure that the given offer ,that was made
        // by the travel agency, is still valid (has rooms left to book).
        // For now we don't use any real external API, so we assume that all the offers will be valid (if not null)

        return offer != null;
    }
}
