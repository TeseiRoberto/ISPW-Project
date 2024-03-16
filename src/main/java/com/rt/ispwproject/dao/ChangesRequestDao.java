package com.rt.ispwproject.dao;

import com.rt.ispwproject.config.DbConnection;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.model.*;

import java.sql.*;

public class ChangesRequestDao {


    // Stores the given ChangesRequest in the db
    public void postRequest(ChangesRequest changes) throws DbException
    {
        AccommodationChangesRequestDao accommodationChangesDao = new AccommodationChangesRequestDao();
        TransportChangesRequestDao transportChangesDao = new TransportChangesRequestDao();

        int accommodationChangesId = 0;
        int transportChangesId = 0;

        Connection connection = DbConnection.getInstance().getConnection();
        try (CallableStatement createRequestProc = connection.prepareCall("CALL createChangesRequest(?, ?, ?, ?, ?, ?, ?, ?, ?)"))
        {
            createRequestProc.setInt("ownerId_in", changes.getMetadata().getRequestOwner().getUserId());
            createRequestProc.setInt("relativeOfferId_in", changes.getMetadata().getRelativeOfferId());
            createRequestProc.setInt("bidderAgencyId_in", changes.getMetadata().getRelativeOfferOwner().getUserId());
            createRequestProc.setString("changesDescription_in", changes.getChangesDescription());

            // Set parameters of the stored procedure according to the requested changes
            if(changes.isHolidayDurationChangeRequired())
            {
                createRequestProc.setDate("newDepartureDate_in", Date.valueOf(changes.getHolidayDuration().getStartDate()));
                createRequestProc.setDate("newReturnDate_in", Date.valueOf(changes.getHolidayDuration().getEndDate()));
            } else {
                createRequestProc.setNull("newDepartureDate_in", Types.DATE);
                createRequestProc.setNull("newReturnDate_in", Types.DATE);
            }

            if(changes.isPriceChangeRequired())
                createRequestProc.setInt("requiredPrice_in", changes.getPrice());
            else
                createRequestProc.setNull("requiredPrice_in", Types.INTEGER);

            if(changes.isAccommodationChangeRequired())
                accommodationChangesId = accommodationChangesDao.postChangesRequest(changes.getAccommodationChanges());

            if(changes.isTransportChangeRequired())
                transportChangesId = transportChangesDao.postChangesRequest(changes.getTransportChanges());

            createRequestProc.setInt("accommodationChangesId_in", accommodationChangesId);
            createRequestProc.setInt("transportChangesId_in", transportChangesId);

            createRequestProc.execute();
        } catch(SQLException e)
        {
            if(accommodationChangesId != 0)
            {
                AccommodationChangesRequest accommodationChanges = accommodationChangesDao.getChangesRequestById(accommodationChangesId);
                accommodationChangesDao.removeChangesRequest(accommodationChanges);
            }

            if(transportChangesId != 0)
            {
                TransportChangesRequest transportChanges = transportChangesDao.getChangesRequestById(transportChangesId);
                transportChangesDao.removeChangesRequest(transportChanges);
            }

            throw new DbException("Failed to invoke the \"createChangesRequest\" stored procedure:\n" + e.getMessage());
        } catch (DbException e)
        {
            if(accommodationChangesId != 0)
            {
                AccommodationChangesRequest accommodationChanges = accommodationChangesDao.getChangesRequestById(accommodationChangesId);
                accommodationChangesDao.removeChangesRequest(accommodationChanges);
            }

            if(transportChangesId != 0)
            {
                TransportChangesRequest transportChanges = transportChangesDao.getChangesRequestById(transportChangesId);
                transportChangesDao.removeChangesRequest(transportChanges);
            }

            throw new DbException("Cannot post request of changes:\n" + e.getMessage());
        }
    }


    // Retrieves the changes requested on the holiday offer associated to the given id (if there is a request)
    public ChangesRequest getChangesRequestForOffer(int offerId) throws DbException
    {
        ChangesRequest request = null;
        Connection connection = DbConnection.getInstance().getConnection();
        try (CallableStatement getRequestProc = connection.prepareCall("CALL getRequestedChangesOnOffer(?)"))
        {
            getRequestProc.setInt("offerId_in", offerId);
            boolean status = getRequestProc.execute();
            if(status)
            {
                ResultSet rs = getRequestProc.getResultSet();
                request = createRequestFromResultSet(rs);
            }

        } catch (SQLException e) {
            throw new DbException("Failed to invoke the \"getRequestedChangesOnOffer\" stored procedure:\n" + e.getMessage());
        } catch(DbException e) {
            throw new DbException("Cannot get changes requested on holiday offer with id " + offerId + ":\n" + e.getMessage());
        }

        return request;
    }


    // Retrieves the request of changes associated to the given id from the db
    public ChangesRequest getChangesRequestById(int requestId) throws DbException
    {
        ChangesRequest request = null;
        Connection connection = DbConnection.getInstance().getConnection();
        try (CallableStatement getRequestProc = connection.prepareCall("CALL getChangesRequestById(?)"))
        {
            getRequestProc.setInt("requestId_in", requestId);
            boolean status = getRequestProc.execute();
            if(status)
            {
                ResultSet rs = getRequestProc.getResultSet();
                request = createRequestFromResultSet(rs);
            }

        } catch (SQLException e) {
            throw new DbException("Failed to invoke the \"getChangesRequestById\" stored procedure:\n" + e.getMessage());
        } catch(DbException e) {
            throw new DbException("Cannot get request of changes with id " + requestId + ":\n" + e.getMessage());
        }

        return request;
    }


    // Removes the request of changes associated to the given id from the db
    public void removeRequest(ChangesRequest request) throws DbException
    {
        Connection connection = DbConnection.getInstance().getConnection();
        try (CallableStatement removeRequestProc = connection.prepareCall("CALL deleteChangesRequest(?)"))
        {
            removeRequestProc.setInt("requestId_in", request.getMetadata().getRequestId());
            removeRequestProc.execute();

            if(request.isAccommodationChangeRequired())
            {
                AccommodationChangesRequestDao accommodationChangesDao = new AccommodationChangesRequestDao();
                accommodationChangesDao.removeChangesRequest(request.getAccommodationChanges());
            }

            if(request.isTransportChangeRequired())
            {
                TransportChangesRequestDao transportChangesDao = new TransportChangesRequestDao();
                transportChangesDao.removeChangesRequest(request.getTransportChanges());
            }

        } catch (SQLException e) {
            throw new DbException("Failed to invoke the \"deleteChangesRequest\" stored procedure:\n" + e.getMessage());
        } catch (DbException e) {
            throw new DbException("Cannot remove request of changes:\n" + e.getMessage());
        }
    }


    // Creates a ChangesRequest instance using data contained in the given result set
    private ChangesRequest createRequestFromResultSet(ResultSet rs) throws SQLException, DbException
    {
        if(!rs.next())
            return null;

        // Retrieve profile of the request owner and the request addressee
        ProfileDao profileDao = new ProfileDao();
        Profile requestOwner = profileDao.getProfileById(rs.getInt("ownerId"));
        Profile requestAddressee = profileDao.getProfileById(rs.getInt("bidderAgencyId"));

        ChangesRequest request = null;
        try {
            ChangesRequestMetadata metadata = new ChangesRequestMetadata(
                    rs.getInt("id"),
                    requestOwner,
                    rs.getInt("relativeOfferId"),
                    requestAddressee
            );

            request = new ChangesRequest(metadata, rs.getString("changesDescription"));

            // Populate the changes request with not-null data present the result set
            Date newDepartureDate = rs.getDate("newDepartureDate");
            if(!rs.wasNull())
            {
                DateRange newHolidayDuration = new DateRange(
                        newDepartureDate.toLocalDate(),
                        rs.getDate("newReturnDate").toLocalDate()
                );

                request.setHolidayDuration(newHolidayDuration);
            }

            int requiredPrice = rs.getInt("newPrice");
            if(!rs.wasNull())
                request.setPrice(requiredPrice);

            int accommodationChangesId = rs.getInt("accommodationChangesId");
            if(accommodationChangesId != 0)
            {
                AccommodationChangesRequestDao accommodationChangesDao = new AccommodationChangesRequestDao();
                AccommodationChangesRequest changes = accommodationChangesDao.getChangesRequestById(accommodationChangesId);
                request.setAccommodationChanges(changes);
            }

            int transportChangesId = rs.getInt("transportChangesId");
            if(transportChangesId != 0)
            {
                TransportChangesRequestDao accommodationChangesDao = new TransportChangesRequestDao();
                TransportChangesRequest changes = accommodationChangesDao.getChangesRequestById(transportChangesId);
                request.setTransportChanges(changes);
            }
        } catch (IllegalArgumentException e) {
            throw new DbException("persistence layer returned invalid data:\n" + e.getMessage());
        }

        return request;
    }

}
