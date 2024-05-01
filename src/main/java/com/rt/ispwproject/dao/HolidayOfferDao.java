package com.rt.ispwproject.dao;

import com.rt.ispwproject.config.DbConnection;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.factories.LocationFactory;
import com.rt.ispwproject.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HolidayOfferDao {


    // Stores given holiday offer in db
    public void postOffer(HolidayOffer offer) throws DbException
    {
        Connection connection = DbConnection.getInstance().getConnection();

        AccommodationOfferDao accommodationOfferDao = new AccommodationOfferDao();
        TransportOfferDao transportOfferDao = new TransportOfferDao();
        int accommodationOfferId = 0;
        int transportOfferId = 0;

        try (CallableStatement createOfferProc = connection.prepareCall("call createHolidayOffer(?, ?, ?, ?, ?, ?, ?, ?)"))
        {
            // Use accommodation offer dao to post the accommodation offer
            accommodationOfferId = accommodationOfferDao.postOffer(offer.getAccommodationOffer());

            // Use transport offer dao to post the accommodation offer
            transportOfferId = transportOfferDao.postOffer(offer.getTransportOffer());

            createOfferProc.setInt(     "requirementsId_in", offer.getMetadata().getRelativeRequirementsId());
            createOfferProc.setInt(     "bidderAgencyId_in", offer.getMetadata().getOfferOwner().getUserId());
            createOfferProc.setString(  "destinationAddress_in", offer.getDestination().getAddress());
            createOfferProc.setInt(     "holidayPrice_in", offer.getPrice());
            createOfferProc.setDate(    "holidayStartDate_in", Date.valueOf(offer.getHolidayDuration().getStartDate()));
            createOfferProc.setDate(    "holidayEndDate_in", Date.valueOf(offer.getHolidayDuration().getEndDate()));
            createOfferProc.setInt(     "accommodationOfferId_in", accommodationOfferId);
            createOfferProc.setInt(     "transportOfferId_in", transportOfferId);

            createOfferProc.execute();
        } catch(SQLException e)
        {
            if(accommodationOfferId != 0)
            {
                AccommodationOffer accommodationOffer = accommodationOfferDao.getOfferById(accommodationOfferId);
                accommodationOfferDao.removeOffer(accommodationOffer);
            }

            if(transportOfferId != 0)
            {
                TransportOffer transportOffer = transportOfferDao.getOfferById(transportOfferId);
                transportOfferDao.removeOffer(transportOffer);
            }

            throw new DbException("Failed to invoke the \"createHolidayOffer\" stored procedure:\n\"" + e.getMessage());
        } catch (DbException e)
        {
            if(accommodationOfferId != 0)
            {
                AccommodationOffer accommodationOffer = accommodationOfferDao.getOfferById(accommodationOfferId);
                accommodationOfferDao.removeOffer(accommodationOffer);
            }

            if(transportOfferId != 0)
            {
                TransportOffer transportOffer = transportOfferDao.getOfferById(transportOfferId);
                transportOfferDao.removeOffer(transportOffer);
            }

            throw new DbException("Cannot post holiday offer:\n" + e.getMessage());
        }
    }


    // Retrieves the holiday offer associated to the given id from the db
    public HolidayOffer getOfferById(int offerId) throws DbException
    {
        List<HolidayOffer> holidayOffer = null;
        Connection connection = DbConnection.getInstance().getConnection();

        try (CallableStatement getOfferProc = connection.prepareCall("CALL getHolidayOfferById(?)"))
        {
            getOfferProc.setInt("offerId_in", offerId);
            boolean status = getOfferProc.execute();
            if(status)                                      // If the stored procedure returned a result set
            {
                ResultSet rs = getOfferProc.getResultSet();
                holidayOffer = createHolidayOffersFromResultSet(rs);
                rs.close();
            }

        } catch(SQLException e) {
            throw new DbException("Failed to invoke the \"getHolidayOfferById\" stored procedure:\n" + e.getMessage());
        }

        if(holidayOffer == null || holidayOffer.size() != 1)
            throw new DbException("Cannot find holiday offer with id " + offerId);

        return holidayOffer.getFirst();
    }


    // Retrieves all the holiday offers made by travel agencies to the holiday requirements associated to the given holidayReqId
    public List<HolidayOffer> getOffersForHolidayRequirements(int holidayReqId) throws DbException
    {
        List<HolidayOffer> holidayOffers = null;
        Connection connection = DbConnection.getInstance().getConnection();

        try (CallableStatement getOffersProc = connection.prepareCall("call getHolidayOffersForRequirements(?)"))
        {
            getOffersProc.setInt("requirementsId_in", holidayReqId);
            boolean status = getOffersProc.execute();
            if(status)                                      // If the stored procedure returned a result set
            {
                ResultSet rs = getOffersProc.getResultSet();
                holidayOffers = createHolidayOffersFromResultSet(rs);
                rs.close();
            }

        } catch(SQLException e) {
            throw new DbException("Failed to invoke the \"getHolidayOffersForRequirements\" stored procedure:\n\"" + e.getMessage());
        } catch (DbException e) {
            throw new DbException("Cannot load holiday offers for the given holiday requirements:\n" + e.getMessage());
        }

        return holidayOffers;
    }


    // Retrieves all the holiday offers made by the user associated to the given userId
    public List<HolidayOffer> getOffersMadeByUser(int userId) throws DbException
    {
        List<HolidayOffer> holidayOffers = null;
        Connection connection = DbConnection.getInstance().getConnection();

        try (CallableStatement getOffersProc = connection.prepareCall("call getHolidayOffersPostedByUser(?)"))
        {
            getOffersProc.setInt("userId_in", userId);
            boolean status = getOffersProc.execute();
            if(status)                                      // If the stored procedure returned a result set
            {
                ResultSet rs = getOffersProc.getResultSet();
                holidayOffers = createHolidayOffersFromResultSet(rs);
                rs.close();
            }

        } catch(SQLException e) {
            throw new DbException("Failed to invoke the \"getHolidayOffersPostedByUser\" stored procedure:\n\"" + e.getMessage());
        } catch (DbException e) {
            throw new DbException("Cannot load holiday offers made by the given user:\n" + e.getMessage());
        }

        return holidayOffers;
    }


    // Updates the state of the holiday offer associated to the given offerId
    public void updateOffer(HolidayOffer newOffer) throws DbException
    {
        AccommodationOfferDao accommodationDao = new AccommodationOfferDao();
        TransportOfferDao transportDao = new TransportOfferDao();

        accommodationDao.updateOffer(newOffer.getAccommodationOffer());
        transportDao.updateOffer(newOffer.getTransportOffer());

        Connection connection = DbConnection.getInstance().getConnection();
        try (CallableStatement updateOfferProc = connection.prepareCall("call updateHolidayOffer(?, ?, ?, ?, ?, ?, ?, ?)"))
        {
            updateOfferProc.setInt("offerId_in", newOffer.getMetadata().getOfferId());
            updateOfferProc.setInt("newRequirementsId_in", newOffer.getMetadata().getRelativeRequirementsId());
            updateOfferProc.setInt("newBidderAgencyId_in", newOffer.getMetadata().getOfferOwner().getUserId());
            updateOfferProc.setString("newDestinationAddress_in", newOffer.getDestination().getAddress());
            updateOfferProc.setInt("newHolidayPrice_in", newOffer.getPrice());
            updateOfferProc.setDate("newHolidayStartDate_in", Date.valueOf(newOffer.getHolidayDuration().getStartDate()));
            updateOfferProc.setDate("newHolidayEndDate_in", Date.valueOf(newOffer.getHolidayDuration().getEndDate()));
            updateOfferProc.setString("newOfferState_in", newOffer.getMetadata().getOfferState().toPersistenceType());

            updateOfferProc.execute();
        } catch(SQLException e) {
            throw new DbException("Failed to invoke the \"updateHolidayOffer\" stored procedure:\n\"" + e.getMessage());
        }
    }


    // Creates a list of HolidayOffer using data contained in the given result set.
    private List<HolidayOffer> createHolidayOffersFromResultSet(ResultSet rs) throws SQLException, IllegalArgumentException, DbException
    {
        ArrayList<HolidayOffer> result = new ArrayList<>();

        ProfileDao profileDao = new ProfileDao();
        AccommodationOfferDao accommodationOfferDao = new AccommodationOfferDao();
        TransportOfferDao transportOfferDao = new TransportOfferDao();
        LocationFactory locationFactory = new LocationFactory();

        try {
            while(rs.next())
            {
                // Retrieve the user that made the offer and the one to which the offer is intended to
                Profile bidderAgency = profileDao.getProfileById(rs.getInt("bidderAgencyId"));
                Profile relativeReqOwner = profileDao.getProfileById(rs.getInt("relativeRequirementsOwnerId"));

                HolidayOfferMetadata metadata = new HolidayOfferMetadata(
                        rs.getInt("id"),
                        bidderAgency,
                        HolidayOfferState.fromPersistenceType(rs.getString("offerState")),
                        rs.getInt("relativeRequirementsId"),
                        relativeReqOwner
                );

                DateRange duration = new DateRange(
                        rs.getDate("holidayStartDate").toLocalDate(),
                        rs.getDate("holidayEndDate").toLocalDate()
                );

                int accommodationOfferId = rs.getInt("accommodationOfferId");
                int transportOfferId = rs.getInt("transportOfferId");
                AccommodationOffer accommodationOffer = accommodationOfferDao.getOfferById(accommodationOfferId);
                TransportOffer transportOffer = transportOfferDao.getOfferById(transportOfferId);

                Location destination = locationFactory.createLocation(rs.getString("destinationAddress"));
                result.add(new HolidayOffer(metadata, destination, duration, rs.getInt("holidayPrice"), accommodationOffer, transportOffer));
            }
        } catch (IllegalArgumentException e) {
            throw new DbException("persistence layer returned invalid data:\n" + e.getMessage());
        }

        return result;
    }

}
