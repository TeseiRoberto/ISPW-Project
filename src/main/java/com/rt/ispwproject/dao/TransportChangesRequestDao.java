package com.rt.ispwproject.dao;

import com.rt.ispwproject.config.DbConnection;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.factories.LocationFactory;
import com.rt.ispwproject.factories.TransportFactory;
import com.rt.ispwproject.model.*;

import java.sql.*;

public class TransportChangesRequestDao {


    // Stores the changes requested on the transport in the db, returns the identifier of the changes
    public int postChangesRequest(TransportChangesRequest request) throws DbException
    {
        int transportChangesId = 0;

        Connection connection = DbConnection.getInstance().getConnection();
        try (CallableStatement postRequestProc = connection.prepareCall("CALL createTransportChangesRequest(?, ?, ?, ?, ?, ?)"))
        {
            postRequestProc.setString(  "newTransportType_in", request.getType().toPersistenceType());
            postRequestProc.setInt(     "newTransportQuality_in", request.getQuality());
            postRequestProc.setInt(     "newNumOfTravelers_in", request.getNumOfTravelers());
            postRequestProc.setString(  "newDepartureLocationAddress_in", request.getRoute().getDepartureLocation().getAddress());
            postRequestProc.setString(  "newArrivalLocationAddress_in", request.getRoute().getArrivalLocation().getAddress());
            postRequestProc.registerOutParameter("transportChangesId_out", Types.INTEGER);

            postRequestProc.execute();
            transportChangesId = postRequestProc.getInt("transportChangesId_out");
        } catch(SQLException e) {
            throw new DbException("Failed to invoke the \"createTransportChangesRequest\" stored procedure:\n" + e.getMessage());
        }

        return transportChangesId;
    }


    // Retrieves the request of changes associated to the given id from the db
    public TransportChangesRequest getChangesRequestById(int requestId) throws DbException
    {
        TransportChangesRequest request = null;
        Connection connection = DbConnection.getInstance().getConnection();
        try(CallableStatement getRequestProc = connection.prepareCall("CALL getTransportChangesRequest(?)"))
        {
            getRequestProc.setInt("requestId_in", requestId);
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

                    request = TransportFactory.getInstance().createChangesRequest(
                            TransportType.fromPersistenceType(rs.getString("transportType")),
                            rs.getInt("transportQuality"),
                            fromToLocation,
                            rs.getInt("numOfTravelers")
                    );

                    request.setId(rs.getInt("id"));
                }
            }
        } catch (SQLException e) {
            throw new DbException("Failed to invoke the \"getTransportChangesRequest\" stored procedure:\n" + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new DbException("Cannot get transport requirements, persistence layer returned invalid data:\n" + e.getMessage());
        }

        if(request == null)
            throw new DbException("Transport changes request not found");

        return request;
    }


    // Removes the given transport changes request from the db (if it exists)
    public void removeChangesRequest(TransportChangesRequest request) throws DbException
    {
        Connection connection = DbConnection.getInstance().getConnection();
        try (CallableStatement deleteRequestProc = connection.prepareCall("CALL deleteTransportChangesRequest(?)"))
        {
            deleteRequestProc.setInt("requestId_in", request.getId());
            deleteRequestProc.execute();
        } catch(SQLException e) {
            throw new DbException("Failed to invoke the \"deleteTransportChangesRequest\" stored procedure:\n" + e.getMessage());
        }
    }


}
