package com.rt.ispwproject.dao;

import com.rt.ispwproject.config.DbConnection;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.factories.AccommodationFactory;
import com.rt.ispwproject.model.AccommodationChangesRequest;
import com.rt.ispwproject.model.AccommodationType;

import java.sql.*;

public class AccommodationChangesRequestDao {


    // Stores the changes requested on the accommodation in the db, returns the identifier of the changes
    public int postChangesRequest(AccommodationChangesRequest request) throws DbException
    {
        int accommodationChangesId = 0;
        Connection connection = DbConnection.getInstance().getConnection();
        try (CallableStatement postRequestProc = connection.prepareCall("CALL createAccommodationChangesRequest(?, ?, ?, ?)"))
        {
            postRequestProc.setString("newAccommodationType_in", request.getType().toPersistenceType());
            postRequestProc.setInt("newAccommodationQuality_in", request.getQuality());
            postRequestProc.setInt("newNumOfRooms_in", request.getNumOfRooms());
            postRequestProc.registerOutParameter("accommodationChangesId_out", Types.INTEGER);

            postRequestProc.execute();
            accommodationChangesId = postRequestProc.getInt("accommodationChangesId_out");
        } catch(SQLException e) {
            throw new DbException("Failed to invoke the \"createAccommodationChangesRequest\" stored procedure:\n" + e.getMessage());
        }

        return accommodationChangesId;
    }


    // Retrieves the request of changes associated to the given id from the db
    public AccommodationChangesRequest getChangesRequestById(int requestId) throws DbException
    {
        AccommodationChangesRequest request = null;
        Connection connection = DbConnection.getInstance().getConnection();
        try(CallableStatement getRequestProc = connection.prepareCall("CALL getAccommodationChangesRequest(?)"))
        {
            getRequestProc.setInt("requestId_in", requestId);
            boolean status = getRequestProc.execute();
            if(status)                                          // If the stored procedure returned a result set
            {
                ResultSet rs = getRequestProc.getResultSet();
                if (rs.next())                                  // Construct accommodation changes with data in the result set
                {
                    request = AccommodationFactory.getInstance().createChangesRequest(
                            AccommodationType.fromPersistenceType(rs.getString("accommodationType")),
                            rs.getInt("accommodationQuality"),
                            rs.getInt("numOfRooms")
                    );

                    request.setId(rs.getInt("id"));
                }
            }
        } catch (SQLException e) {
            throw new DbException("Failed to invoke the \"getAccommodationChangesRequest\" stored procedure:\n" + e.getMessage());
        }

        if(request == null)
            throw new DbException("Accommodation changes request not found");

        return request;
    }


    // Removes the given accommodation changes request from the db (if it exists)
    public void removeChangesRequest(AccommodationChangesRequest request) throws DbException
    {
        Connection connection = DbConnection.getInstance().getConnection();
        try (CallableStatement deleteRequestProc = connection.prepareCall("CALL deleteAccommodationChangesRequest(?)"))
        {
            deleteRequestProc.setInt("requestId_in", request.getId());
            deleteRequestProc.execute();
        } catch(SQLException e) {
            throw new DbException("Failed to invoke the \"deleteAccommodationChangesRequest\" stored procedure:\n" + e.getMessage());
        }
    }


}
