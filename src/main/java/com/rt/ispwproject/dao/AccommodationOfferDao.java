package com.rt.ispwproject.dao;

import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.model.*;

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
            createOfferProc.setString(  "accommodationType_in", offer.getType().toPersistenceType());
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


    // Retrieves the accommodation offer associated to the given id from the db (if exists)
    public AccommodationOffer getOffer(int offerId) throws DbException
    {
        AccommodationOffer offer = null;
        Connection connection = DbConnection.getInstance().getConnection();

        // Create callable statement and setup parameters to invoke the getAccommodationOffer stored procedure
        try (CallableStatement getOfferProc = connection.prepareCall("call getAccommodationOffer(?)"))
        {
            getOfferProc.setInt("offerId_in", offerId);

            boolean status = getOfferProc.execute();
            if(status)                                          // If the stored procedure returned a result set
            {
                ResultSet rs = getOfferProc.getResultSet();
                if(rs.next())                                   // Construct accommodation offer with data in the result set
                {
                    Location accommodationAddress = new Location(
                            rs.getString("accommodationAddress"),
                            rs.getDouble("accommodationLatitude"),
                            rs.getDouble("accommodationLongitude")
                    );

                    DateRange checkInOutDates = new DateRange(
                            rs.getDate("checkInDate").toLocalDate(),
                            rs.getDate("checkOutDate").toLocalDate()
                    );

                    offer = new AccommodationOffer(
                            AccommodationType.fromPersistenceType(rs.getString("accommodationType")),
                            rs.getString("accommodationName"),
                            accommodationAddress,
                            rs.getInt("accommodationQuality"),
                            rs.getInt("numOfRoomsOffered"),
                            checkInOutDates,
                            rs.getInt("pricePerNight")
                    );

                    offer.setAccommodationId(rs.getInt("accommodationId"));
                }
                rs.close();
            }
        } catch(SQLException e)
        {
            throw new DbException("Failed to invoke the \"getAccommodationOffer\" stored procedure:\n" + e.getMessage());
        } catch(IllegalArgumentException e)
        {
            throw new DbException("The \"getAccommodationOffer\" stored procedure has returned invalid data:\n" + e.getMessage());
        }

        if(offer == null)
            throw new DbException("Accommodation offer not found");

        return offer;
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
