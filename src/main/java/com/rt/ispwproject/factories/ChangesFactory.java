package com.rt.ispwproject.factories;


import com.rt.ispwproject.beans.ChangesOnOffer;
import com.rt.ispwproject.model.*;

// This class is responsible for the creation of HolidayOfferAdapter instances
public class ChangesFactory {

    private static ChangesFactory   instance = null;


    private ChangesFactory() {}


    public static ChangesFactory getInstance()
    {
        if(instance == null)
            instance = new ChangesFactory();

        return instance;
    }


    // Constructs an HolidayOfferAdapter instance
    // @agencyOffer: the offer as made by the travel agency
    // @requestOwner: the user that wants to request changes
    // @changes: bean class that contains the requested changes
    public ChangesRequest createChangesRequest(HolidayOffer agencyOffer, Profile requestOwner, ChangesOnOffer changes) throws IllegalArgumentException
    {
        if(agencyOffer == null)
            throw new IllegalArgumentException("An Holiday offer must be specified to create a request of changes!");

        if(requestOwner == null)
            throw new IllegalArgumentException("The user which is requesting changes is unknown!");

        if(changes == null)
            throw new IllegalArgumentException("No change has been specified!");

        // Let's construct the offer desired by the user
        HolidayOfferMetadata desiredOfferMetadata = new HolidayOfferMetadata(
                requestOwner,
                HolidayOfferState.PENDING,
                agencyOffer.getMetadata().getOfferId(),
                agencyOffer.getMetadata().getOfferOwner()
        );

        // By default, the desired holiday offer is equal to the one made by the travel agency
        HolidayOffer desiredOffer = new HolidayOffer(
                desiredOfferMetadata,
                agencyOffer.getDestination(),
                agencyOffer.getHolidayDuration(),
                agencyOffer.getPrice(),
                agencyOffer.getAccommodationOffer(),
                agencyOffer.getTransportOffer()
        );

        if(changes.isDurationChangeRequired())
        {
            desiredOffer.setHolidayDuration(
                    new DateRange(
                            changes.getHolidayDuration().getDepartureDate(),
                            changes.getHolidayDuration().getReturnDate()
                    )
            );
        }

        if(changes.isPriceChangeRequired())
            desiredOffer.setPrice(changes.getPrice());

        if(changes.isAccommodationChangeRequired())
        {
            AccommodationOffer desiredAccommodation = createDesiredAccommodationOffer(
                    AccommodationType.fromViewType(changes.getAccommodationChanges().getType()),
                    changes.getAccommodationChanges().getQuality(),
                    changes.getAccommodationChanges().getNumOfRooms()
            );

            desiredOffer.setAccommodationOffer(desiredAccommodation);
        }

        if(changes.isTransportChangeRequired())
        {
            LocationFactory locationFactory = new LocationFactory();
            Location departureLocation = locationFactory.createLocation(changes.getTransportChanges().getDepartureLocation());
            Location arrivalLocation = locationFactory.createLocation(changes.getTransportChanges().getArrivalLocation());

            Route desiredRoute = new Route(departureLocation, arrivalLocation);

            TransportOffer desiredTransport = createDesiredTransportOffer(
                    TransportType.fromViewType(changes.getTransportChanges().getType()),
                    changes.getTransportChanges().getQuality(),
                    changes.getTransportChanges().getNumOfTravelers(),
                    desiredRoute
            );

            desiredOffer.setDestination(arrivalLocation);
            desiredOffer.setTransportOffer(desiredTransport);
        }

        ChangesRequest request = new HolidayOfferAdapter(agencyOffer, desiredOffer, changes.getChangesDescription());

        // Now if the request is valid we can return it, otherwise an exception will be thrown
        checkRequestValidity(request);
        return request;
    }


    public ChangesRequest createChangesRequest(HolidayOffer agencyOffer, HolidayOffer desiredOffer, String changesDescription) throws IllegalArgumentException
    {
        if(agencyOffer == null)
            throw new IllegalArgumentException("An Holiday offer must be specified to create a request of changes!");

        if(desiredOffer == null)
            throw new IllegalArgumentException("A desired holiday offer must be specified to create a request of changes!");

        ChangesRequest request = new HolidayOfferAdapter(agencyOffer, desiredOffer, changesDescription);
        checkRequestValidity(request);
        return request;
    }


    // Constructs an AccommodationOffer instance that represents the accommodation offer desired by a user
    public AccommodationOffer createDesiredAccommodationOffer(AccommodationType type, int quality, int numOfRooms)
    {
        return new AccommodationOffer(type, "", quality, null, numOfRooms, null, 0);
    }


    // Constructs a TransportOffer instance that represents the transport offer desired by a user
    public TransportOffer createDesiredTransportOffer(TransportType type, int quality, int numOfTravelers, Route fromToLocation) throws IllegalArgumentException
    {
        return new TransportOffer(type, "", quality, numOfTravelers, 0, fromToLocation, null);
    }


    // Checks that the given request is valid (a changes request is valid if it contains at least one change on the original offer)
    private void checkRequestValidity(ChangesRequest request) throws IllegalArgumentException
    {
        if(request != null)
        {
            if(request.getRequestOwner().isEqual(request.getRelativeOfferOwner()))
                throw new IllegalArgumentException("User cannot request changes on an offer made by himself!");

            if(request.isHolidayDurationChangeRequired())
                return;

            if(request.isPriceChangeRequired())
                return;

            if(request.isAccommodationChangeRequired())
                return;

            if(request.isTransportChangeRequired())
                return;
        }

        throw new IllegalArgumentException("No change has been specified!");
    }

}
