package com.rt.ispwproject.factories;

import com.rt.ispwproject.apiboundaries.AddressChecker;
import com.rt.ispwproject.exceptions.ApiException;
import com.rt.ispwproject.model.Location;

// This class is responsible for the creation of Location instances, in particular
// before creating a new Location instance using the given address it checks that the address exists using the AddressChecker class.
public class LocationFactory {

    private final AddressChecker        checker;

    public LocationFactory()
    {
        this.checker = new AddressChecker();
    }


    // Checks that the given address exists and returns a Location instance
    public Location createLocation(String address) throws IllegalArgumentException
    {
        try {
            checker.checkAddressExistence(address);
        } catch (ApiException e)
        {
            throw new IllegalArgumentException("Address \"" + address + "\" is unknown");
        }

        return new Location(address);
    }

}
