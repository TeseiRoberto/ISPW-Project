package com.rt.ispwproject.apiboundaries;

import com.rt.ispwproject.beans.Transport;
import com.rt.ispwproject.exceptions.ApiException;

// This class interacts with the external transports API to book transports
public class TransportBooker {

    public void bookTransport(Transport offer) throws ApiException
    {
        if(isTransportAvailable(offer))
        {
            throw new ApiException("TransportApi", "TransportOffer is available but booking is not implemented yet");
            // TODO: Add implementation
        }

        throw new ApiException("TransportApi", "TransportOffer is not available anymore");
    }


    public boolean isTransportAvailable(Transport offer)
    {
        // Here we should query the external transport API to ensure that the given offer ,that was made
        // by the travel agency, is still valid.
        // For now we don't use any real external API, so we assume that all the offers will be valid (if not null)

        return offer != null;
    }
}
