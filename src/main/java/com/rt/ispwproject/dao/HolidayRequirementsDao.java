package com.rt.ispwproject.dao;

import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HolidayRequirementsDao {

    
    // Stores given holiday requirements in db
    public void postRequirements(int userId, HolidayRequirements req) throws DbException
    {
        Connection connection = DbConnection.getInstance().getConnection();

        AccommodationRequirementsDao accommodationReqDao = new AccommodationRequirementsDao();
        int accommodationReqId = 0;

        TransportRequirementsDao transportReqDao = new TransportRequirementsDao();
        int transportReqId = 0;

        // Create callable statement and setup parameters to invoke the createHolidayRequirements stored procedure
        try (CallableStatement postReqProc = connection.prepareCall("call createHolidayRequirements(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"))
        {
            // Use accommodation requirements dao to post the accommodation requirements
            accommodationReqId = accommodationReqDao.postRequirements(req.getAccommodation());

            // Use transport requirements dao to post the transport requirements
            transportReqId = transportReqDao.postRequirements(req.getTransport());

            // Post holiday requirements
            postReqProc.setInt("userId_in", userId);
            postReqProc.setString("destinationName_in", req.getDestination().getName());
            postReqProc.setDouble("destinationLatitude_in", req.getDestination().getLatitude());
            postReqProc.setDouble("destinationLongitude_in", req.getDestination().getLongitude());
            postReqProc.setInt("availableBudget_in", req.getBudget());
            postReqProc.setString("description_in", req.getHolidayDescription());
            postReqProc.setDate("dateOfPost_in", Date.valueOf(req.getMetadata().getDateOfPost()) );
            postReqProc.setDate("departureDate_in", Date.valueOf(req.getDepartureDate()) );
            postReqProc.setDate("returnDate_in", Date.valueOf(req.getReturnDate()) );
            postReqProc.setInt("accommodationReqId_in", accommodationReqId);
            postReqProc.setInt("transportReqId_in", transportReqId);

            postReqProc.execute();
        } catch(SQLException e)
        {
            if(accommodationReqId != 0)
                accommodationReqDao.removeRequirements(accommodationReqId);

            if(transportReqId != 0)
                transportReqDao.removeRequirements(transportReqId);

            throw new DbException("Failed to invoke the \"createHolidayRequirements\" stored procedure:\n" + e.getMessage());
        } catch(DbException e)
        {
            if(accommodationReqId != -1)
                accommodationReqDao.removeRequirements(accommodationReqId);

            if(transportReqId != -1)
                transportReqDao.removeRequirements(transportReqId);
            throw new DbException("Cannot post holiday requirements:\n" + e.getMessage());
        }

    }


    // Retrieves the holiday requirements posted by the given user from the db
    public List<HolidayRequirements> loadRequirementsPostedByUser(int userId) throws DbException
    {
        List<HolidayRequirements> requirements = null;
        Connection connection = DbConnection.getInstance().getConnection();

        // Create callable statement and setup parameters to invoke the getHolidayRequirements stored procedure
        try (CallableStatement getReqProc = connection.prepareCall("call getHolidayRequirements(?)"))
        {
            getReqProc.setInt("userId_in", userId);

            boolean status = getReqProc.execute();
            if(status)                                      // If the stored procedure returned a result set
            {
                ResultSet rs = getReqProc.getResultSet();
                requirements = createHolidayRequirementsFromResultSet(rs, -1);
                rs.close();
            }

        } catch (SQLException e) {
            throw new DbException("Failed to invoke the \"getUserHolidayRequirements\" stored procedure:\n" + e.getMessage());
        } catch(IllegalArgumentException e)
        {
            throw new DbException("\"getUserHolidayRequirements\" stored procedure has returned invalid data:\n" + e.getMessage());
        }

        return requirements;
    }


    // Removes the holiday requirements with given id from the db (if they exist)
    public void removeRequirementsPostedByUser(int userId, int holidayReqId) throws DbException
    {
        Connection connection = DbConnection.getInstance().getConnection();

        // We need to check that the holiday requirements associated to the given id exists and were actually
        // posted by the user associated with the given user id
        List<HolidayRequirements> reqs = loadRequirementsPostedByUser(userId);

        for(HolidayRequirements req : reqs)
        {
            if(req.getMetadata().getHolidayId() == holidayReqId)
            {
                AccommodationRequirementsDao accommodationReqDao = new AccommodationRequirementsDao();
                accommodationReqDao.removeRequirements(req.getAccommodation().getId());

                TransportRequirementsDao transportReqDao = new TransportRequirementsDao();
                transportReqDao.removeRequirements(req.getTransport().getId());

                // Create callable statement and setup parameters to invoke the deleteHolidayRequirements stored procedure
                try (CallableStatement deleteRequirementsProc = connection.prepareCall("call deleteHolidayRequirements(?)") )
                {
                    deleteRequirementsProc.setInt("requirementsId_in", req.getMetadata().getHolidayId());
                    deleteRequirementsProc.execute();
                } catch(SQLException e)
                {
                    throw new DbException("Failed to invoke the \"deleteHolidayRequirements\" stored procedure:\n" + e.getMessage());
                }

                break;
            }
        }
    }


    // Retrieves holiday requirements which id is equal to or grater than start id from the db
    public List<HolidayRequirements> loadRequirements(int startId, int maxRequirementsNum) throws DbException
    {
        List<HolidayRequirements> requirements = null;
        Connection connection = DbConnection.getInstance().getConnection();

        // Create callable statement and setup parameters to invoke the searchHolidayRequirements stored procedure
        try (CallableStatement searchRequirementsProc = connection.prepareCall("call searchHolidayRequirements(?)"))
        {
            searchRequirementsProc.setInt("startId_in", startId);

            boolean status = searchRequirementsProc.execute();
            if(status)                                      // If the stored procedure returned a result set
            {
                ResultSet rs = searchRequirementsProc.getResultSet();
                requirements = createHolidayRequirementsFromResultSet(rs, maxRequirementsNum);
                rs.close();
            }
        } catch(SQLException e)
        {
            throw new DbException("Failed to invoke the \"searchHolidayRequirements\" stored procedure:\n" + e.getMessage());
        } catch(IllegalArgumentException e)
        {
            throw new DbException("\"searchHolidayRequirements\" stored procedure has returned invalid data:\n" + e.getMessage());
        }

        return requirements;
    }


    // Creates a list of HolidayRequirements using data contained in the given result set.
    // The returned list will have a size between 0 and maxReqNum if maxReqNum is != -1
    private List<HolidayRequirements> createHolidayRequirementsFromResultSet(ResultSet rs, int maxReqNum) throws SQLException, IllegalArgumentException, DbException
    {
        ArrayList<HolidayRequirements> result = new ArrayList<>();
        AccommodationRequirementsDao accommodationReqDao = new AccommodationRequirementsDao();
        TransportRequirementsDao transportReqDao = new TransportRequirementsDao();

        while(rs.next())
        {
            if(maxReqNum != -1 && result.size() == maxReqNum)
                break;

            HolidayMetadata metadata = new HolidayMetadata(rs.getInt("id"),
                    rs.getString("ownerUsername"),
                    rs.getDate("dateOfPost").toLocalDate(),
                    rs.getInt("numOfViews")
            );

            Location destination = new Location(rs.getString("destinationName"), rs.getDouble("destinationLatitude"), rs.getDouble("destinationLongitude"));
            DateRange duration = new DateRange(rs.getDate("departureDate").toLocalDate(), rs.getDate("returnDate").toLocalDate());
            AccommodationRequirements accommodationReq = accommodationReqDao.getRequirements(rs.getInt("accommodationReqId"));
            TransportRequirements transportReq = transportReqDao.getRequirements(rs.getInt("transportReqId"));

            result.add(new HolidayRequirements(metadata, destination, rs.getString("description"), duration, rs.getInt("budget"), accommodationReq, transportReq));
        }

        return result;
    }
}
