package com.rt.ispwproject.dao;

import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.model.AccommodationOffer;

import java.sql.*;

public class AccommodationOfferDao {


    // Stores given accommodation offer in db and returns the id that identifies the accommodation offer
    public int postOffer(AccommodationOffer offer) throws DbException
    {
        int accommodationOfferId = 0;
        Connection connection = DbConnection.getInstance().getConnection();

        // Create callable statement and setup parameters to invoke the createAccommodationOffer stored procedure
        try (CallableStatement createOfferProc = connection.prepareCall("call createAccommodationOffer(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"))
        {
            createOfferProc.setString(  "accommodationType_in", offer.getType().toString());
            createOfferProc.setString(  "accommodationName_in", offer.getName());
            createOfferProc.setInt(     "accommodationId_in", offer.getAccommodationId());
            createOfferProc.setString(  "accommodationAddress_in", offer.getLocation().getAddress());
            createOfferProc.setDouble(  "accommodationLatitude_in", offer.getLocation().getLatitude());
            createOfferProc.setDouble(  "accommodationLongitude_in", offer.getLocation().getLongitude());
            createOfferProc.setInt(     "accommodationQuality_in", offer.getQuality());
            createOfferProc.setInt(     "numOfRoomsOffered_in", offer.getNumOfRooms());
            createOfferProc.setInt(     "pricePerNight_in", offer.getPricePerNight());
            createOfferProc.setDate(    "checkInDate_in", Date.valueOf(offer.getCheckInDate()));
            createOfferProc.setDate(    "checkOutDate_in", Date.valueOf(offer.getCheckOutDate()));

            createOfferProc.registerOutParameter("accommodationOfferId_out", Types.INTEGER);
            createOfferProc.execute();
            accommodationOfferId = createOfferProc.getInt("accommodationOfferId_out");
        } catch(SQLException e)
        {
            throw new DbException("Failed to invoke the \"createAccommodationOffer\" stored procedure:\n" + e.getMessage());
        }

        return accommodationOfferId;
    }


    // Removes the accommodation offer associated to the given id from the db (if exists)
    public void removeOffer(int offerId) throws DbException
    {
        Connection connection = DbConnection.getInstance().getConnection();
        // TODO: Should I check that the offer exists before trying to delete it?

        // Create callable statement and setup parameters to invoke the deleteAccommodationOffer stored procedure
        try (CallableStatement deleteOfferProc = connection.prepareCall("call deleteAccommodationOffer(?)"))
        {
            deleteOfferProc.setInt("offerId_in", offerId);
            deleteOfferProc.execute();
        } catch(SQLException e)
        {
            throw new DbException("Failed to invoke the \"deleteAccommodationOffer\" stored procedure:\n" + e.getMessage());
        }
    }
}
