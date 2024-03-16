package com.rt.ispwproject.dao;

import com.rt.ispwproject.config.DbConnection;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.factories.AccommodationFactory;
import com.rt.ispwproject.factories.LocationFactory;
import com.rt.ispwproject.model.*;

import java.sql.*;

public class AccommodationOfferDao {


    // Stores given accommodation offer in db and returns the id that identifies the accommodation offer
    public int postOffer(AccommodationOffer offer) throws DbException
    {
        int accommodationOfferId = 0;
        Connection connection = DbConnection.getInstance().getConnection();

        // Create callable statement and setup parameters to invoke the createAccommodationOffer stored procedure
        try (CallableStatement createOfferProc = connection.prepareCall("call createAccommodationOffer(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"))
        {
            createOfferProc.setString(  "accommodationType_in", offer.getType().toPersistenceType());
            createOfferProc.setString(  "accommodationName_in", offer.getName());
            createOfferProc.setInt(     "accommodationId_in", offer.getAccommodationId());
            createOfferProc.setString(  "accommodationAddress_in", offer.getLocation().getAddress());
            createOfferProc.setInt(     "accommodationQuality_in", offer.getQuality());
            createOfferProc.setInt(     "numOfRoomsOffered_in", offer.getNumOfRooms());
            createOfferProc.setInt(     "totalPrice_in", offer.getPrice());
            createOfferProc.setDate(    "checkInDate_in", Date.valueOf(offer.getLengthOfStay().getStartDate()));
            createOfferProc.setDate(    "checkOutDate_in", Date.valueOf(offer.getLengthOfStay().getEndDate()));

            createOfferProc.registerOutParameter("accommodationOfferId_out", Types.INTEGER);
            createOfferProc.execute();
            accommodationOfferId = createOfferProc.getInt("accommodationOfferId_out");
        } catch(SQLException e) {
            throw new DbException("Failed to invoke the \"createAccommodationOffer\" stored procedure:\n" + e.getMessage());
        }

        return accommodationOfferId;
    }


    // Retrieves the accommodation offer associated to the given id from the db (if exists)
    public AccommodationOffer getOfferById(int offerId) throws DbException
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
                    Location locatedIn = LocationFactory.getInstance().createLocation(rs.getString("accommodationAddress"));
                    DateRange lengthOfStay = new DateRange(
                            rs.getDate("checkInDate").toLocalDate(),
                            rs.getDate("checkOutDate").toLocalDate()
                    );

                    offer = AccommodationFactory.getInstance().createOffer(
                            AccommodationType.fromPersistenceType(rs.getString("accommodationType")),
                            rs.getString("accommodationName"),
                            locatedIn,
                            lengthOfStay,
                            rs.getInt("accommodationQuality"),
                            rs.getInt("numOfRoomsOffered"),
                            rs.getInt("totalPrice")
                    );

                    offer.setAccommodationId(rs.getInt("accommodationId"));
                }
                rs.close();
            }
        } catch(SQLException e) {
            throw new DbException("Failed to invoke the \"getAccommodationOffer\" stored procedure:\n" + e.getMessage());
        } catch(IllegalArgumentException e) {
            throw new DbException("Cannot get accommodation offer, persistence layer returned invalid data:\n" + e.getMessage());
        }

        if(offer == null)
            throw new DbException("Accommodation offer not found");

        return offer;
    }


    // Updates all the fields of the offer in the db with the newOffer data (the offer must already exist in the db)
    public void updateOffer(AccommodationOffer newOffer) throws DbException
    {
        Connection connection = DbConnection.getInstance().getConnection();
        try (CallableStatement updateOfferProc = connection.prepareCall("call updateAccommodationOffer(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"))
        {
            updateOfferProc.setInt("accommodationOfferId_in", newOffer.getId());
            updateOfferProc.setString("newAccommodationType_in", newOffer.getType().toPersistenceType());
            updateOfferProc.setString("newAccommodationName_in", newOffer.getName());
            updateOfferProc.setInt("newAccommodationId_in", newOffer.getAccommodationId());
            updateOfferProc.setString("newAccommodationAddress_in", newOffer.getLocation().getAddress());
            updateOfferProc.setInt("newAccommodationQuality_in", newOffer.getQuality());
            updateOfferProc.setInt("newNumOfRoomsOffered_in", newOffer.getNumOfRooms());
            updateOfferProc.setInt("newTotalPrice_in", newOffer.getPrice());
            updateOfferProc.setDate("newCheckInDate_in", Date.valueOf(newOffer.getLengthOfStay().getStartDate()));
            updateOfferProc.setDate("newCheckOutDate_in", Date.valueOf(newOffer.getLengthOfStay().getEndDate()));

            updateOfferProc.execute();
        } catch(SQLException e) {
            throw new DbException("Failed to invoke the \"updateAccommodationOffer\" stored procedure:\n" + e.getMessage());
        }
    }


    // Removes the accommodation offer associated to the given id from the db (if exists)
    public void removeOffer(AccommodationOffer offer) throws DbException
    {
        Connection connection = DbConnection.getInstance().getConnection();

        // Create callable statement and setup parameters to invoke the deleteAccommodationOffer stored procedure
        try (CallableStatement deleteOfferProc = connection.prepareCall("call deleteAccommodationOffer(?)"))
        {
            deleteOfferProc.setInt("offerId_in", offer.getId());
            deleteOfferProc.execute();
        } catch(SQLException e) {
            throw new DbException("Failed to invoke the \"deleteAccommodationOffer\" stored procedure:\n" + e.getMessage());
        }
    }

}
