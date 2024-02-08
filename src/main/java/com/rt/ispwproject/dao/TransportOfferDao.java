package com.rt.ispwproject.dao;

import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.model.TransportOffer;

import java.sql.*;

public class TransportOfferDao {


    // Stores given transport offer in db and returns the id that identifies the transport offer
    public int postOffer(TransportOffer offer) throws DbException
    {
        int transportOfferId = 0;
        Connection connection = DbConnection.getInstance().getConnection();

        // Create callable statement and setup parameters to invoke the createTransportOffer stored procedure
        try (CallableStatement createOfferProc = connection.prepareCall("call createTransportOffer(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"))
        {
            createOfferProc.setString(  "transportType_in", offer.getType().toString());
            createOfferProc.setString(  "companyName_in", offer.getCompany());
            createOfferProc.setInt(     "companyId_in", offer.getCompanyId());
            createOfferProc.setInt(     "transportQuality_in", offer.getQuality());
            createOfferProc.setInt(     "numOfTravelers_in", offer.getNumOfTravelers());
            createOfferProc.setInt(     "pricePerTraveler_in", offer.getPricePerTraveler());
            createOfferProc.setString(  "departureLocationAddress_in", offer.getDepartureLocation().getAddress());
            createOfferProc.setDouble(  "departureLocationLatitude_in", offer.getDepartureLocation().getLatitude());
            createOfferProc.setDouble(  "departureLocationLongitude_in", offer.getDepartureLocation().getLongitude());
            createOfferProc.setString(  "arrivalLocationAddress_in", offer.getArrivalLocation().getAddress());
            createOfferProc.setDouble(  "arrivalLocationLatitude_in", offer.getArrivalLocation().getLatitude());
            createOfferProc.setDouble(  "arrivalLocationLongitude_in", offer.getArrivalLocation().getLongitude());
            createOfferProc.setDate(    "departureDate_in", Date.valueOf(offer.getDepartureDate()));
            createOfferProc.setDate(    "returnDate_in", Date.valueOf(offer.getReturnDate()));

            createOfferProc.registerOutParameter("transportOfferId_out", Types.INTEGER);
            createOfferProc.execute();
            transportOfferId = createOfferProc.getInt("transportOfferId_out");
        } catch(SQLException e)
        {
            throw new DbException("Failed to invoke the \"createTransportOffer\" stored procedure:\n" + e.getMessage());
        }

        return transportOfferId;
    }


    // Removes the transport offer associated to the given id from the db (if exists)
    public void removeOffer(int offerId) throws DbException
    {
        Connection connection = DbConnection.getInstance().getConnection();
        // TODO: Should I check that the offer exists before trying to delete it?

        // Create callable statement and setup parameters to invoke the deleteTransportOffer stored procedure
        try (CallableStatement deleteOfferProc = connection.prepareCall("call deleteTransportOffer(?)"))
        {
            deleteOfferProc.setInt("offerId_in", offerId);
            deleteOfferProc.execute();
        } catch(SQLException e)
        {
            throw new DbException("Failed to invoke the \"deleteTransportOffer\" stored procedure:\n" + e.getMessage());
        }
    }
}
