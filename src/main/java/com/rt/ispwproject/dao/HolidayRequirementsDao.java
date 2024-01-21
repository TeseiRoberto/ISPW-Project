package com.rt.ispwproject.dao;

import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HolidayRequirementsDao {

    
    // Stores given holiday requirements in db
    public void postRequirements(int userId, HolidayRequirements requirements) throws DbException
    {
        Connection connection = DbConnection.getInstance().getConnection();

        // Create callable statement and setup parameters to invoke the createHolidayRequirements stored procedure
        try (CallableStatement saveRequirementsProc = connection.prepareCall("call createHolidayRequirements(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"))
        {
            saveRequirementsProc.setInt("userId_in", userId);
            saveRequirementsProc.setString("destination_in", requirements.getDestination());
            saveRequirementsProc.setInt("availableBudget_in", requirements.getAvailableBudget());
            saveRequirementsProc.setString("description_in", requirements.getHolidayDescription());
            saveRequirementsProc.setDate("dateOfPost_in", Date.valueOf(requirements.getMetadata().getDateOfPost()));
            saveRequirementsProc.setDate("departureDate_in", Date.valueOf(requirements.getDepartureDate()));
            saveRequirementsProc.setDate("returnDate_in", Date.valueOf(requirements.getReturnDate()));
            saveRequirementsProc.setString("accommodationType_in", requirements.getAccommodation().getType().toString());
            saveRequirementsProc.setInt("accommodationQuality_in", requirements.getAccommodation().getQuality());
            saveRequirementsProc.setInt("numOfRooms_in", requirements.getAccommodation().getNumOfRooms());
            saveRequirementsProc.setString("transportType_in", requirements.getTransport().getType().toString());
            saveRequirementsProc.setInt("transportQuality_in", requirements.getTransport().getQuality());
            saveRequirementsProc.setInt("numOfTravelers_in", requirements.getTransport().getNumOfTravelers());
            saveRequirementsProc.setString("departureLocation_in", requirements.getTransport().getDepartureLocation());

            saveRequirementsProc.execute();
        } catch(SQLException e)
        {
            throw new DbException("Failed to invoke the \"createHolidayRequirements\" stored procedure");
        }

    }


    // Retrieves the holiday requirements posted by the given user from the db
    public List<HolidayRequirements> loadRequirementsPostedByUser(int userId) throws DbException
    {
        ArrayList<HolidayRequirements> requirements = new ArrayList<>();
        Connection connection = DbConnection.getInstance().getConnection();

        // Create callable statement and setup parameters to invoke the getUserHolidayRequirements stored procedure
        try (CallableStatement getRequirementsProc = connection.prepareCall("call getUserHolidayRequirements(?)"))
        {
            getRequirementsProc.setInt("userId_in", userId);

            boolean status = getRequirementsProc.execute();
            if(status)                                      // If the stored procedure returned a result set
            {
                ResultSet rs = getRequirementsProc.getResultSet();
                while(rs.next())
                    requirements.add(createHolidayRequirementsFromResultSet(rs));

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
    public void removeRequirementsPostedByUser(int userId, int requirementsId) throws DbException
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


    // Retrieves holiday requirements (which id is equal to or grater that start id) from the db
    public List<HolidayRequirements> loadRequirements(int startId, int maxRequirementsNum) throws DbException
    {
        ArrayList<HolidayRequirements> requirements = new ArrayList<>();
        Connection connection = DbConnection.getInstance().getConnection();

        // Create callable statement and setup parameters to invoke the searchHolidayRequirements stored procedure
        try (CallableStatement searchRequirementsProc = connection.prepareCall("call searchHolidayRequirements(?)"))
        {
            searchRequirementsProc.setInt("startId_in", startId);

            boolean status = searchRequirementsProc.execute();
            if(status)                                      // If the stored procedure returned a result set
            {
                ResultSet rs = searchRequirementsProc.getResultSet();
                while (rs.next() && requirements.size() < maxRequirementsNum)
                    requirements.add(createHolidayRequirementsFromResultSet(rs));

                rs.close();
            }
        } catch(SQLException e)
        {
            throw new DbException("Failed to invoke the \"searchHolidayRequirements\" stored procedure: " + e.getMessage());
        }

        return requirements;
    }


    // Creates an instance of HolidayRequirements using data contained in a row of the given result set.
    private HolidayRequirements createHolidayRequirementsFromResultSet(ResultSet rs) throws SQLException
    {
        HolidayMetadata metadata = new HolidayMetadata(rs.getInt("id"), rs.getString("ownerUsername"), rs.getDate("dateOfPost").toLocalDate(), rs.getInt("numOfViews"));
        DateRange duration = new DateRange(rs.getDate("departureDate").toLocalDate(), rs.getDate("returnDate").toLocalDate());

        Accommodation accommodation = new Accommodation(AccommodationType.valueOf(rs.getString("accommodationType")), rs.getInt("accommodationQuality"), rs.getInt("numOfRooms"));
        Transport transport = new Transport(TransportType.valueOf(rs.getString("transportType")), rs.getInt("transportQuality"),
                new Route(rs.getString("departureLocation"), rs.getString("destination")), rs.getInt("numOfTravelers"));

        return new HolidayRequirements(metadata, rs.getString("destination"), rs.getString("description"), duration, rs.getInt("budget"), accommodation, transport);
    }
}
