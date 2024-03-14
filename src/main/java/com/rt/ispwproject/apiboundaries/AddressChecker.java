package com.rt.ispwproject.apiboundaries;

import com.rt.ispwproject.exceptions.ApiException;

// This class is responsible for the interaction with the external maps API
public class AddressChecker {


    // Checks if the given address exists on the earth
    public void checkAddressExistence(String address) throws ApiException
    {
        if(address == null || address.isEmpty())
            throw new ApiException("MapsApi", "Address cannot be empty");

        // Here we should query the API to check that the given address exists on the earth.
        // For now we assume that all addresses will exist (because we are not using any external system/API).
    }

}
