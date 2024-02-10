package com.rt.ispwproject.dao;

import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.model.*;

import java.sql.*;

public class TransportRequirementsDao {

    // Stores given transport requirements in db and returns the id that identifies those requirements
    public int postRequirements(TransportRequirements req) throws DbException
    {
        int transportReqId = 0;
        Connection connection = DbConnection.getInstance().getConnection();

        // Create callable statement and setup parameters to invoke the createTransportRequirements stored procedure
        try (CallableStatement createReqProc = connection.prepareCall("call createTransportRequirements(?, ?, ?, ?, ?, ?, ?)"))
        {
            createReqProc.setString(  "transportType_in", req.getType().toPersistenceType());
            createReqProc.setInt(     "transportQuality_in", req.getQuality());
            createReqProc.setInt(     "numOfTravelers_in", req.getNumOfTravelers());
            createReqProc.setString(  "departureLocationAddress_in", req.getDepartureLocation().getAddress());
            createReqProc.setDouble(  "departureLocationLatitude_in", req.getDepartureLocation().getLatitude());
            createReqProc.setDouble(  "departureLocationLongitude_in", req.getDepartureLocation().getLongitude());
            createReqProc.registerOutParameter("transportReqId_out", Types.INTEGER);

            createReqProc.execute();
            transportReqId = createReqProc.getInt("transportReqId_out");
        } catch(SQLException e)
        {
            throw new DbException("Failed to invoke the \"createTransportRequirements\" stored procedure:\n" + e.getMessage());
        }

        return transportReqId;
    }


    // Retrieves the transport requirements associated to the given id from the db
    public TransportRequirements getRequirements(int transportReqId) throws DbException
    {
        if(transportReqId <= 0)
            return null;

        TransportRequirements transportReq = null;
        Connection connection = DbConnection.getInstance().getConnection();

        // Create callable statement and setup parameters to invoke the getTransportRequirements stored procedure
        try (CallableStatement getReqProc = connection.prepareCall("call getTransportRequirements(?)"))
        {
            getReqProc.setInt("requirementsId_in", transportReqId);

            boolean status = getReqProc.execute();
            if(status)                                      // If the stored procedure returned a result set
            {
                ResultSet rs = getReqProc.getResultSet();   // The result set should contain only one entry
                if(rs.next())
                {
                    Location departureLocation = new Location(rs.getString("departureLocationAddress"),
                            rs.getDouble("departureLocationLatitude"),
                            rs.getDouble("departureLocationLongitude"));

                    transportReq = new TransportRequirements(rs.getInt("id"),
                            TransportType.fromPersistenceType(rs.getString("transportType")),
                            rs.getInt("transportQuality"),
                            rs.getInt("numOfTravelers"),
                            departureLocation);
                }

                rs.close();
            }

        } catch(SQLException e)
        {
            throw new DbException("Failed to invoke the \"getTransportRequirements\" stored procedure:\n" + e.getMessage());
        } catch(IllegalArgumentException e)
        {
            throw new DbException("\"getTransportRequirements\" stored procedure has returned invalid data:\n" + e.getMessage());
        }

        if(transportReq == null)
            throw new DbException("Transport requirements not found");

        return transportReq;
    }


    // Removes the transport requirements associated to the given id from the db
    public void removeRequirements(int transportReqId) throws DbException
    {
        if(transportReqId <= 0)
            return;

        Connection connection = DbConnection.getInstance().getConnection();

        // Create callable statement and setup parameters to invoke the deleteTransportRequirements stored procedure
        try (CallableStatement deleteReqProc = connection.prepareCall("call deleteTransportRequirements(?)"))
        {
            deleteReqProc.setInt("requirementsId_in", transportReqId);
            deleteReqProc.execute();

            deleteReqProc.execute();
        } catch(SQLException e)
        {
            throw new DbException("Failed to invoke the \"deleteTransportRequirements\" stored procedure:\n" + e.getMessage());
        }
    }

}
