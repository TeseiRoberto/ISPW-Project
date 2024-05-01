package com.rt.ispwproject.dao;

import com.rt.ispwproject.config.DbConnection;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.factories.ChangesFactory;
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
                    LocationFactory locationFactory = new LocationFactory();
                    Location locatedIn = locationFactory.createLocation(rs.getString("accommodationAddress"));

                    DateRange lengthOfStay = new DateRange(
                            rs.getDate("checkInDate").toLocalDate(),
                            rs.getDate("checkOutDate").toLocalDate()
                    );

                    offer = new AccommodationOffer(
                            AccommodationType.fromPersistenceType(rs.getString("accommodationType")),
                            rs.getString("accommodationName"),
                            rs.getInt("accommodationQuality"),
                            locatedIn,
                            rs.getInt("numOfRoomsOffered"),
                            lengthOfStay,
                            rs.getInt("totalPrice")
                    );

                    offer.setId(rs.getInt("id"));
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


    // Stores the given accommodation changes (the accommodation offer desired by the user) in the db and returns the identifier of the changes
    public int postOfferDesiredByUser(AccommodationChanges desiredOffer) throws DbException
    {
        int desiredAccommodationId = 0;
        Connection connection = DbConnection.getInstance().getConnection();
        try (CallableStatement postRequestProc = connection.prepareCall("CALL createAccommodationChangesRequest(?, ?, ?, ?)"))
        {
            postRequestProc.setString("newAccommodationType_in", desiredOffer.getType().toPersistenceType());
            postRequestProc.setInt("newAccommodationQuality_in", desiredOffer.getQuality());
            postRequestProc.setInt("newNumOfRooms_in", desiredOffer.getNumOfRooms());
            postRequestProc.registerOutParameter("accommodationChangesId_out", Types.INTEGER);

            postRequestProc.execute();
            desiredAccommodationId = postRequestProc.getInt("accommodationChangesId_out");
        } catch(SQLException e) {
            throw new DbException("Failed to invoke the \"createAccommodationChangesRequest\" stored procedure:\n" + e.getMessage());
        }

        return desiredAccommodationId;
    }


    // Retrieves the accommodation changes (the accommodation offer desired by the user) associated to the given id
    public AccommodationOffer getOfferDesiredByUser(int offerId) throws DbException
    {
        AccommodationOffer desiredOffer = null;
        Connection connection = DbConnection.getInstance().getConnection();
        try(CallableStatement getRequestProc = connection.prepareCall("CALL getAccommodationChangesRequest(?)"))
        {
            getRequestProc.setInt("requestId_in", offerId);
            boolean status = getRequestProc.execute();
            if(status)                                          // If the stored procedure returned a result set
            {
                ResultSet rs = getRequestProc.getResultSet();
                if (rs.next())                                  // Construct accommodation changes with data in the result set
                {
                    desiredOffer = ChangesFactory.getInstance().createDesiredAccommodationOffer(
                            AccommodationType.fromPersistenceType(rs.getString("accommodationType")),
                            rs.getInt("accommodationQuality"),
                            rs.getInt("numOfRooms")
                    );

                    desiredOffer.setId(rs.getInt("id"));
                }
            }
        } catch (SQLException e) {
            throw new DbException("Failed to invoke the \"getAccommodationChangesRequest\" stored procedure:\n" + e.getMessage());
        }

        if(desiredOffer == null)
            throw new DbException("Accommodation changes request not found");

        return desiredOffer;
    }


    // Removes the given accommodation changes (accommodation offer desired by the user) from the db (if it exists)
    public void removeOfferDesiredByUser(AccommodationChanges desiredOffer) throws DbException
    {
        Connection connection = DbConnection.getInstance().getConnection();
        try (CallableStatement deleteRequestProc = connection.prepareCall("CALL deleteAccommodationChangesRequest(?)"))
        {
            deleteRequestProc.setInt("requestId_in", desiredOffer.getId());
            deleteRequestProc.execute();
        } catch(SQLException e) {
            throw new DbException("Failed to invoke the \"deleteAccommodationChangesRequest\" stored procedure:\n" + e.getMessage());
        }
    }

}
