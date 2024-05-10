package com.rt.ispwproject.dao;

import com.rt.ispwproject.config.DbConnection;
import com.rt.ispwproject.dao.profiledao.ProfileDao;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.factories.ChangesFactory;
import com.rt.ispwproject.factories.ProfileDaoFactory;
import com.rt.ispwproject.model.*;

import java.sql.*;

public class ChangesRequestDao {


    // Stores the given ChangesRequest in the db
    public void postRequest(ChangesRequest changes) throws DbException
    {
        AccommodationOfferDao accommodationDao = new AccommodationOfferDao();
        TransportOfferDao transportDao = new TransportOfferDao();

        int desiredAccommodationId = 0;
        int desiredTransportId = 0;

        Connection connection = DbConnection.getInstance().getConnection();
        try (CallableStatement createRequestProc = connection.prepareCall("CALL createChangesRequest(?, ?, ?, ?, ?, ?, ?, ?, ?)"))
        {
            createRequestProc.setInt("ownerId_in", changes.getRequestOwner().getUserId());
            createRequestProc.setInt("relativeOfferId_in", changes.getRelativeOfferId());
            createRequestProc.setInt("bidderAgencyId_in", changes.getRelativeOfferOwner().getUserId());
            createRequestProc.setString("changesDescription_in", changes.getChangesDescription());
            createRequestProc.setNull("newDepartureDate_in", Types.DATE);
            createRequestProc.setNull("newReturnDate_in", Types.DATE);
            createRequestProc.setNull("requiredPrice_in", Types.INTEGER);

            // Set parameters of the stored procedure according to the requested changes
            if(changes.isHolidayDurationChangeRequired())
            {
                createRequestProc.setDate("newDepartureDate_in", Date.valueOf(changes.getHolidayDurationChange().getStartDate()));
                createRequestProc.setDate("newReturnDate_in", Date.valueOf(changes.getHolidayDurationChange().getEndDate()));
            }

            if(changes.isPriceChangeRequired())
                createRequestProc.setInt("requiredPrice_in", changes.getPriceChange());

            if(changes.isAccommodationChangeRequired())
                desiredAccommodationId = accommodationDao.postOfferDesiredByUser(changes.getAccommodationChanges());

            if(changes.isTransportChangeRequired())
                desiredTransportId = transportDao.postOfferDesiredByUser(changes.getTransportChanges());

            createRequestProc.setInt("accommodationChangesId_in", desiredAccommodationId);
            createRequestProc.setInt("transportChangesId_in", desiredTransportId);

            createRequestProc.execute();
        } catch(SQLException e) {
            if(desiredAccommodationId != 0)
            {
                AccommodationOffer desiredAccommodation = accommodationDao.getOfferDesiredByUser(desiredAccommodationId);
                accommodationDao.removeOfferDesiredByUser(desiredAccommodation);
            }

            if(desiredTransportId != 0)
            {
                TransportOffer desiredTransport = transportDao.getOfferDesiredByUser(desiredTransportId);
                transportDao.removeOfferDesiredByUser(desiredTransport);
            }

            throw new DbException("Failed to invoke the \"createChangesRequest\" stored procedure:\n" + e.getMessage());
        } catch (DbException e) {
            if(desiredAccommodationId != 0)
            {
                AccommodationOffer desiredAccommodation = accommodationDao.getOfferDesiredByUser(desiredAccommodationId);
                accommodationDao.removeOfferDesiredByUser(desiredAccommodation);
            }

            if(desiredTransportId != 0)
            {
                TransportOffer desiredTransport = transportDao.getOfferDesiredByUser(desiredTransportId);
                transportDao.removeOfferDesiredByUser(desiredTransport);
            }

            throw new DbException("Cannot post request of changes:\n" + e.getMessage());
        }

    }


    // Retrieves the changes requested on the holiday offer associated to the given id (if there is a request)
    public ChangesRequest getChangesRequestForOffer(int offerId) throws DbException
    {
        ChangesRequest request = null;

        // Let's retrieve data of the desired offer
        Connection connection = DbConnection.getInstance().getConnection();
        try (CallableStatement getRequestProc = connection.prepareCall("CALL getRequestedChangesOnOffer(?)"))
        {
            getRequestProc.setInt("offerId_in", offerId);
            boolean status = getRequestProc.execute();
            if(status)
            {
                ResultSet rs = getRequestProc.getResultSet();
                if(rs.next())
                    request = createChangesRequestFromResultSet(rs);
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
        ChangesRequest request= null;

        Connection connection = DbConnection.getInstance().getConnection();
        try (CallableStatement getRequestProc = connection.prepareCall("CALL getChangesRequestById(?)"))
        {
            getRequestProc.setInt("requestId_in", requestId);
            boolean status = getRequestProc.execute();
            if(status)
            {
                ResultSet rs = getRequestProc.getResultSet();
                if(rs.next())
                    request = createChangesRequestFromResultSet(rs);
            }

        } catch (SQLException e) {
            throw new DbException("Failed to invoke the \"getChangesRequestById\" stored procedure:\n" + e.getMessage());
        } catch(DbException e) {
            throw new DbException("Cannot get request of changes with id " + requestId + ":\n" + e.getMessage());
        }

        if(request == null)
            throw new DbException("Cannot find request of changes with id: " + requestId);

        return request;
    }


    // Removes the given request of changes from the db
    public void removeRequest(ChangesRequest request) throws DbException
    {
        Connection connection = DbConnection.getInstance().getConnection();
        try (CallableStatement removeRequestProc = connection.prepareCall("CALL deleteChangesRequest(?)"))
        {
            if(request.isAccommodationChangeRequired())     // Delete the accommodation desired by the user (if present)
            {
                AccommodationOfferDao accommodationDao = new AccommodationOfferDao();
                accommodationDao.removeOfferDesiredByUser(request.getAccommodationChanges());
            }

            if(request.isTransportChangeRequired())         // Delete the transport offer desired by the user (if present)
            {
                TransportOfferDao transportDao = new TransportOfferDao();
                transportDao.removeOfferDesiredByUser(request.getTransportChanges());
            }

            removeRequestProc.setInt("requestId_in", request.getRequestId());
            removeRequestProc.execute();

        } catch (SQLException e) {
            throw new DbException("Failed to invoke the \"deleteChangesRequest\" stored procedure:\n" + e.getMessage());
        } catch (DbException e) {
            throw new DbException("Cannot remove request of changes:\n" + e.getMessage());
        }
    }


    // Creates an HolidayOffer instance that represents the offer desired by the user using data contained in the given result set
    private ChangesRequest createChangesRequestFromResultSet(ResultSet rs) throws SQLException, DbException
    {
        String changesDescription = rs.getString("changesDescription");

        // Retrieve the original holiday offer made by the travel agency
        HolidayOfferDao offerDao = new HolidayOfferDao();
        HolidayOffer agencyOffer = offerDao.getOfferById(rs.getInt("relativeOfferId"));

        // Retrieve profile of the owner of the desired offer
        ProfileDao profileDao = ProfileDaoFactory.getInstance().createDao();
        Profile offerOwner = profileDao.getProfileById(rs.getInt("ownerId"));

        // Now let's build the desired holiday offer using not-null data present the result set
        HolidayOffer desiredOffer;

        try {
            HolidayOfferMetadata metadata = new HolidayOfferMetadata(
                    rs.getInt("id"),
                    offerOwner,
                    HolidayOfferState.PENDING,
                    rs.getInt("relativeOfferId"),
                    agencyOffer.getMetadata().getOfferOwner()
            );

            // By default, the desired holiday offer is equal to the one made by the travel agency
            desiredOffer = new HolidayOffer(
                    metadata,
                    agencyOffer.getDestination(),
                    agencyOffer.getHolidayDuration(),
                    agencyOffer.getPrice(),
                    agencyOffer.getAccommodationOffer(),
                    agencyOffer.getTransportOffer()
            );

            Date newDepartureDate = rs.getDate("newDepartureDate");
            if (!rs.wasNull()) {
                DateRange newHolidayDuration = new DateRange(
                        newDepartureDate.toLocalDate(),
                        rs.getDate("newReturnDate").toLocalDate()
                );

                desiredOffer.setHolidayDuration(newHolidayDuration);
            }

            int requiredPrice = rs.getInt("newPrice");
            if (!rs.wasNull())
                desiredOffer.setPrice(requiredPrice);

            int accommodationId = rs.getInt("accommodationChangesId");
            if (accommodationId != 0) {
                AccommodationOfferDao accommodationDao = new AccommodationOfferDao();
                AccommodationOffer desiredAccommodation = accommodationDao.getOfferDesiredByUser(accommodationId);
                desiredOffer.setAccommodationOffer(desiredAccommodation);
            }

            int transportId = rs.getInt("transportChangesId");
            if (transportId != 0) {
                TransportOfferDao transportDao = new TransportOfferDao();
                TransportOffer desiredTransport = transportDao.getOfferDesiredByUser(transportId);
                desiredOffer.setTransportOffer(desiredTransport);

                desiredOffer.setDestination(desiredTransport.getRoute().getArrivalLocation());
            }

        } catch (IllegalArgumentException e) {
            throw new DbException("persistence layer returned invalid data:\n" + e.getMessage());
        }

        return ChangesFactory.getInstance().createChangesRequest(agencyOffer, desiredOffer, changesDescription);
    }

}

