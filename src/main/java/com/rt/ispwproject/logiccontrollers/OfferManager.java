package com.rt.ispwproject.logiccontrollers;

import com.rt.ispwproject.beans.*;
import com.rt.ispwproject.dao.HolidayOfferDao;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.model.*;

import java.time.LocalDate;

public class OfferManager {

    private static final double  TRAVEL_AGENCY_PERCENTAGE = 0.05;    // Percentage of profit for the travel agency if the offer is accepted by the user


    // Inserts a new offer (intended for the given announcement) in the system
    public void makeOfferToUser(Session currUser, Announcement announce, Offer offer) throws DbException
    {
        HolidayMetadata metadata = new HolidayMetadata(0, offer.getBidder(), LocalDate.now(), 0);
        DateRange holidayDuration = new DateRange(offer.getDepartureDate(), offer.getReturnDate());

        Accommodation accommodation = new Accommodation(
                offer.getAccommodationOffer().getType(),
                offer.getAccommodationOffer().getName(),
                offer.getAccommodationOffer().getAddress(),
                offer.getAccommodationOffer().getQuality(),
                offer.getAccommodationOffer().getNumOfRooms(),
                holidayDuration,
                offer.getAccommodationOffer().getPricePerNight()
        );

        Transport transport = new Transport(
                offer.getTransportOffer().getType(),
                offer.getTransportOffer().getCompanyName(),
                offer.getTransportOffer().getQuality(),
                new Route(offer.getTransportOffer().getDepartureLocation(), offer.getDestination()),
                offer.getTransportOffer().getNumOfTravelers(),
                offer.getTransportOffer().getPricePerTraveler(),
                holidayDuration
        );

        HolidayOffer holidayOffer = new HolidayOffer(metadata, offer.getDestination(), holidayDuration, offer.getPrice(), accommodation, transport);
        HolidayOfferDao offerDao = new HolidayOfferDao();
        offerDao.postOffer(currUser.getUserId(), announce.getId(), holidayOffer);
    }


    // Calculates the price of the offer according to the accommodation and transport prices and the travel agency percentage
    public int calculateOfferPrice(int accommodationPrice, int transportPrice) throws IllegalArgumentException
    {
        int price = accommodationPrice + transportPrice;
        price += (int) (price * TRAVEL_AGENCY_PERCENTAGE);

        if(price <= 0)
            throw new IllegalArgumentException("Offer price cannot be negative or zero!");

        return price;
    }

}
