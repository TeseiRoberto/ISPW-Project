package com.rt.ispwproject.dao;

import com.rt.ispwproject.config.DbConnection;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.factories.ChangesFactory;
import com.rt.ispwproject.factories.LocationFactory;
import com.rt.ispwproject.model.*;

import java.sql.*;

public class TransportOfferDao {


    // Stores given transport offer in db and returns the id that identifies the transport offer
    public int postOffer(TransportOffer offer) throws DbException
    {
        int transportOfferId = 0;
        Connection connection = DbConnection.getInstance().getConnection();

        // Create callable statement and setup parameters to invoke the createTransportOffer stored procedure
        try (CallableStatement createOfferProc = connection.prepareCall("call createTransportOffer(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"))
        {
            createOfferProc.setString(  "transportType_in", offer.getType().toPersistenceType());
            createOfferProc.setString(  "companyName_in", offer.getCompanyName());
            createOfferProc.setInt(     "companyId_in", offer.getCompanyId());
            createOfferProc.setInt(     "transportQuality_in", offer.getQuality());
            createOfferProc.setInt(     "numOfTravelers_in", offer.getNumOfTravelers());
            createOfferProc.setInt(     "pricePerTraveler_in", offer.getPricePerTraveler());
            createOfferProc.setString(  "departureLocationAddress_in", offer.getRoute().getDepartureLocation().getAddress());
            createOfferProc.setString(  "arrivalLocationAddress_in", offer.getRoute().getArrivalLocation().getAddress());
            createOfferProc.setDate(    "departureDate_in", Date.valueOf(offer.getDepartureAndReturnDates().getStartDate()));
            createOfferProc.setDate(    "returnDate_in", Date.valueOf(offer.getDepartureAndReturnDates().getEndDate()));

            createOfferProc.registerOutParameter("transportOfferId_out", Types.INTEGER);
            createOfferProc.execute();
            transportOfferId = createOfferProc.getInt("transportOfferId_out");
        } catch(SQLException e) {
            throw new DbException("Failed to invoke the \"createTransportOffer\" stored procedure:\n" + e.getMessage());
        }

        return transportOfferId;
    }


    // Retrieves the transport offer associated to the given id from the db (if exists)
    public TransportOffer getOfferById(int offerId) throws DbException
    {
        TransportOffer offer = null;
        Connection connection = DbConnection.getInstance().getConnection();

        // Create callable statement and setup parameters to invoke the getTransportOffer stored procedure
        try (CallableStatement getOfferProc = connection.prepareCall("call getTransportOffer(?)"))
        {
            getOfferProc.setInt("offerId_in", offerId);

            boolean status = getOfferProc.execute();
            if(status)                                          // If the stored procedure returned a result set
            {
                ResultSet rs = getOfferProc.getResultSet();
                if(rs.next())                                   // Construct transport offer with data in the result set
                {
                    Route transportRoute = new Route(
                            LocationFactory.getInstance().createLocation(rs.getString("departureLocationAddress")),
                            LocationFactory.getInstance().createLocation(rs.getString("arrivalLocationAddress"))
                    );

                    DateRange departureAndReturnDates = new DateRange(
                            rs.getDate("departureDate").toLocalDate(),
                            rs.getDate("returnDate").toLocalDate()
                    );

                    offer = new TransportOffer(
                            rs.getInt("id"),
                            TransportType.fromPersistenceType(rs.getString("transportType")),
                            rs.getString("companyName"),
                            rs.getInt("companyId"),
                            rs.getInt("transportQuality"),
                            rs.getInt("numOfTravelers"),
                            rs.getInt("pricePerTraveler"),
                            transportRoute,
                            departureAndReturnDates
                    );
                }
                rs.close();
            }
        } catch(SQLException e) {
            throw new DbException("Failed to invoke the \"getTransportOffer\" stored procedure:\n" + e.getMessage());
        } catch(IllegalArgumentException e) {
            throw new DbException("Cannot get transport offer, persistence layer returned invalid data:\n" + e.getMessage());
        }

        if(offer == null)
            throw new DbException("Transport offer not found");

        return offer;
    }

    // Updates all the fields of the offer in the db with the newOffer data (the offer must already exist in the db)
    public void updateOffer(TransportOffer newOffer) throws DbException
    {
        Connection connection = DbConnection.getInstance().getConnection();
        try (CallableStatement updateOfferProc = connection.prepareCall("call updateTransportOffer(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"))
        {
            updateOfferProc.setInt(     "transportOfferId_in", newOffer.getId());
            updateOfferProc.setString(  "newTransportType_in", newOffer.getType().toPersistenceType());
            updateOfferProc.setString(  "newCompanyName_in", newOffer.getCompanyName());
            updateOfferProc.setInt(     "newCompanyId_in", newOffer.getCompanyId());
            updateOfferProc.setInt(     "newTransportQuality_in", newOffer.getQuality());
            updateOfferProc.setInt(     "newNumOfTravelers_in", newOffer.getNumOfTravelers());
            updateOfferProc.setInt(     "newPricePerTraveler_in", newOffer.getPricePerTraveler());
            updateOfferProc.setString(  "newDepartureLocationAddress_in", newOffer.getRoute().getDepartureLocation().getAddress());
            updateOfferProc.setString(  "newArrivalLocationAddress_in", newOffer.getRoute().getArrivalLocation().getAddress());
            updateOfferProc.setDate(    "newDepartureDate_in", Date.valueOf(newOffer.getDepartureAndReturnDates().getStartDate()));
            updateOfferProc.setDate(    "newReturnDate_in", Date.valueOf(newOffer.getDepartureAndReturnDates().getEndDate()));

            updateOfferProc.execute();
        } catch(SQLException e) {
            throw new DbException("Failed to invoke the \"updateTransportOffer\" stored procedure:\n\"" + e.getMessage());
        }
    }


    // Removes the transport offer associated to the given id from the db (if exists)
    public void removeOffer(TransportOffer offer) throws DbException
    {
        Connection connection = DbConnection.getInstance().getConnection();

        // Create callable statement and setup parameters to invoke the deleteTransportOffer stored procedure
        try (CallableStatement deleteOfferProc = connection.prepareCall("call deleteTransportOffer(?)"))
        {
            deleteOfferProc.setInt("offerId_in", offer.getId());
            deleteOfferProc.execute();
        } catch(SQLException e) {
            throw new DbException("Failed to invoke the \"deleteTransportOffer\" stored procedure:\n" + e.getMessage());
        }
    }


    // Stores the given transport changes (the transport offer desired by the user) in the db and returns the identifier of the changes
    public int postOfferDesiredByUser(TransportChanges desiredOffer) throws DbException
    {
        int desiredTransportId = 0;

        Connection connection = DbConnection.getInstance().getConnection();
        try (CallableStatement postRequestProc = connection.prepareCall("CALL createTransportChangesRequest(?, ?, ?, ?, ?, ?)"))
        {
            postRequestProc.setString(  "newTransportType_in", desiredOffer.getType().toPersistenceType());
            postRequestProc.setInt(     "newTransportQuality_in", desiredOffer.getQuality());
            postRequestProc.setInt(     "newNumOfTravelers_in", desiredOffer.getNumOfTravelers());
            postRequestProc.setString(  "newDepartureLocationAddress_in", desiredOffer.getRoute().getDepartureLocation().getAddress());
            postRequestProc.setString(  "newArrivalLocationAddress_in", desiredOffer.getRoute().getArrivalLocation().getAddress());
            postRequestProc.registerOutParameter("transportChangesId_out", Types.INTEGER);

            postRequestProc.execute();
            desiredTransportId = postRequestProc.getInt("transportChangesId_out");
        } catch(SQLException e) {
            throw new DbException("Failed to invoke the \"createTransportChangesRequest\" stored procedure:\n" + e.getMessage());
        }

        return desiredTransportId;
    }


    // Retrieves the transport changes (the transport offer desired by the user) associated to the given id
    public TransportOffer getOfferDesiredByUser(int offerId) throws DbException
    {
        TransportOffer desiredOffer = null;
        Connection connection = DbConnection.getInstance().getConnection();
        try(CallableStatement getRequestProc = connection.prepareCall("CALL getTransportChangesRequest(?)"))
        {
            getRequestProc.setInt("requestId_in", offerId);
            boolean status = getRequestProc.execute();
            if(status)                                          // If the stored procedure returned a result set
            {
                ResultSet rs = getRequestProc.getResultSet();
                if (rs.next())                                  // Construct transport changes with data in the result set
                {
                    Route fromToLocation = new Route(
                            LocationFactory.getInstance().createLocation(rs.getString("departureLocationAddress")),
                            LocationFactory.getInstance().createLocation(rs.getString("arrivalLocationAddress"))
                    );

                    desiredOffer = ChangesFactory.getInstance().createDesiredTransportOffer(
                            TransportType.fromPersistenceType(rs.getString("transportType")),
                            rs.getInt("transportQuality"),
                            rs.getInt("numOfTravelers"),
                            fromToLocation
                    );

                    desiredOffer.setId(rs.getInt("id"));
                }
            }
        } catch (SQLException e) {
            throw new DbException("Failed to invoke the \"getTransportChangesRequest\" stored procedure:\n" + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new DbException("Cannot get transport requirements, persistence layer returned invalid data:\n" + e.getMessage());
        }

        if(desiredOffer == null)
            throw new DbException("Transport changes request not found");

        return desiredOffer;
    }


    // Removes the given transport changes request from the db (if it exists)
    public void removeOfferDesiredByUser(TransportChanges desiredOffer) throws DbException
    {
        Connection connection = DbConnection.getInstance().getConnection();
        try (CallableStatement deleteRequestProc = connection.prepareCall("CALL deleteTransportChangesRequest(?)"))
        {
            deleteRequestProc.setInt("requestId_in", desiredOffer.getId());
            deleteRequestProc.execute();
        } catch(SQLException e) {
            throw new DbException("Failed to invoke the \"deleteTransportChangesRequest\" stored procedure:\n" + e.getMessage());
        }
    }

}
