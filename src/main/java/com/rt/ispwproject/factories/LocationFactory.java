package com.rt.ispwproject.factories;

import com.rt.ispwproject.apiboundaries.AddressChecker;
import com.rt.ispwproject.exceptions.ApiException;
import com.rt.ispwproject.model.Location;

// This class is responsible for the creation of Location instances, in particular
// before creating an instance with the given address it checks that the address exists using the AddressChecker class.
public class LocationFactory {

    private static LocationFactory      instance;
    private final AddressChecker        checker;

    private LocationFactory()
    {
        this.checker = new AddressChecker();
    }


    public static LocationFactory getInstance()
    {
        if(instance == null)
            instance = new LocationFactory();

        return instance;
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
