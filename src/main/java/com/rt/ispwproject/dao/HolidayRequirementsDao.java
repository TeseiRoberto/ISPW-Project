package com.rt.ispwproject.dao;

import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.model.AccommodationType;
import com.rt.ispwproject.model.HolidayRequirements;
import com.rt.ispwproject.model.TransportType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HolidayRequirementsDao {

    
    // Stores given holiday requirements in db
    public void postRequirements(int userId, HolidayRequirements requirements) throws DbException
    {
        Connection connection = DbConnection.getInstance().getConnection();

        // Create callable statement and setup parameters to invoke the createHolidayRequirements stored procedure
        try (CallableStatement saveRequirementsProc = connection.prepareCall("call createHolidayRequirements(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)") )
        {
            saveRequirementsProc.setInt("userId_in", userId);
            saveRequirementsProc.setString("destination_in", requirements.getDestination());
            saveRequirementsProc.setInt("availableBudget_in", requirements.getAvailableBudget());
            saveRequirementsProc.setInt("numOfTravelers_in", requirements.getNumOfTravelers());
            saveRequirementsProc.setString("description_in", requirements.getHolidayDescription());
            saveRequirementsProc.setDate("dateOfPost_in", Date.valueOf(requirements.getDateOfPost()));
            saveRequirementsProc.setDate("departureDate_in", Date.valueOf(requirements.getDateOfPost()));
            saveRequirementsProc.setDate("returnDate_in", Date.valueOf(requirements.getDateOfPost()));
            saveRequirementsProc.setString("accommodationType_in", requirements.getAccommodationType().toString());
            saveRequirementsProc.setInt("accommodationQuality_in", requirements.getAccommodationQuality());
            saveRequirementsProc.setInt("numOfRooms_in", requirements.getNumOfRoomsRequired());
            saveRequirementsProc.setString("transportType_in", requirements.getTransportType().toString());
            saveRequirementsProc.setInt("transportQuality_in", requirements.getTransportQuality());

            saveRequirementsProc.execute();
        } catch(SQLException e)
        {
            throw new DbException("Failed to invoke the \"createHolidayRequirements\" stored procedure");
        }

    }


    // Retrieves the holiday requirements posted by the given user from the db
    public List<HolidayRequirements> getPostedRequirements(int userId) throws DbException
    {
        ArrayList<HolidayRequirements> requirements = new ArrayList<>();
        Connection connection = DbConnection.getInstance().getConnection();

        // Create callable statement and setup parameters to invoke the getUserHolidayRequirements stored procedure
        try (CallableStatement getRequirementsProc = connection.prepareCall("call getUserHolidayRequirements(?, ?)") )
        {
            getRequirementsProc.setInt("userId_in", userId);
            getRequirementsProc.registerOutParameter("ownerUsername_out", Types.VARCHAR);

            boolean status = getRequirementsProc.execute();
            String ownerUsername = getRequirementsProc.getString("ownerUsername_out");  // Retrieve name of the user that posted the announcements

            if(status && !getRequirementsProc.wasNull())                                // If the stored procedure returned a result set
            {
                ResultSet rs = getRequirementsProc.getResultSet();
                while(rs.next())
                {
                    HolidayRequirements req = new HolidayRequirements();
                    req.setId(rs.getInt("id"));
                    req.setOwner(ownerUsername);
                    req.setDestination(rs.getString("destination"));
                    req.setHolidayDescription(rs.getString("description"));
                    req.setAvailableBudget(rs.getInt("availableBudget"));
                    req.setNumOfTravelers(rs.getInt("numOfTravelers"));
                    req.setDateOfPost(rs.getDate("dateOfPost").toLocalDate());
                    req.setDepartureDate(rs.getDate("departureDate").toLocalDate());
                    req.setReturnDate(rs.getDate("returnDate").toLocalDate());
                    req.setAccommodationType(AccommodationType.valueOf( rs.getString("accommodationType") ));
                    req.setAccommodationQuality(rs.getInt("accommodationQuality"));
                    req.setNumOfRoomsRequired(rs.getInt("numOfRooms"));
                    req.setTransportType(TransportType.valueOf( rs.getString("transportType") ));
                    req.setTransportQuality(rs.getInt("transportQuality"));
                    req.setNumOfViews(rs.getInt("numOfViews"));

                    requirements.add(req);
                }

                rs.close();
            }

        } catch (SQLException e) {
            throw new DbException("Failed to invoke the \"getUserHolidayRequirements\" stored procedure: " + e.getMessage());
        } catch(IllegalArgumentException e)
        {
            throw new DbException("\"getUserHolidayRequirements\" stored procedure has returned an unknown accommodation/transport type" + e.getMessage());
        }

        return requirements;
    }


    // Removes the holiday requirements with given id from the db (if they exist)
    public void removeRequirements(int userId, int requirementsId) throws DbException
    {
        Connection connection = DbConnection.getInstance().getConnection();

        // Create callable statement and setup parameters to invoke the deleteHolidayRequirements stored procedure
        try (CallableStatement deleteRequirementsProc = connection.prepareCall("call deleteHolidayRequirements(?, ?)") )
        {
            deleteRequirementsProc.setInt("ownerId_in", userId);
            deleteRequirementsProc.setInt("requirementsId_in", requirementsId);

            deleteRequirementsProc.execute();
        } catch(SQLException e)
        {
            throw new DbException("Failed to invoke the \"deleteHolidayRequirements\" stored procedure: " + e.getMessage());
        }
    }
}
