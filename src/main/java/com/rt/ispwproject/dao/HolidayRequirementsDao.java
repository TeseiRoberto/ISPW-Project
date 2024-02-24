package com.rt.ispwproject.dao;

import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HolidayRequirementsDao {

    
    // Stores given holiday requirements in db
    public void postRequirements(HolidayRequirements req) throws DbException
    {
        Connection connection = DbConnection.getInstance().getConnection();

        AccommodationRequirementsDao accommodationReqDao = new AccommodationRequirementsDao();
        TransportRequirementsDao transportReqDao = new TransportRequirementsDao();
        int accommodationReqId = 0;
        int transportReqId = 0;

        // Create callable statement and setup parameters to invoke the createHolidayRequirements stored procedure
        try (CallableStatement createReqProc = connection.prepareCall("call createHolidayRequirements(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"))
        {
            // Use accommodation requirements dao to post the accommodation requirements
            accommodationReqId = accommodationReqDao.postRequirements(req.getAccommodationRequirements());

            // Use transport requirements dao to post the transport requirements
            transportReqId = transportReqDao.postRequirements(req.getTransportRequirements());

            // Post holiday requirements
            createReqProc.setInt(     "userId_in", req.getMetadata().getRequirementsOwner().getUserId());
            createReqProc.setString(  "destinationAddress_in", req.getDestination().getAddress());
            createReqProc.setDouble(  "destinationLatitude_in", req.getDestination().getLatitude());
            createReqProc.setDouble(  "destinationLongitude_in", req.getDestination().getLongitude());
            createReqProc.setInt(     "availableBudget_in", req.getBudget());
            createReqProc.setString(  "description_in", req.getHolidayDescription());
            createReqProc.setDate(    "dateOfPost_in", Date.valueOf(req.getMetadata().getDateOfPost()) );
            createReqProc.setDate(    "departureDate_in", Date.valueOf(req.getDepartureDate()) );
            createReqProc.setDate(    "returnDate_in", Date.valueOf(req.getReturnDate()) );
            createReqProc.setInt(     "accommodationReqId_in", accommodationReqId);
            createReqProc.setInt(     "transportReqId_in", transportReqId);

            createReqProc.execute();
        } catch(SQLException e)
        {
            if(accommodationReqId != 0)
                accommodationReqDao.removeRequirements(accommodationReqId);

            if(transportReqId != 0)
                transportReqDao.removeRequirements(transportReqId);

            throw new DbException("Failed to invoke the \"createHolidayRequirements\" stored procedure:\n" + e.getMessage());
        } catch(DbException e)
        {
            if(accommodationReqId != 0)
                accommodationReqDao.removeRequirements(accommodationReqId);

            if(transportReqId != 0)
                transportReqDao.removeRequirements(transportReqId);
            throw new DbException("Cannot post holiday requirements:\n" + e.getMessage());
        }

    }


    // Retrieves the holiday requirements posted by the given user from the db
    public List<HolidayRequirements> getRequirementsPostedByUser(int userId) throws DbException
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
            throw new DbException("Failed to invoke the \"getHolidayRequirements\" stored procedure:\n" + e.getMessage());
        } catch(IllegalArgumentException e)
        {
            throw new DbException("\"getHolidayRequirements\" stored procedure has returned invalid data:\n" + e.getMessage());
        }

        return requirements;
    }


    // Retrieves the holiday requirements associated to the given id from the db
    public HolidayRequirements getRequirementsById(int requirementsId) throws DbException
    {
        List<HolidayRequirements> requirements = null;
        Connection connection = DbConnection.getInstance().getConnection();
        try (CallableStatement getReqProc = connection.prepareCall("call getHolidayRequirementsById(?)"))
        {
            getReqProc.setInt("requirementsId_in", requirementsId);
            boolean status = getReqProc.execute();
            if(status)                                      // If the stored procedure returned a result set
            {
                ResultSet rs = getReqProc.getResultSet();
                requirements = createHolidayRequirementsFromResultSet(rs, -1);
                rs.close();
            }

        } catch (SQLException e) {
            throw new DbException("Failed to invoke the \"getHolidayRequirementsById\" stored procedure:\n" + e.getMessage());
        } catch(IllegalArgumentException e)
        {
            throw new DbException("\"getHolidayRequirementsById\" stored procedure has returned invalid data:\n" + e.getMessage());
        }

        if(requirements == null || requirements.size() != 1)
            throw new DbException("Cannot find holiday requirements with id " + requirementsId);

        return requirements.getFirst();
    }


    // Removes the given holiday requirements from the db (if they exist)
    public void removeRequirements(HolidayRequirements requirements) throws DbException
    {
        Connection connection = DbConnection.getInstance().getConnection();

        AccommodationRequirementsDao accommodationReqDao = new AccommodationRequirementsDao();
        TransportRequirementsDao transportReqDao = new TransportRequirementsDao();

        accommodationReqDao.removeRequirements(requirements.getAccommodationRequirements().getId());
        transportReqDao.removeRequirements(requirements.getTransportRequirements().getId());

        // Create callable statement and setup parameters to invoke the deleteHolidayRequirements stored procedure
        try (CallableStatement deleteRequirementsProc = connection.prepareCall("call deleteHolidayRequirements(?)") )
        {
            deleteRequirementsProc.setInt("requirementsId_in", requirements.getMetadata().getRequirementsId());
            deleteRequirementsProc.execute();
        } catch(SQLException e)
        {
            throw new DbException("Failed to invoke the \"deleteHolidayRequirements\" stored procedure:\n" + e.getMessage());
        }
    }


    // Retrieves holiday requirements which id is equal to or grater than start id from the db
    public List<HolidayRequirements> searchRequirements(int startId, int maxRequirementsNum) throws DbException
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

        ProfileDao profileDao = new ProfileDao();
        AccommodationRequirementsDao accommodationReqDao = new AccommodationRequirementsDao();
        TransportRequirementsDao transportReqDao = new TransportRequirementsDao();

        while(rs.next())
        {
            if(maxReqNum != -1 && result.size() == maxReqNum)
                break;

            // Retrieve profile of the user that posted this holiday requirements
            Profile requirementsOwner = profileDao.getProfile(rs.getInt("ownerId"));

            HolidayRequirementsMetadata metadata = new HolidayRequirementsMetadata(
                    rs.getInt("id"),
                    requirementsOwner,
                    rs.getDate("dateOfPost").toLocalDate(),
                    rs.getInt("numOfOffersReceived"),
                    rs.getInt("numOfViews")
            );

            Location destination = new Location(rs.getString("destinationAddress"), rs.getDouble("destinationLatitude"), rs.getDouble("destinationLongitude"));
            DateRange duration = new DateRange(rs.getDate("departureDate").toLocalDate(), rs.getDate("returnDate").toLocalDate());
            AccommodationRequirements accommodationReq = accommodationReqDao.getRequirements(rs.getInt("accommodationReqId"));
            TransportRequirements transportReq = transportReqDao.getRequirements(rs.getInt("transportReqId"));

            result.add(new HolidayRequirements(metadata, destination, rs.getString("description"), duration, rs.getInt("budget"), accommodationReq, transportReq));
        }

        return result;
    }
}
