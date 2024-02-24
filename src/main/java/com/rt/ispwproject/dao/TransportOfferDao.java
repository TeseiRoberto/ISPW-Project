package com.rt.ispwproject.dao;

import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.model.*;

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
            createOfferProc.setString(  "transportType_in", offer.getType().toPersistenceType());
            createOfferProc.setString(  "companyName_in", offer.getCompanyName());
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


    // Retrieves the transport offer associated to the given id from the db (if exists)
    public TransportOffer getOffer(int offerId) throws DbException
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
                    Route fromToLocation = new Route(
                            new Location(
                                    rs.getString("departureLocationAddress"),
                                    rs.getDouble("departureLocationLatitude"),
                                    rs.getDouble("departureLocationLongitude")
                            ),
                            new Location(
                                    rs.getString("arrivalLocationAddress"),
                                    rs.getDouble("arrivalLocationLatitude"),
                                    rs.getDouble("arrivalLocationLongitude")
                            )
                    );

                    DateRange departureAndReturnDates = new DateRange(
                            rs.getDate("departureDate").toLocalDate(),
                            rs.getDate("returnDate").toLocalDate()
                    );

                    offer = new TransportOffer(
                            TransportType.fromPersistenceType(rs.getString("transportType")),
                            rs.getString("companyName"),
                            rs.getInt("transportQuality"),
                            fromToLocation,
                            rs.getInt("numOfTravelers"),
                            rs.getInt("pricePerTraveler"),
                            departureAndReturnDates
                    );

                    offer.setCompanyId(rs.getInt("companyId"));
                }
                rs.close();
            }
        } catch(SQLException e)
        {
            throw new DbException("Failed to invoke the \"getTransportOffer\" stored procedure:\n" + e.getMessage());
        } catch(IllegalArgumentException e)
        {
            throw new DbException("The \"getTransportOffer\" stored procedure has returned invalid data:\n" + e.getMessage());
        }

        if(offer == null)
            throw new DbException("Transport offer not found");

        return offer;
    }

    // Updates all the fields of the offer in the db with the newOffer data (the offer must already exist in the db)
    public void updateOffer(TransportOffer newOffer) throws DbException
    {
        Connection connection = DbConnection.getInstance().getConnection();
        try (CallableStatement updateOfferProc = connection.prepareCall("call updateTransportOffer(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"))
        {
            updateOfferProc.setInt("transportOfferId_in", newOffer.getId());
            updateOfferProc.setString("newTransportType_in", newOffer.getType().toPersistenceType());
            updateOfferProc.setString("newCompanyName_in", newOffer.getCompanyName());
            updateOfferProc.setInt("newCompanyId_in", newOffer.getCompanyId());
            updateOfferProc.setInt("newTransportQuality_in", newOffer.getQuality());
            updateOfferProc.setInt("newNumOfTravelers_in", newOffer.getNumOfTravelers());
            updateOfferProc.setInt("newPricePerTraveler_in", newOffer.getPricePerTraveler());
            updateOfferProc.setString("newDepartureLocationAddress_in", newOffer.getDepartureLocation().getAddress());
            updateOfferProc.setDouble("newDepartureLocationLatitude_in", newOffer.getDepartureLocation().getLatitude());
            updateOfferProc.setDouble("newDepartureLocationLongitude_in", newOffer.getDepartureLocation().getLongitude());
            updateOfferProc.setString("newArrivalLocationAddress_in", newOffer.getArrivalLocation().getAddress());
            updateOfferProc.setDouble("newArrivalLocationLatitude_in", newOffer.getArrivalLocation().getLatitude());
            updateOfferProc.setDouble("newArrivalLocationLongitude_in", newOffer.getArrivalLocation().getLongitude());
            updateOfferProc.setDate("newDepartureDate_in", Date.valueOf(newOffer.getDepartureDate()));
            updateOfferProc.setDate("newReturnDate_in", Date.valueOf(newOffer.getReturnDate()));

            updateOfferProc.execute();
        } catch(SQLException e)
        {
            throw new DbException("Failed to invoke the \"updateTransportOffer\" stored procedure:\n\"" + e.getMessage());
        }
    }


    // Removes the transport offer associated to the given id from the db (if exists)
    public void removeOffer(int offerId) throws DbException
    {
        Connection connection = DbConnection.getInstance().getConnection();

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
