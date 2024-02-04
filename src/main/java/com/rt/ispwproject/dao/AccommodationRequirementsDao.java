package com.rt.ispwproject.dao;

import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.model.AccommodationRequirements;
import com.rt.ispwproject.model.AccommodationType;

import java.sql.*;

public class AccommodationRequirementsDao {

    // Stores given accommodation requirements in db and returns the id that identifies those requirements
    public int postRequirements(AccommodationRequirements req) throws DbException
    {
        int accommodationReqId = -1;
        Connection connection = DbConnection.getInstance().getConnection();

        // Create callable statement and setup parameters to invoke the createAccommodationRequirements stored procedure
        try (CallableStatement postReqProc = connection.prepareCall("call createAccommodationRequirements(?, ?, ?, ?)"))
        {
            postReqProc.setString("accommodationType_in", req.getType().toString());
            postReqProc.setInt("accommodationQuality_in", req.getQuality());
            postReqProc.setInt("numOfRooms_in", req.getNumOfRooms());
            postReqProc.registerOutParameter("accommodationReqId_out", Types.INTEGER);

            postReqProc.execute();
            accommodationReqId = postReqProc.getInt("accommodationReqId_out");
        } catch(SQLException e)
        {
            System.out.println(e.getMessage());
            throw new DbException("Failed to invoke the \"createAccommodationRequirements\" stored procedure:\n" + e.getMessage());
        }

        return accommodationReqId;
    }


    // Retrieves the accommodation requirements associated to the given id from the db
    public AccommodationRequirements getRequirements(int accommodationReqId) throws DbException
    {
        AccommodationRequirements accommodationReq = null;
        Connection connection = DbConnection.getInstance().getConnection();

        // Create callable statement and setup parameters to invoke the getAccommodationRequirements stored procedure
        try (CallableStatement getReqProc = connection.prepareCall("call getAccommodationRequirements(?)"))
        {
            getReqProc.setInt("requirementsId_in", accommodationReqId);

            boolean status = getReqProc.execute();
            if(status)                                      // If the stored procedure returned a result set
            {
                ResultSet rs = getReqProc.getResultSet();   // The result set should contain only one entry
                if(rs.next())
                    accommodationReq = new AccommodationRequirements(rs.getInt("id"),
                            AccommodationType.valueOf(rs.getString("accommodationType")),
                            rs.getInt("accommodationQuality"), rs.getInt("numOfRooms"));

                rs.close();
            }

        } catch(SQLException e)
        {
            throw new DbException("Failed to invoke the \"getAccommodationRequirements\" stored procedure:\n" + e.getMessage());
        } catch(IllegalArgumentException e)
        {
            throw new DbException("\"getAccommodationRequirements\" stored procedure has returned invalid data:\n" + e.getMessage());
        }

        if(accommodationReq == null)
            throw new DbException("Accommodation requirements not found");

        return accommodationReq;
    }


    // Removes the accommodation requirements associated to the given id from the db
    public void removeRequirements(int accommodationReqId) throws DbException
    {
        Connection connection = DbConnection.getInstance().getConnection();

        // Create callable statement and setup parameters to invoke the deleteAccommodationRequirements stored procedure
        try (CallableStatement deleteReqProc = connection.prepareCall("call deleteAccommodationRequirements(?)"))
        {
            deleteReqProc.setInt("requirementsId_in", accommodationReqId);
            deleteReqProc.execute();

            deleteReqProc.execute();
        } catch(SQLException e)
        {
            throw new DbException("Failed to invoke the \"deleteAccommodationRequirements\" stored procedure:\n" + e.getMessage());
        }
    }

}
