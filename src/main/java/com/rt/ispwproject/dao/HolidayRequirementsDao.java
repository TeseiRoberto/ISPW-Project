package com.rt.ispwproject.dao;

import com.rt.ispwproject.config.DbConnection;
import com.rt.ispwproject.dao.profiledao.ProfileDao;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.factories.ProfileDaoFactory;
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
        try (CallableStatement createReqProc = connection.prepareCall("call createHolidayRequirements(?, ?, ?, ?, ?, ?, ?, ?)"))
        {
            // Use accommodation requirements dao to post the accommodation requirements
            accommodationReqId = accommodationReqDao.postRequirements(req.getAccommodationRequirements());

            // Use transport requirements dao to post the transport requirements
            transportReqId = transportReqDao.postRequirements(req.getTransportRequirements());

            // Post holiday requirements
            createReqProc.setInt(     "userId_in", req.getMetadata().getRequirementsOwner().getUserId());
            createReqProc.setInt(     "availableBudget_in", req.getAvailableBudget());
            createReqProc.setString(  "description_in", req.getHolidayDescription());
            createReqProc.setDate(    "dateOfPost_in", Date.valueOf(req.getMetadata().getDateOfPost()) );
            createReqProc.setDate(    "departureDate_in", Date.valueOf(req.getHolidayDuration().getStartDate()) );
            createReqProc.setDate(    "returnDate_in", Date.valueOf(req.getHolidayDuration().getEndDate()) );
            createReqProc.setInt(     "accommodationReqId_in", accommodationReqId);
            createReqProc.setInt(     "transportReqId_in", transportReqId);

            createReqProc.execute();
        } catch(SQLException e)
        {
            if(accommodationReqId != 0)
            {
                AccommodationRequirements accommodationReq = accommodationReqDao.getRequirementsById(accommodationReqId);
                accommodationReqDao.removeRequirements(accommodationReq);
            }

            if(transportReqId != 0)
            {
                TransportRequirements transportReq = transportReqDao.getRequirementsById(transportReqId);
                transportReqDao.removeRequirements(transportReq);
            }

            throw new DbException("Failed to invoke the \"createHolidayRequirements\" stored procedure:\n" + e.getMessage());
        } catch(DbException e)
        {
            if(accommodationReqId != 0)
            {
                AccommodationRequirements accommodationReq = accommodationReqDao.getRequirementsById(accommodationReqId);
                accommodationReqDao.removeRequirements(accommodationReq);
            }

            if(transportReqId != 0)
            {
                TransportRequirements transportReq = transportReqDao.getRequirementsById(transportReqId);
                transportReqDao.removeRequirements(transportReq);
            }
            throw new DbException("Cannot post holiday requirements:\n" + e.getMessage());
        }

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
        } catch(DbException e) {
            throw new DbException("Cannot get holiday requirements with id " + requirementsId + ":\n" + e.getMessage());
        }

        if(requirements == null || requirements.size() != 1)
            throw new DbException("Cannot find holiday requirements with id " + requirementsId);

        return requirements.getFirst();
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
        } catch(DbException e) {
            throw new DbException("Cannot get holiday requirements posted by user:\n" + e.getMessage());
        }

        return requirements;
    }


    // Removes the given holiday requirements from the db (if they exist)
    public void removeRequirements(HolidayRequirements requirements) throws DbException
    {
        Connection connection = DbConnection.getInstance().getConnection();

        AccommodationRequirementsDao accommodationReqDao = new AccommodationRequirementsDao();
        TransportRequirementsDao transportReqDao = new TransportRequirementsDao();

        accommodationReqDao.removeRequirements(requirements.getAccommodationRequirements());
        transportReqDao.removeRequirements(requirements.getTransportRequirements());

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
    // @startId: indicates from which holiday requirements the search will start from
    // @maxRequirementsNum: max size of the list that will be returned
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
        } catch(SQLException e) {
            throw new DbException("Failed to invoke the \"searchHolidayRequirements\" stored procedure:\n" + e.getMessage());
        } catch(DbException e) {
            throw new DbException("Search of holiday requirements failed:\n" + e.getMessage());
        }

        return requirements;
    }


    // Creates a list of HolidayRequirements using data contained in the given result set.
    // @rs: result set from which the holiday requirements instances will be built
    // @maxReqNum: max size of the list that will be returned (if -1 is given then no limit is imposed)
    private List<HolidayRequirements> createHolidayRequirementsFromResultSet(ResultSet rs, int maxReqNum) throws SQLException, DbException
    {
        ArrayList<HolidayRequirements> result = new ArrayList<>();

        ProfileDao profileDao = ProfileDaoFactory.getInstance().createDao();
        AccommodationRequirementsDao accommodationReqDao = new AccommodationRequirementsDao();
        TransportRequirementsDao transportReqDao = new TransportRequirementsDao();

        try {
            while(rs.next())
            {
                if(maxReqNum != -1 && result.size() == maxReqNum)
                    break;

                // Retrieve profile of the user that posted this holiday requirements
                Profile requirementsOwner = profileDao.getProfileById(rs.getInt("ownerId"));

                HolidayRequirementsMetadata metadata = new HolidayRequirementsMetadata(
                        rs.getInt("id"),
                        requirementsOwner,
                        rs.getDate("dateOfPost").toLocalDate(),
                        rs.getInt("numOfOffersReceived"),
                        rs.getInt("numOfViews")
                );

                DateRange duration = new DateRange(rs.getDate("departureDate").toLocalDate(), rs.getDate("returnDate").toLocalDate());
                AccommodationRequirements accommodationReq = accommodationReqDao.getRequirementsById(rs.getInt("accommodationReqId"));
                TransportRequirements transportReq = transportReqDao.getRequirementsById(rs.getInt("transportReqId"));

                result.add(new HolidayRequirements(metadata, rs.getString("description"), duration, rs.getInt("budget"), accommodationReq, transportReq));
            }
        } catch (IllegalArgumentException e) {
            throw new DbException("persistence layer returned invalid data:\n" + e.getMessage());
        }

        return result;
    }
}
